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

object spriteActivateSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return SpriteDeactivateFactory(selectSprite())
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return SpriteActivateFactory(node.getField("spriteentry") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteActivate(node.getField("sprite") as Sprite)
  }

  override fun toString(): String = "Активировать"
}

class SpriteActivateFactory(spriteEntry: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteActivate(spriteEntry.resolve())
  }

  override fun toString(): String = "Активировать$caption"

  override fun toNode(node: Node) {
  }
}

class SpriteActivate(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.active = true
  }

  override fun toString(): String = "Активировать $sprite"
}