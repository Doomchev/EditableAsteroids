package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import fpsk

class SpriteMoveFactory: SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteMoveFactory()
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteMove(sprite)
  }

  override fun fullText(): String = "Перемещать"

  override fun toNode(node: Node) {
  }

  override fun fromNode(node: Node) {
  }
}
class SpriteMove(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX += fpsk * sprite.dx
    sprite.centerY += fpsk * sprite.dy
  }

  override fun toString(): String = "Перемещать"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
  }

  override fun fromNode(node: Node) {
    sprite = node.getField("sprite") as Sprite
  }
}