package mod.dragging

import DraggingAction
import shapeUnderCursor
import snapX
import snapY
import java.awt.Graphics
import java.awt.Graphics2D

object moveSprites: DraggingAction {
  var oldx: Double = 0.0
  var oldy: Double = 0.0

  override fun conditions(x: Double, y: Double): Boolean {
    if(shapeUnderCursor(selectedShapes, x, y) != null) return true
    selectedShapes.clear()
    val shape = shapeUnderCursor(shapes, x, y)
    if(shape != null) {
      selectedShapes.add(shape)
      return true
    }
    return false
  }

  override fun pressed(x: Double, y: Double) {
    oldx = snapX(x)
    oldy = snapY(y)
  }

  override fun dragged(x: Double, y: Double) {
    val fx = snapX(x)
    val fy = snapY(y)
    val dx = fx - oldx
    val dy = fy - oldy
    for(shape in selectedShapes) {
      shape.leftX += dx
      shape.topY += dy
    }
    oldx = fx
    oldy = fy
  }

  override fun released(x: Double, y: Double) {
  }

  override fun drawWhileDragging(g: Graphics2D) {
  }
}