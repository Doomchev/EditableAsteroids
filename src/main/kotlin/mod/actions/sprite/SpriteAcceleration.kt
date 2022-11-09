package mod.actions.sprite

import Sprite
import SpriteAction
import fpsk
import mod.dragging.enterDouble
import kotlin.math.cos
import kotlin.math.sin

class SpriteAcceleration: SpriteAction() {
  var acceleration = 0.0
  var limit: Double = 0.0

  override fun create(sprite: Sprite): SpriteAction {
    val action = SpriteAcceleration()
    action.sprite = sprite
    action.acceleration = acceleration
    action.limit = limit
    return action
  }

  override fun settings() {
    acceleration = enterDouble("Введите ускорение (ед/сек):")
    limit = enterDouble("Введите макс. скорость (ед/сек):")
  }

  override fun execute() {
    var newLength = sprite!!.movingVector.length + acceleration * fpsk
    if(newLength < 0) newLength = 0.0
    if(limit > 0 && newLength > limit) newLength = limit
    sprite!!.movingVector.x = newLength * cos(sprite!!.angle)
    sprite!!.movingVector.y = newLength * sin(sprite!!.angle)
  }
}