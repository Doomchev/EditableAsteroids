package mod.actions.sprite

import Action
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import selectSprite

object spriteShowSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteShowFactory(selectSprite())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteShowFactory(node.getField("spriteentry") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteShow(node.getField("sprite") as Sprite)
  }

  override fun toString(): String = "Показать"
}

class SpriteShowFactory(spriteEntry: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteShow(spriteEntry.resolve())
  }

  override fun toString(): String = "Показать$caption"

  override fun toNode(node: Node) {
  }
}

class SpriteShow(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.visible = true
  }

  override fun toString(): String = "Показать $sprite"

  override fun toNode(node: Node) {
  }
}

