package mod.actions.sprite

import Formula
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.RandomDoubleValue
import mod.dragging.enterDouble
import zero
import kotlin.math.PI

class SpriteSetAngleFactory(private val angle: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetAngleFactory(enterDouble("Введите скорость поворота (град/сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetAngle(sprite, angle.get() * PI / 180.0)
  }

  override fun toString(): String = "Задать угол"
  override fun fullText(): String = "Задать угол $angle"
}

class SpriteSetAngle(sprite: Sprite, private val angle: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = angle
  }

  override fun toString(): String = "Задать угол $angle"
}