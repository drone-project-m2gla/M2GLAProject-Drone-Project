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
    if lng - lng0 > 0:
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
    command.pose = data.pose.pose

def callbackImage(data):
    command.saveImg = data
    command.cmdImage.unregister()
    rospy.sleep(1.)
    command.cmdImage = rospy.Subscriber("/drone/camera/image", Image, callbackImage, queue_size=1)

class Command:
    def __init__(self):
        self.cmdWaypoint = rospy.Publisher("/drone/waypoint", Pose, queue_size=10, latch=True)
        self.cmdOdometry = rospy.Subscriber("/drone/odometry",Odometry, callbackOdometry, queue_size=1)
        self.cmdImage = rospy.Subscriber("/drone/camera/image", Image, callbackImage, queue_size=1)

    def setWaypoint(self, x, y, z):
        point = gpsToPoint(y,x)
        print("Go to Point " + str(point.x) + " " + str(point.y))
        pose = Pose(position=point)
        self.cmdWaypoint.publish(pose)

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
    if not request.json or not 'latitude' in request.json or not 'longitude' in request.json or not 'altitude' in request.json:
        abort(400)    
    x = request.json['longitude']
    y = request.json['latitude']
    z = request.json['altitude']
    command.setWaypoint(x, y, z)
    
    return jsonify({"longitude": x, "latitude": y, "altitude": z}), 201

@app.route('/robot/position', methods=['GET'])
def getPosition():
    x = command.pose.position.x
    y = command.pose.position.y
    z = command.pose.position.z
    t = meterToGps(x,y)

    return jsonify({"latitude": t[0], "longitude": t[1], "altitude": z}), 200

def encoreImage(img):
    result = ""
    for i in range(0, len(img)):
        result += str(img[i]) + ","

@app.route('/robot/picture', methods=['GET'])
def getpicture() :
    return jsonify({
    	"width": command.saveImg.width,
    	"height": command.saveImg.height,
    	"encoding": command.saveImg.encoding,
    	"image": str(command.saveImg.data)
    })

if __name__ == '__main__' :
    rospy.init_node("flask")
    app.run(debug=True, host='0.0.0.0', port=5000)