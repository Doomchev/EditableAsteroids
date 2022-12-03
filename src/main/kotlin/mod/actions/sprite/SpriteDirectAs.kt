package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.parentSprite

class SpriteDirectAsFactory(): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteDirectAsFactory()
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDirectAs(sprite)
  }

  override fun fullText(): String = "Направить как родителя"

  override fun toNode(node: Node) {
  }

  override fun fromNode(node: Node) {
  }
}
class SpriteDirectAs(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = parentSprite.angle
  }

  override fun toString(): String = "Направить как родителя"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
  }

  override fun fromNode(node: Node) {
    sprite = node.getField("sprite") as Sprite
  }
}