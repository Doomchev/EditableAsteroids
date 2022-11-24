package mod.actions.sprite

import Formula
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.RandomDoubleValue
import mod.dragging.enterDouble
import zero

class SpriteSetMovingVectorFactory(private val dx: Formula = zero, private val dy: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetMovingVectorFactory(enterDouble("Введите приращение по Х:"
    ), enterDouble("Введите приращение по Y:"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetMovingVector(sprite, dx.get(), dy.get())
  }

  override fun toString(): String = "Задать движение ($dx, $dy)"
}

class SpriteSetMovingVector(sprite: Sprite, private val dx: Double, private val dy: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.dx += dx
    sprite.dy += dy
  }

  override fun toString(): String = "Задать движение ($dx, $dy)"
}