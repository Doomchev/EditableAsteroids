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
import fpsk
import mod.dragging.enterDouble
import selectSprite
import spritesToRemove

object spriteDelayedRemoveSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return SpriteDelayedRemoveFactory(selectSprite(), enterDouble("Введите задержку:"))
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return SpriteDelayedRemoveFactory(node.getField("spriteentry") as SpriteEntry, node.getFormula("delay"))
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteDelayedRemove(node.getField("sprite") as Sprite, node.getDouble("delay"))
  }

  override fun toString(): String = "Удалить позже"
}

class SpriteDelayedRemoveFactory(spriteEntry: SpriteEntry, private var delay: Formula): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteDelayedRemove(spriteEntry.resolve(), delay.getDouble())
  }

  override fun toString(): String = "Удалить позже"
  override fun fullText(): String = "Удалить$caption через $delay сек."

  override fun toNode(node: Node) {
    node.setFormula("delay", delay)
  }
}

class SpriteDelayedRemove(sprite: Sprite, private var delay: Double): SpriteAction(sprite) {
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