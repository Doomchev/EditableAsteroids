package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import Serializer
import mod.dragging.enterDouble
import spritesToRemove

object spriteDelayedRemoveSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteDelayedRemoveFactory(enterDouble("Введите задержку:").get())
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteDelayedRemoveFactory(node.getDouble("delay"))
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteDelayedRemove(node.getField("sprite") as Sprite, node.getDouble("delay"))
  }
}

class SpriteDelayedRemoveFactory(private var delay: Double = 0.0): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDelayedRemove(sprite, delay)
  }

  override fun toString(): String = "Удалить позже"
  override fun fullText(): String = "Удалить через $delay сек."

  override fun toNode(node: Node) {
    node.setDouble("delay", delay)
  }
}

class SpriteDelayedRemove(sprite: Sprite, var delay: Double = 0.0): SpriteAction(sprite) {
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