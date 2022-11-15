package mod.actions.sprite

import Sprite
import SpriteAction
import mod.dragging.enterDouble
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SpriteSetSpeed: SpriteAction() {
  var speed: Double = 0.0

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteSetSpeed()
    action.sprite = sprite
    action.speed = speed
    return action
  }

  override fun settings() {
    speed = enterDouble("Введите скорость поворота (град/сек):") * PI / 180.0
  }

  override fun execute() {
    sprite!!.movingVector.x = speed * cos(sprite!!.angle)
    sprite!!.movingVector.y = speed * sin(sprite!!.angle)
  }

  override fun toString(): String {
    return "Установить скорость"
  }
}