package mod.actions.sprite

import Formula
import Node
import Sprite
import SpriteAction
import SpriteFactory
import Serializer
import mod.dragging.enterDouble
import zero

object spriteSetSizeSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteSetSizeFactory(enterDouble("Введите ширину/высоту:"))
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteSetSizeFactory(node.getFormula("size"))
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteSetSize(node.getField("sprite") as Sprite, node.getDouble("size"))
  }

  override fun toString(): String = "Установить размер"
}

class SpriteSetSizeFactory(var size: Formula = zero): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    val value = size.get()
    return SpriteSetSize(sprite, value)
  }

  override fun toString(): String = "Установить размер"
  override fun fullText(): String = "Изменить размер на $size"

  override fun toNode(node: Node) {
    node.setFormula("size", size)
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
}