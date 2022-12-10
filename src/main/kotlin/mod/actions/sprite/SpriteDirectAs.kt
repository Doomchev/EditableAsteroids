package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import Serializer
import mod.dragging.parentSprite

object spriteDirectAsSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteDirectAsFactory()
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteDirectAsFactory()
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteDirectAs(node.getField("sprite") as Sprite)
  }
}

class SpriteDirectAsFactory(): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteDirectAs(sprite)
  }

  override fun fullText(): String = "Направить как родителя"

  override fun toNode(node: Node) {
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
}