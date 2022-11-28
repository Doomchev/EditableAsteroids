package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import mod.dragging.enterDouble
import spritesToRemove


class SpriteDelayedRemoveFactory(private var delay: Double = 0.0): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteDelayedRemoveFactory(enterDouble("Введите задержку:").get())
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDelayedRemove(sprite, delay)
  }

  override fun toString(): String = "Удалить позже"
  override fun fullText(): String = "Удалить через $delay сек."

  override fun getClassName(): String = "SpriteDelayedRemoveFactory"

  override fun store(node: Node) {
    node.setDouble("delay", delay)
  }

  override fun load(node: Node) {
    delay = node.getDouble("delay")
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

  override fun getClassName(): String = "SpriteDelayedRemove"

  override fun store(node: Node) {
    node.setField("sprite", sprite)
    node.setDouble("delay", delay)
  }

  override fun load(node: Node) {
    sprite = node.getField("sprite") as Sprite
    delay = node.getDouble("delay")
  }
}