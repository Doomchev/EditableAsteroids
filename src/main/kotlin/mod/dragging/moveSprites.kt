package mod.dragging

import DraggingAction
import Pushable
import shapeUnderCursor
import snapX
import snapY
import xFromScreen
import yFromScreen
import java.awt.Graphics2D
import java.awt.event.MouseEvent

object moveSprites: DraggingAction {
  var oldx: Double = 0.0
  var oldy: Double = 0.0

  override fun conditions(x: Int, y: Int): Boolean {
    if(shapeUnderCursor(selectedShapes, x, y) != null) return true
    selectedShapes.clear()
    val shape = shapeUnderCursor(shapes, x, y)
    if(shape != null) {
      selectedShapes.add(shape)
      return true
    }
    return false
  }

  override fun pressed(x: Int, y: Int) {
    oldx = snapX(xFromScreen(x))
    oldy = snapY(yFromScreen(y))
  }

  override fun dragged(x: Int, y: Int) {
    val fx = snapX(xFromScreen(x))
    val fy = snapY(yFromScreen(y))
    val dx = fx - oldx
    val dy = fy - oldy
    for(shape in selectedShapes) {
      shape.leftX += dx
      shape.topY += dy
    }
    oldx = fx
    oldy = fy
  }

  override fun released(x: Int, y: Int) {
  }

  override fun drawWhileDragging(g2d: Graphics2D) {
  }
}