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

  override fun getClassName(): String = "SpriteDirectAsFactory"

  override fun store(node: Node) {
  }

  override fun load(node: Node) {
  }
}
class SpriteDirectAs(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.angle = parentSprite.angle
  }

  override fun toString(): String = "Направить как родителя"

  override fun getClassName(): String = "SpriteDirectAs"

  override fun store(node: Node) {
    node.setField("sprite", sprite)
  }

  override fun load(node: Node) {
    sprite = node.getField("sprite") as Sprite
  }
}