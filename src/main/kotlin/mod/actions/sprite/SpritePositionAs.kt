package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.parentSprite

class SpritePositionAsFactory(): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpritePositionAsFactory()
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpritePositionAs(sprite,)
  }

  override fun fullText(): String = "Переместить к родителю"

  override fun toNode(node: Node) {
  }

  override fun fromNode(node: Node) {
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

  override fun fromNode(node: Node) {
    sprite = node.getField("sprite") as Sprite
  }
}

