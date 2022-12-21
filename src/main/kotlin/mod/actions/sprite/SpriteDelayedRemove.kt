package mod.actions.sprite

import Formula
import Node
import Sprite
import Action
import SpriteActionFactory
import fpsk
import Serializer
import SpriteAction
import mod.dragging.SpriteEntry
import mod.dragging.enterDouble
import mod.dragging.selectSprite
import spritesToRemove

object spriteDelayedRemoveSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteDelayedRemoveFactory(selectSprite(), enterDouble("Введите задержку:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteDelayedRemoveFactory(node.getField("spriteentry") as SpriteEntry, node.getFormula("delay"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteDelayedRemove(node.getField("sprite") as Sprite, node.getDouble("delay"))
  }

  override fun toString(): String = "Удалить позже"
}

class SpriteDelayedRemoveFactory(spriteEntry: SpriteEntry, private var delay: Formula): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteDelayedRemove(spriteEntry.resolve(), delay.get())
  }

  override fun toString(): String = "Удалить позже"
  override fun fullText(): String = "Удалить через $delay сек."

  override fun toNode(node: Node) {
    node.setFormula("delay", delay)
  }
}

class SpriteDelayedRemove(sprite: Sprite, private var delay: Double = 0.0): SpriteAction(sprite) {
  override fun execute() {
    delay -= fpsk
    if(delay <= 0) {
      spritesToRemove.add(sprite)
    }
  }

  override fun toString(): String = "Удалить через $delay сек."

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("delay", delay)
  }
}