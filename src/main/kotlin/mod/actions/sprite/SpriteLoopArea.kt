package mod.actions.sprite

import Action
import Node
import Serializer
import Sprite
import SpriteAction
import SpriteActionFactory
import SpriteEntry
import selectSprite
import nullSprite

object spriteLoopAreaSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpriteLoopAreaFactory(
      selectSprite(),
      selectSprite("Выберите границу")
    )
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpriteLoopAreaFactory(node.getField("spriteentry") as SpriteEntry, node.getField("bounds") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpriteLoopArea(node.getField("sprite") as Sprite, node.getField("bounds") as Sprite)
  }

  override fun toString(): String = "Зациклить пространство"
}

class SpriteLoopAreaFactory(spriteEntry: SpriteEntry, private var bounds: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpriteLoopArea(spriteEntry.resolve(), bounds.resolve())
  }

  override fun toString(): String = "Зациклить пространство"
  override fun fullText(): String = "Зациклить пространство в $bounds$forCaption"

  override fun toNode(node: Node) {
    node.setField("bounds", bounds)
  }
}

class SpriteLoopArea(sprite: Sprite, private var bounds: Sprite = nullSprite): SpriteAction(sprite) {
  override fun execute() {
    if(sprite.centerX < bounds.leftX) sprite.centerX += bounds.width
    if(sprite.centerX >= bounds.rightX) sprite.centerX -= bounds.width
    if(sprite.centerY < bounds.topY) sprite.centerY += bounds.height
    if(sprite.centerY >= bounds.bottomY) sprite.centerY -= bounds.height
  }

  override fun toString(): String = "Зациклить пространство для $sprite в $bounds"

  override fun toNode(node: Node) {
    node.setField("bounds", bounds)
  }
}