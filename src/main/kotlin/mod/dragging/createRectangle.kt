package mod.dragging

import DraggingAction
import Shape
import snapX
import snapY
import xFromScreen
import yFromScreen
import java.awt.Graphics2D
import kotlin.math.abs
import kotlin.math.min

abstract class createRectangle: StartingPosition(), Drawing {
  var shape: Shape? = null

  fun dragged(x: Int, y: Int, snapToGrid: Boolean) {
    var fx = xFromScreen(x)
    var fy = yFromScreen(y)
    shape!!.width = snapX(abs(startingX - fx), snapToGrid)
    shape!!.height = snapY(abs(startingY - fy), snapToGrid)
    shape!!.leftX = snapX(min(startingX, fx), snapToGrid)
    shape!!.topY = snapY(min(startingY, fy), snapToGrid)
  }

  override fun drawWhileDragging(g2d: Graphics2D) {
    if(shape != null) {
      shape!!.drawSelection(g2d)
    }
  }

  override fun draw(g2d: Graphics2D) {
  }
}