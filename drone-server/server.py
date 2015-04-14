from flask import Flask, jsonify, request
from flask_restful import abort
import math
import rospy
from nav_msgs.msg import Odometry
from geometry_msgs.msg import Pose
from geometry_msgs.msg import Point
from sensor_msgs.msg import Image
from cv_bridge import CvBridge, CvBridgeError
lat0 = 48.117451
lng0 = -1.641408

def gpsToPoint(lat,lng):
    if lng + lng0 > 0:
        coefx = 1
    else :
        coefx =-1

    if lat - lat0 > 0:
        coefy = 1
    else:
        coefy =-1

    x =  coefx * gpsToMeter(lat,lng, lat,lng0)
    y =  coefy * gpsToMeter(lat,lng, lat0,lng)
    return Point(x,y,20)

def gpsToMeter(lat_a, lng_a, lat_b, lng_b):
    angleLatA = math.radians(lat_a)
    angleLngA = math.radians(lng_a)
    angleLatB = math.radians(lat_b)
    angleLngB = math.radians(lng_b)

    t1 = math.cos(angleLatA)*math.cos(angleLngA)*math.cos(angleLatB)*math.cos(angleLngB)
    t2 = math.cos(angleLatA)*math.sin(angleLngA)*math.cos(angleLatB)*math.sin(angleLngB)
    t3 = math.sin(angleLatA)*math.sin(angleLatB)
    tt = math.acos(t1 + t2 + t3) 
    return 6366000*tt

def meterToGps(x,y):
    lat = lat0 + (180/ math.pi)*(y/6378137)
    lng = lng0 + (180/ math.pi)*(x/6378137)/math.cos(lat0)
    return (lat,lng)

def callbackOdometry(data):
    '''rospy.loginfo (" postition d'odometry %s ", data.pose.pose)'''
    command.pose = data.pose.pose

class Command:

    def __init__(self):
        self.cmdWaypoint = rospy.Publisher("/drone/waypoint", Pose, queue_size=10, latch=True)
        self.cmdOdometry = rospy.Subscriber("/drone/odometry",Odometry,callbackOdometry)

    def setWaypoint(self, x, y, z):
        point = gpsToPoint(x,y)
        print("Go to Point " + str(point.x) + " " + str(point.y))
        pose = Pose(position=point)
        self.cmdWaypoint.publish(pose)

    def process_image(self, ros_image):
        print("processing image")
        #### direct conversion to CV2 ####
        bridge = CvBridge()
        cv_image = bridge.imgmsg_to_cv2(ros_image, desired_encoding="rgb8")
        cv2.imwrite("testimage.png", cv_image)

command = Command()
app = Flask(__name__)

@app.route('/robot/rotate', methods=['POST'])
def rotate() :
    if not request.json or not 'd' in request.json:
        abort(400)
    
    d = request.json['d']
    
    return jsonify({"d": d}), 201

@app.route('/robot/position', methods=['POST'])
def setPosition():
    global command
    if not request.json or not 'x' in request.json or not 'y' in request.json or not 'z' in request.json:
        abort(400)    
    x = request.json['x']
    y = request.json['y']
    z = request.json['z']
    command.setWaypoint(x, y, z)
    
    return jsonify({"x": x, "y": y, "z": z}), 201

@app.route('/robot/position', methods=['GET'])
def getPosition():
    x = command.pose.position.x
    y = command.pose.position.y
    z = command.pose.position.z
    t = meterToGps(x,y)    
    return jsonify({"x": t[0], "y": t[1], "z": z}), 200

@app.route('/robot/picture', methods=['GET'])
def getpicture() :    
    command.subscriber = rospy.Subscriber("cat/camera/image", Image, command.process_image, queue_size = 10, latch=True)
    return jsonify({"message":"reussi"})

if __name__ == '__main__' :
    rospy.init_node("flask")
    app.run(debug=True, host='0.0.0.0', port=5000)
