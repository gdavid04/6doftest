# 6DOF Test

## Zero gravity space
- [x] no gravity
- [x] swim movement
  - [ ] no swim up/down based on pitch
- [ ] correct on ground to use all surface orientations
- [x] no fall damage
- [ ] bump into wall damage regardless of direction
- [ ] make mob AI less helpless in zero gravity

## 6DOF
- [x] camera
  - [ ] fix glitch when looking upside down (gimbal lock?)
- [x] model pose
- [ ] look around
- [x] controls for roll
- [ ] camera relative movement (hook Entity.movementInputToVelocity)
- [ ] sync orientation
- [x] animate transition back to upright (roll)
