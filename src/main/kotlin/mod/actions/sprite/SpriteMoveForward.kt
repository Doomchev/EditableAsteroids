package mod.actions.sprite

import Action
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import fpsk
import selectSprite

object spriteMoveForwardSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteMoveForwardFactory(selectSprite())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteMoveForwardFactory(node.getField("spriteentry") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteMoveForward(node.getField("sprite") as Sprite)
  }

  override fun toString(): String = "Перемещать"
}

class SpriteMoveForwardFactory(spriteEntry: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteMoveForward(spriteEntry.resolve())
  }

  override fun toString(): String = "Перемещать$caption"

  override fun toNode(node: Node) {
  }
}

class SpriteMoveForward(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    if(!sprite.active) return
    sprite.centerX += fpsk * sprite.dx
    sprite.centerY += fpsk * sprite.dy
  }

  override fun toString(): String = "Перемещать $sprite"
}