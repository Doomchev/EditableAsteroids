package mod.actions.sprite

import Formula
import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import mod.dragging.RandomDoubleValue
import mod.dragging.enterDouble
import zero
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SpriteAccelerationFactory(private val acceleration: Formula = zero, private val limit: Formula = zero):
  SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteAccelerationFactory(
      enterDouble("Введите ускорение (ед/сек):")
      , enterDouble("Введите макс. скорость (ед/сек):"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteAcceleration(sprite, acceleration.get(), limit.get())
  }

  override fun toString(): String = "Ускорять на $acceleration до $limit"
}

class SpriteAcceleration(sprite: Sprite, private val acceleration: Double, private val limit: Double): SpriteAction(sprite) {
  override fun execute() {
    var newLength = vectorLength(sprite.dx, sprite.dy) + acceleration * fpsk
    if(newLength < 0) newLength = 0.0
    if(limit > 0 && newLength > limit) newLength = limit
    sprite.dx = newLength * cos(sprite.angle)
    sprite.dy = newLength * sin(sprite.angle)
  }

  override fun toString(): String = "Ускорять на $acceleration до $limit"
}

private fun vectorLength(dx: Double, dy: Double): Double {
  return sqrt(dx * dx + dy * dy)
}