package mod.dragging

import DraggingAction
import shapeUnderCursor
import xFromScreen
import yFromScreen
import java.awt.Graphics2D
import java.awt.event.MouseEvent

object moveSprites: DraggingAction {
  var oldx: Double = 0.0
  var oldy: Double = 0.0

  override fun conditions(x: Int, y: Int, button: Int): Boolean {
    if(button != MouseEvent.BUTTON1) return false
    if(shapeUnderCursor(selectedShapes, x, y) != null) return true
    selectedShapes.clear()
    val shape = shapeUnderCursor(shapes, x, y)
    if(shape != null) {
      selectedShapes.add(shape)
      return true
    }
    return false
  }

  override fun mousePressed(x: Int, y: Int, button: Int) {
    oldx = xFromScreen(x)
    oldy = yFromScreen(y)
  }

  override fun mouseDragged(x: Int, y: Int) {
    val fx = xFromScreen(x)
    val fy = yFromScreen(y)
    val dx = fx - oldx
    val dy = fy - oldy
    for(shape in selectedShapes) {
      shape.leftX += dx
      shape.topY += dy
    }
    oldx = fx
    oldy = fy
  }

  override fun mouseReleased(x: Int, y: Int) {
  }

  override fun drawWhileDragging(g2d: Graphics2D) {
  }
}