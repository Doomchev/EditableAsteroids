package mod.dragging

import Sprite
import java.awt.Graphics2D

object selectSprites: createRectangle(), Drawing {
  private val selection = Sprite(0.0, 0.0, 0.0, 0.0)

  override fun pressed(x: Double, y: Double) {
    sprite = selection
    selection.width = 0.0
    selection.height = 0.0
    pressed(x, y, false)
  }

  override fun dragged(x: Double, y: Double) {
    dragged(x, y, false)
  }

  override fun released(x: Double, y: Double) {
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