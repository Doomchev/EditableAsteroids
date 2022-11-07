package mod.dragging

import Sprite
import mod.actions.sprite.currentImageArray
import snapX
import snapY

object createSprite: createRectangle() {
  override fun pressed(x: Double, y: Double) {
    startingX = snapX(x)
    startingY = snapY(y)
    sprite = Sprite(x, y, 0.0, 0.0)
    sprite!!.image = if(selectedSprites.size > 0) selectedSprites.first.image else currentImageArray!!.images[0]
    sprites.add(sprite!!)
    selectedSprites.clear()
    selectedSprites.add(sprite!!)
  }

  override fun dragged(x: Double, y: Double) {
    dragged(x, y, true)
  }

  override fun released(x: Double, y: Double) {
    if(sprite!!.width == 0.0 && sprite!!.height == 0.0) {
      sprites.remove(sprite)
      selectedSprites.clear()
    }
  }
}