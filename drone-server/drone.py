from morse.builder import *
import math

# must be called before creating component
bpymorse.set_speed(fps=60, logic_step_max=5, physics_step_max=5)

# Simple quadrotor with rigid body physics
'''quadrotor = Submarine()'''
quadrotor = Quadrotor()
quadrotor.translate(x=-0, y=0, z=20)
quadrotor.name = 'drone'

camera = VideoCamera()

camera.name = "camera"
camera.translate(x=0.05,z=-0.50)
camera.properties(cam_width=640,cam_heigth=480,cam_near=15,cam_far=500)
camera.frequency(15)
camera.add_interface('ros')
quadrotor.append(camera)

odometry = Odometry()
odometry.translate(x=0.05,z=0.23)
odometry.add_stream('ros')
quadrotor.append(odometry)

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

env = Environment('fac.blend')
env.show_framerate(True)
#env.show_physics(True)

env.set_camera_location([10.0, -10.0, 10.0])
env.set_camera_rotation([1.0470, 0, 0.7854])
env.set_camera_clip(clip_start=1, clip_end=500)

env.create()
