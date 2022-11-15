package mod.actions.sprite

import Sprite
import SpriteAction
import fpsk
import mod.dragging.enterDouble
import kotlin.math.PI

class SpriteRotation: SpriteAction() {
  var speed: Double = 0.0

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteRotation()
    action.sprite = sprite
    action.speed = speed
    return action
  }

  override fun settings() {
    speed = enterDouble("Введите скорость поворота (град/сек):") * PI / 180.0
  }

  override fun execute() {
    sprite!!.angle += fpsk * speed
  }

  override fun toString(): String = "Вращать"
}