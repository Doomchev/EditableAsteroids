package mod.actions.sprite

import Formula
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.enterDouble
import zero
import kotlin.math.cos
import kotlin.math.sin

class SpriteSetSizeFactory(val width: Formula = zero, val height: Formula = zero):
  SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetSizeFactory(
      enterDouble("Введите ширину:")
      , enterDouble("Введите высоту:"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetSize(sprite, width.get(), height.get())
  }

  override fun toString(): String = "Ускорять"
}

class SpriteSetSize(sprite: Sprite, val width: Double, val height: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.width = width
    sprite.height = height
  }
}