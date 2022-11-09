package mod.dragging

import DraggingAction
import mousefx
import mousefy
import shapeUnderCursor
import snapX
import snapY
import java.awt.Graphics2D

object moveSprites: DraggingAction {
  var oldx: Double = 0.0
  var oldy: Double = 0.0

  override fun conditions(): Boolean {
    if(shapeUnderCursor(selectedSprites, mousefx, mousefy) != null) {
      return true
    }
    selectedSprites.clear()
    val shape = shapeUnderCursor(sprites, mousefx, mousefy)
    if(shape != null) {
      selectedSprites.add(shape)
      return true
    }
    return false
  }

  override fun pressed() {
    oldx = snapX(mousefx)
    oldy = snapY(mousefy)
  }

  override fun dragged() {
    val fx = snapX(mousefx)
    val fy = snapY(mousefy)
    val dx = fx - oldx
    val dy = fy - oldy
    for(shape in selectedSprites) {
      shape.leftX += dx
      shape.topY += dy
    }
    oldx = fx
    oldy = fy
  }

  override fun released() {
  }

  override fun drawWhileDragging(g: Graphics2D) {
  }
}