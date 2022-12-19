package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import Serializer
import mod.dragging.parentSprite

object spritePositionAsSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpritePositionAsFactory()
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpritePositionAsFactory()
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpritePositionAs(node.getField("sprite") as Sprite)
  }

  override fun toString(): String = "Переместить к родителю"
}

class SpritePositionAsFactory: SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpritePositionAs(sprite)
  }

  override fun toString(): String = "Переместить к родителю"

  override fun toNode(node: Node) {
  }
}

class SpritePositionAs(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX = parentSprite.centerX
    sprite.centerY = parentSprite.centerY
  }

  override fun toString(): String = "Переместить к родителю"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
  }
}

