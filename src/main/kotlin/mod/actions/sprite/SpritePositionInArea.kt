package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import Serializer
import mod.selectedSprites
import kotlin.random.Random

object spritePositionInAreaSerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SpritePositionInAreaFactory(selectedSprites.first)
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SpritePositionInAreaFactory(node.getField("area") as Sprite)
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SpritePositionInArea(node.getField("sprite") as Sprite, node.getField("area") as Sprite)
  }

  override fun toString(): String = "Переместить в область"
}

class SpritePositionInAreaFactory(private var area: Sprite): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SpritePositionInArea(sprite, area)
  }

  override fun toString(): String = "Переместить в область"
  override fun fullText(): String = "Переместить в область $area"

  override fun toNode(node: Node) {
    node.setField("area", area)
  }
}

class SpritePositionInArea(sprite: Sprite, private var area: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX = area.leftX + Random.nextDouble(area.width)
    sprite.centerY = area.topY + Random.nextDouble(area.height)
  }

  override fun toString(): String = "Переместить в область $area"

  override fun toNode(node: Node) {
    node.setField("area", area)
    node.setField("sprite", sprite)
  }
}