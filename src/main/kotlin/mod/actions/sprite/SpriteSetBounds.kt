package mod.actions.sprite

import Sprite
import SpriteAction
import SpriteFactory
import currentCanvas
import mod.dragging.enterDouble
import mod.dragging.scene
import mod.dragging.selectedSprites
import nullSprite

class SpriteSetBoundsFactory(private val bounds: Sprite = nullSprite): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SpriteSetBoundsFactory(selectedSprites.first)
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SpriteSetBounds(sprite, bounds)
  }

  override fun toString(): String = "Установить границы"
}

class SpriteSetBounds(sprite: Sprite, var bounds: Sprite = nullSprite): SpriteAction(sprite) {
  override fun execute() {
    if(sprite.centerX >= bounds.leftX && sprite.centerX < bounds.rightX && sprite.centerY >= bounds.topY && sprite.centerY < bounds.bottomY) return
    scene.remove(sprite)
  }
}