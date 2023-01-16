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
import kotlin.random.Random

object spritePositionInAreaSerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return SpritePositionInAreaFactory(selectSprite(), selectSprite("Выберите область:"))
  }

  override fun factoryFromNode(node: Node): ActionFactory {
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
  override fun fullText(): String = "Переместить$caption в область $area"

  override fun toNode(node: Node) {
    node.setField("area", area)
  }
}

class SpritePositionInArea(sprite: Sprite, private var area: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX = area.leftX + if(area.width > 0.0) Random.nextDouble(area.width) else 0.0
    sprite.centerY = area.topY + if(area.height > 0.0) Random.nextDouble(area.height) else 0.0
  }

  override fun toString(): String = "Переместить $sprite в область $area"

  override fun toNode(node: Node) {
    node.setField("area", area)
    node.setField("sprite", sprite)
  }
}