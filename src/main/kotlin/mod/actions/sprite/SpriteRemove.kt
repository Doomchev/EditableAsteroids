package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import Serializer
import spritesToRemove

object spriteRemoveSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteRemoveFactory()
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteRemoveFactory()
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteDelayedRemove(node.getField("sprite") as Sprite)
  }
}

class SpriteRemoveFactory: SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteRemove(sprite)
  }

  override fun fullText(): String = "Удалить"

  override fun toNode(node: Node) {
  }
}

class SpriteRemove(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    spritesToRemove.add(sprite)
  }

  override fun toString(): String = "Удалить"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
  }
}