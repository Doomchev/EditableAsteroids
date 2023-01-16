package mod.actions.sprite

import Action
import Formula
import Node
import Serializer
import Sprite
import SpriteAction
import ActionFactory
import SpriteActionFactory
import SpriteEntry
import mod.dragging.enterDouble
import selectSprite
import zero

object spriteSetSizeSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return SpriteSetSizeFactory(selectSprite(), enterDouble("Введите ширину/высоту:"))
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return SpriteSetSizeFactory(node.getField("spriteentry") as SpriteEntry, node.getFormula("size"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteSetSize(node.getField("sprite") as Sprite, node.getDouble("size"))
  }

  override fun toString(): String = "Установить размер"
}

class SpriteSetSizeFactory(spriteEntry: SpriteEntry, var size: Formula = zero): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteSetSize(spriteEntry.resolve(), size.getDouble())
  }

  override fun toString(): String = "Установить размер"
  override fun fullText(): String = "Установить размер $size$forCaption"

  override fun toNode(node: Node) {
    node.setFormula("size", size)
  }
}

class SpriteSetSize(sprite: Sprite, var size: Double): SpriteAction(sprite) {
  override fun execute() {
    sprite.width = size
    sprite.height = size
  }

  override fun toString(): String = "Установить размер $size для $sprite"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("size", size)
  }
}