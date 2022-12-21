package mod.actions.sprite

import Node
import Sprite
import Action
import SpriteActionFactory
import Serializer
import SpriteAction
import SpriteEntry
import mod.dragging.selectSprite
import kotlin.random.Random

object spritePositionInAreaSerializer: Serializer {
  override fun newFactory(): SpriteActionFactory {
    return SpritePositionInAreaFactory(selectSprite(), selectSprite("Выберите область:"))
  }

  override fun factoryFromNode(node: Node): SpriteActionFactory {
    return SpritePositionInAreaFactory(node.getField("spriteentry") as SpriteEntry, node.getField("area") as SpriteEntry)
  }

  override fun actionFromNode(node: Node): Action {
    return SpritePositionInArea(node.getField("sprite") as Sprite, node.getField("area") as Sprite)
  }

  override fun toString(): String = "Переместить в область"
}

class SpritePositionInAreaFactory(spriteEntry: SpriteEntry, private var area: SpriteEntry): SpriteActionFactory(spriteEntry) {
  override fun create(): SpriteAction {
    return SpritePositionInArea(spriteEntry.resolve(), area.resolve())
  }

  override fun toString(): String = "Переместить в область"
  override fun fullText(): String = "Переместить $spriteEntry в область $area"

  override fun toNode(node: Node) {
    node.setField("area", area)
  }
}

class SpritePositionInArea(sprite: Sprite, private var area: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX = area.leftX + Random.nextDouble(area.width)
    sprite.centerY = area.topY + Random.nextDouble(area.height)
  }

  override fun toString(): String = "Переместить $sprite в область $area"

  override fun toNode(node: Node) {
    node.setField("area", area)
    node.setField("sprite", sprite)
  }
}