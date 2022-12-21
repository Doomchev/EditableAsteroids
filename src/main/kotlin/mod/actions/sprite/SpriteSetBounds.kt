package mod.actions.sprite

import Node
import Sprite
import Action
import SpriteActionFactory
import Serializer
import SpriteAction
import SpriteEntry
import mod.dragging.selectSprite
import nullSprite
import spritesToRemove

object spriteSetBoundsSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteSetBoundsFactory(selectSprite(), selectSprite("Выберите область:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteSetBoundsFactory(node.getField("spriteentry") as SpriteEntry, node.getField("bounds") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteSetBounds(node.getField("sprite") as Sprite, node.getField("bounds") as Sprite)
  }

  override fun toString(): String = "Установить границы"
}

class SpriteSetBoundsFactory(spriteEntry: SpriteEntry, private var bounds: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteSetBounds(spriteEntry.resolve(), bounds.resolve())
  }

  override fun toString(): String = "Установить границы"
  override fun fullText(): String = "Установить границы $spriteEntry в $bounds"

  override fun toNode(node: Node) {
    node.setField("bounds", bounds)
  }
}

class SpriteSetBounds(sprite: Sprite, private var bounds: Sprite = nullSprite): SpriteAction(sprite) {
  override fun execute() {
    if(sprite.rightX < bounds.leftX || sprite.leftX > bounds.rightX || sprite.bottomY < bounds.topY || sprite.topY > bounds.bottomY) {
      spritesToRemove.add(sprite)
    }
  }

  override fun toString(): String = "Установить границы $sprite в $bounds"

  override fun toNode(node: Node) {
    node.setField("sprite", sprite)
    node.setField("bounds", bounds)
  }
}