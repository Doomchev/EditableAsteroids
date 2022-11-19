package mod.dragging

import Sprite
import currentCanvas
import mod.actions.sprite.currentImageArray
import mousefx
import mousefy
import snapX
import snapY
import world

object createSprite: createRectangle() {
  override fun pressed() {
    startingX = snapX(mousefx)
    startingY = snapY(mousefy)
    sprite = Sprite(mousefx, mousefy, 0.0, 0.0)
    sprite!!.image = if(selectedSprites.size > 0) selectedSprites.first.image else currentImageArray!!.images[0]
    scene.add(sprite!!)
    selectedSprites.clear()
    selectedSprites.add(sprite!!)
  }

  override fun dragged() {
    dragged(true)
  }

  override fun released() {
    if(sprite!!.width == 0.0 && sprite!!.height == 0.0) {
      scene.remove(sprite!!)
      selectedSprites.clear()
    }
  }

  override fun toString(): String {
    return ""
  }
}