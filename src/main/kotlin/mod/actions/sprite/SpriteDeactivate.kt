package mod.actions.sprite

import Action
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import selectSprite

object spriteDeactivateSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteDeactivateFactory(selectSprite())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteDeactivateFactory(node.getField("spriteentry") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteDeactivate(node.getField("sprite") as Sprite)
  }

  override fun toString(): String = "Деактивировать"
}

class SpriteDeactivateFactory(spriteEntry: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteDeactivate(spriteEntry.resolve())
  }

  override fun toString(): String = "Деактивировать$caption"

  override fun toNode(node: Node) {
  }
}

class SpriteDeactivate(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.active = false
  }

  override fun toString(): String = "Деактивировать $sprite"
}