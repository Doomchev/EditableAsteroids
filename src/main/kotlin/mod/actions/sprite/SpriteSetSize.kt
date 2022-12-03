package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.enterDouble
import zero

class SpriteSetSizeFactory(var size: Formula = zero): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetSizeFactory(enterDouble("Введите ширину/высоту:"))
  }

  override fun create(sprite: Sprite): SpriteAction {
    val value = size.get()
    return SpriteSetSize(sprite, value)
  }

  override fun toString(): String = "Установить размер"
  override fun fullText(): String = "Изменить размер на $size"

  override fun toNode(node: Node) {
    node.setFormula("size", size)
  }

  override fun fromNode(node: Node) {
    size = node.getFormula("size")
  }
}

class SpriteSetSize(sprite: Sprite, var size: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.width = size
    sprite.height = size
  }

  override fun toString(): String = "Изменить размер на $size"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("size", size)
  }

  override fun fromNode(node: Node) {
    sprite = node.getField("sprite") as Sprite
    size = node.getDouble("size")
  }
}