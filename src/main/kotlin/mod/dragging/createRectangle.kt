package mod.dragging

import Shape
import currentCanvas
import currentDraggingCanvas
import snapX
import snapY
import java.awt.Graphics
import java.awt.Graphics2D
import kotlin.math.abs
import kotlin.math.min

abstract class createRectangle: StartingPosition(), Drawing {
  var shape: Shape? = null

  fun dragged(x: Double, y: Double, snapToGrid: Boolean) {
    shape!!.width = snapX(abs(startingX - x), snapToGrid)
    shape!!.height = snapY(abs(startingY - y), snapToGrid)
    shape!!.leftX = snapX(min(startingX, x), snapToGrid)
    shape!!.topY = snapY(min(startingY, y), snapToGrid)
  }

  override fun drawWhileDragging(g: Graphics2D) {
    if(shape == null) return
    currentCanvas = currentDraggingCanvas!!
    shape!!.drawSelection(g)
  }

  override fun draw(g: Graphics2D) {
  }
}