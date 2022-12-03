package mod.actions.sprite

import Node
import Sprite
import SpriteAction
import SpriteFactory
import mod.selectedSprites
import nullSprite
import kotlin.random.Random

class SpritePositionInAreaFactory(private var area: Sprite = nullSprite): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpritePositionInAreaFactory(selectedSprites.first)
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpritePositionInArea(sprite, area)
  }

  override fun toString(): String = "Переместить в область"
  override fun fullText(): String = "Переместить в область $area"

  override fun toNode(node: Node) {
    node.setField("area", area)
  }

  override fun fromNode(node: Node) {
    area = node.getField("area") as Sprite
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

  override fun fromNode(node: Node) {
    sprite = node.getField("sprite") as Sprite
    area = node.getField("area") as Sprite
  }
}