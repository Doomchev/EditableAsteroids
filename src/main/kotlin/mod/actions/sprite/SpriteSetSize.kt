package mod.actions.sprite

import Formula
import Node
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

  override fun toString(): String = "Установить размер"
  override fun fullText(): String = "Изменить размер на $size"

  override fun getClassName(): String = "SpriteSetSizeFactory"

  override fun store(node: Node) {
    node.setFormula("size", size)
  }
}

class SpriteSetSize(sprite: Sprite, val size: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.width = size
    sprite.height = size
  }

  override fun toString(): String = "Изменить размер на $size"

  override fun getClassName(): String = "SpriteSetSize"

  override fun store(node: Node) {
    node.setObject("sprite", sprite)
    node.setDouble("size", size)
  }
}