package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import mod.dragging.selectedSprites
import nullSprite
import kotlin.random.Random

class SpritePositionInAreaFactory(private val area: Sprite = nullSprite): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpritePositionInAreaFactory(selectedSprites.first)
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpritePositionInArea(sprite, area)
  }

  override fun toString(): String = "Переместить в область"
}

class SpritePositionInArea(sprite: Sprite, private val area: Sprite): SpriteAction(sprite) {
  override fun execute() {
    sprite.centerX = area.leftX + Random.nextDouble(area.width)
    sprite.centerY = area.topY + Random.nextDouble(area.height)
  }
}