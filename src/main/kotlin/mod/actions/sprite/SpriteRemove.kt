package mod.actions.sprite

import Node
import Sprite
import Action
import SpriteActionFactory
import Serializer
import SpriteAction
import SpriteEntry
import mod.dragging.selectSprite
import spritesToRemove

object spriteRemoveSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteRemoveFactory(selectSprite())
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteRemoveFactory(node.getField("spriteentry") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteDelayedRemove(node.getField("sprite") as Sprite)
  }

  override fun toString(): String = "Удалить"
}

class SpriteRemoveFactory(spriteEntry: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteRemove(spriteEntry.resolve())
  }

  override fun toString(): String = "Удалить $spriteEntry"

  override fun toNode(node: Node) {
  }
}

class SpriteRemove(sprite: Sprite): SpriteAction(sprite) {
  override fun execute() {
    spritesToRemove.add(sprite)
  }

  override fun toString(): String = "Удалить $sprite"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
  }
}