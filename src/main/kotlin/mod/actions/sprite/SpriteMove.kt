package mod.actions.sprite

import Node
import Sprite
import Action
import SpriteActionFactory
import fpsk
import Serializer
import SpriteAction
import mod.dragging.SpriteEntry
import mod.dragging.selectSprite

object spriteMoveSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteMoveFactory(selectSprite())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteMoveFactory(node.getField("spriteentry") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteMove(node.getField("sprite") as Sprite)
  }

  override fun toString(): String = "Перемещать"
}

class SpriteMoveFactory(spriteEntry: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteMove(spriteEntry.resolve())
  }

  override fun toString(): String = "Перемещать $spriteEntry"

  override fun toNode(node: Node) {
  }
}

class SpriteMove(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX += fpsk * sprite.dx
    sprite.centerY += fpsk * sprite.dy
  }

  override fun toString(): String = "Перемещать $sprite"
}