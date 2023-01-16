package mod.actions.sprite

import Action
import Node
import Serializer
import Sprite
import SpriteAction
import ActionFactory
import SpriteActionFactory
import SpriteEntry
import selectSprite

object spriteHideSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return SpriteHideFactory(selectSprite())
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return SpriteHideFactory(node.getField("spriteentry") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteHide(node.getField("sprite") as Sprite)
  }

  override fun toString(): String = "Спрятать"
}

class SpriteHideFactory(spriteEntry: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteHide(spriteEntry.resolve())
  }

  override fun toString(): String = "Спрятать$caption"

  override fun toNode(node: Node) {
  }
}

class SpriteHide(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.visible = false
  }

  override fun toString(): String = "Спрятать $sprite"

  override fun toNode(node: Node) {
  }
}

