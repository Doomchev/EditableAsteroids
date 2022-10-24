package mod.dragging

import DraggingAction
import canvas
import distFromScreen
import xFromScreen
import xToScreen
import yFromScreen
import yToScreen
import java.awt.Graphics2D

object pan: DraggingAction {
  var startingX = 0
  var startingY = 0
  var startingCanvasX = 0.0
  var startingCanvasY = 0.0

  override fun pressed(x: Int, y: Int) {
    startingX = x
    startingY = y
    startingCanvasX = canvas.centerX
    startingCanvasY = canvas.centerY
  }

  override fun dragged(x: Int, y: Int) {
    canvas.centerX = startingCanvasX
    canvas.centerY = startingCanvasY
    canvas.update()

    val xx = startingCanvasX + startingX - x
    val yy = startingCanvasY + startingY - y

    canvas.centerX = xx
    canvas.centerY = yy
    canvas.update()
  }

  override fun released(x: Int, y: Int) {
  }

  override fun drawWhileDragging(g2d: Graphics2D) {
  }
}