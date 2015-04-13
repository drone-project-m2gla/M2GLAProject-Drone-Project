from flask import Flask, jsonify, request
from flask_restful import abort
import math
'''from math import cos
from math import sin
from math import acos'''
import rospy
from geometry_msgs.msg import Pose
from geometry_msgs.msg import Point
from sensor_msgs.msg import Image
from cv_bridge import CvBridge, CvBridgeError

def gpsToPoint(lat,lng):
    if lng + 1.641408 > 0:
        coefx = 1
    else :
        coefx =-1

    if lat - 48.117451 > 0:
        coefy = 1
    else:
        coefy =-1

    x =  coefx * gpsToMeter(lat,lng, lat,-1.641408)
    y =  coefy * gpsToMeter(lat,lng,48.117451,lng)
    return Point(x,y,10)

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

class Command:
    def __init__(self):
        self.cmd = rospy.Publisher("/mav/waypoint", Pose, queue_size=10, latch=True)

    def setWaypoint(self, x, y, z):
        point = gpsToPoint(x,y)
        print("Point " + str(point.x) + " " + str(point.y))
        pose = Pose(position=point)
        self.cmd.publish(pose)

    def getWaypoint(self):
        pose = Pose(position=Point(x,y,z))
        self.cmd.publish(pose)
        return Point(x,y,y)

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

@app.route('/robot/waypoint', methods=['POST'])
def waypoint():
    global command
    if not request.json or not 'x' in request.json or not 'y' in request.json or not 'z' in request.json:
        abort(400)
    
    x = request.json['x']
    y = request.json['y']
    z = request.json['z']
    command.setWaypoint(x, y, z)
    
    return jsonify({"x": x, "y": y, "z": z}), 201

@app.route('/robot/picture', methods=['GET'])
def getpicture() :    
    ''' rospy.init_node("cat_node")'''
    command.subscriber = rospy.Subscriber("cat/camera/image", Image, command.process_image, queue_size = 10, latch=True)
    return jsonify({"message":"reussi"})

if __name__ == '__main__' :
    rospy.init_node("flask")
    app.run(debug=True, host='0.0.0.0', port=5000)
