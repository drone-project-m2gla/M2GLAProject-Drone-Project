from morse.builder import *
import math

# must be called before creating component
bpymorse.set_speed(fps=60, logic_step_max=5, physics_step_max=5)

# Simple quadrotor with rigid body physics
quadrotor = Quadrotor()
quadrotor.translate(x=-1.2483, y=1.7043, z=1.8106)
quadrotor.name = 'mav'

camera = VideoCamera()

camera.name = "camera"
camera.translate(x=0.05,z=-0.05)
camera.properties(cam_width=640,cam_heigth=480)
camera.frequency(15)
camera.add_interface('ros')
quadrotor.append(camera)

motion = RotorcraftWaypoint()
motion.name = 'waypoint'
# read a Pose message
motion.add_stream('ros')

quadrotor.append(motion)

imu = IMU()
imu.name = 'imu'
# IMU with z-axis down (NED)
imu.rotate(x=math.pi)
imu.add_stream('ros')
quadrotor.append(imu)

env = Environment('fac/fac')
env.show_framerate(True)
#env.show_physics(True)

env.create()