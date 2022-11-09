package mod.dragging

import Sprite
import java.awt.Graphics2D

object selectSprites: createRectangle(), Drawing {
  private val selection = Sprite()

  override fun pressed() {
    sprite = selection
    selection.width = 0.0
    selection.height = 0.0
    pressed(false)
  }

  override fun dragged() {
    dragged(false)
  }

  override fun released() {
    for(shape in sprites) {
      if(selection.overlaps(shape)) selectedSprites.add(shape)
    }
  }

  override fun draw(g: Graphics2D) {
    for(shape in selectedSprites) {
      shape.drawSelection(g)
    }
  }
}