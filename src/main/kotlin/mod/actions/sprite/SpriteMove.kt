package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import fpsk
import mod.Serializer

object spriteMoveSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpriteMoveFactory()
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpriteMoveFactory()
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpriteMove(node.getField("sprite") as Sprite)
  }

}

class SpriteMoveFactory: SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpriteMove(sprite)
  }

  override fun fullText(): String = "Перемещать"

  override fun toNode(node: Node) {
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
}