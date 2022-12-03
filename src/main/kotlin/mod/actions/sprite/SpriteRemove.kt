package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import spritesToRemove

class SpriteRemoveFactory(): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteRemoveFactory()
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteRemove(sprite)
  }

  override fun fullText(): String = "Удалить"

  override fun toNode(node: Node) {
  }

  override fun fromNode(node: Node) {
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

  override fun fromNode(node: Node) {
    sprite = node.getField("sprite") as Sprite
  }
}