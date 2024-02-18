# 6DOF Test

## Zero gravity space
-[x] no gravity
-[x] swim movement
  -[ ] no swim up/down based on pitch
-[ ] correct on ground to use all surface orientations
-[x] no fall damage
-[ ] bump into wall damage regardless of direction
-[ ] make mob AI less helpless in zero gravity

## 6DOF
-[x] camera
  -[ ] fix skybox not using the correct transform for some reason
  -[ ] fix culling transform
-[x] model pose
-[ ] look around
-[ ] controls for roll
-[ ] camera relative movement (hook Entity.movementInputToVelocity)
-[ ] sync orientation
-[x] animate transition back to upright (roll)
