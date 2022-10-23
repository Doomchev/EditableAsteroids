package mod.dragging

import DraggingAction
import Shape
import xFromScreen
import yFromScreen
import java.awt.Graphics2D
import kotlin.math.abs
import kotlin.math.min

abstract class createRectangle: StartingPosition(), Drawing {
  var shape: Shape? = null

  override fun dragged(x: Int, y: Int) {
    val fx = xFromScreen(x)
    val fy = yFromScreen(y)
    shape!!.width = abs(startingX - fx)
    shape!!.height = abs(startingY - fy)
    shape!!.leftX = min(startingX, fx)
    shape!!.topY = min(startingY, fy)
  }

  override fun drawWhileDragging(g2d: Graphics2D) {
    if(shape != null) {
      shape!!.drawSelection(g2d)
    }
  }

  override fun draw(g2d: Graphics2D) {
  }
}