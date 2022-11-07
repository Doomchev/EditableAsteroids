package mod.actions.sprite

import Sprite
import fpsk
import mod.dragging.SpriteAction
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
    val length = sprite!!.movingVector.length
    if(limit > 0 && length > limit) {
      sprite!!.movingVector.x *= limit / length
      sprite!!.movingVector.y *= limit / length
    } else {
      sprite!!.movingVector.x += fpsk * acceleration * cos(sprite!!.angle)
      sprite!!.movingVector.y += fpsk * acceleration * sin(sprite!!.angle)
    }
  }
}