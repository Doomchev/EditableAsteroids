package mod.actions.sprite

import Formula
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.RandomDoubleValue
import mod.dragging.enterDouble
import zero

class SpriteSetSizeFactory(val size: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetSizeFactory(enterDouble("Введите ширину/высоту:"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    val value = size.get()
    return SpriteSetSize(sprite, value)
  }

  override fun toString(): String = "Изменить размер"
}

class SpriteSetSize(sprite: Sprite, val size: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.width = size
    sprite.height = size
  }
}