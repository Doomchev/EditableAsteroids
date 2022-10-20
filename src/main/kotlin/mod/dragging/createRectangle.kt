package mod.dragging

import DraggingAction
import Shape
import xFromScreen
import yFromScreen
import java.awt.Graphics2D
import kotlin.math.abs
import kotlin.math.min

abstract class createRectangle: DraggingAction() {
  var shape: Shape? = null
  var startingX:Double = 0.0
  var startingY:Double = 0.0

  override fun mousePressed(x: Int, y: Int, button: Int) {
    startingX = xFromScreen(x)
    startingY = yFromScreen(y)
  }

  override fun mouseDragged(x: Int, y: Int) {
    val fx = xFromScreen(x)
    val fy = yFromScreen(y)
    shape!!.width = abs(startingX - fx)
    shape!!.height = abs(startingY - fy)
    shape!!.leftX = min(startingX, fx)
    shape!!.topY = min(startingY, fy)
  }

  override fun draw(g2d: Graphics2D) {
    if(shape != null) {
      shape!!.drawSelection(g2d)
    }
  }
}