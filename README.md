# 6DOF Test
A WIP space mod for Fabric 1.20.1 with 6DOF movement.

## Zero gravity space
- [x] no gravity
- [x] swim movement
  - [x] no swim up/down based on pitch
  - [x] swim up/down based on jump/sneak
- [ ] correct on ground to use all surface orientations
- [ ] disable jumping or make it work in all directions
- [x] no fall damage
- [ ] bump into wall damage regardless of direction
- [ ] make mob AI less helpless
- [ ] make knockback work vertically
- [ ] make pushing entities work vertically
- [ ] disable water flowing downwards
- [ ] make area effect clouds spherical
- [ ] consider slowing down movement without thrusters
  - [ ] consider making movement speed depend on pressure when not using thrusters
  - [ ] allow launching off of walls faster
  - [ ] allow moving along walls normally without thrusters

## 6DOF
- [x] camera
  - [ ] fix debug cursor orientation
- [x] model pose
- [x] look around
  - [ ] fix third person front view rotation controls
- [x] controls for roll
- [x] camera relative movement
- [ ] sync orientation
- [x] animate transition back to upright (roll)

## Air
- [x] make breath meter deplete in space
- [ ] oxygen tanks
- [ ] pressurization system
  - [ ] airlocks
- [ ] make vacuum suck entities and air out of pressurized areas
- [ ] make vacuum deal damage

## Equipment
- [ ] space suit
  - [ ] thrusters
    - [ ] fuel
    - [ ] option to use oxygen instead of fuel
    - [ ] particle effects when using thrusters
      - [ ] forward
      - [ ] directional thrusters
      - [ ] rotation
    - [ ] sound effects when using thrusters
    - [ ] make forwards thrust faster
  - [ ] helmet
    - [ ] overlay
    - [ ] oxygen supply
- [ ] tether
  - [ ] limit movement distance from tether
  - [ ] attach to tether on right click
    - [ ] detach from previous tether when already attached to one
  - [ ] keybind to detach from tether
  - [ ] keybind to pull yourself towards tether
- [ ] antigravity chamber multiblock
  - [ ] turns on zero gravity inside when powered
- [ ] gravity generator
- [ ] some sort of power source for gravity manipulating equipment

## World
- [ ] space station dimension
  - [ ] zero gravity
    - [ ] maybe add a very weak pull towards the planet
      - [ ] respect no gravity tag
  - [ ] vacuum
  - [ ] space skybox
    - [ ] planet the station is orbiting
      - [ ] day/night cycle for planet texture
    - [ ] rotate skybox and planet as the station orbits the planet
  - [ ] sky light based on planet's sun
    - [ ] planet occluding the sun
    - [ ] maybe sky light direction based on sun direction (would be easiest with shaders)
