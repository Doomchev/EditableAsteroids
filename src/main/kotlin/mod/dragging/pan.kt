package mod.dragging

import DraggingAction
import currentCanvas
import xFromScreen
import yFromScreen
import java.awt.Graphics2D

object pan: DraggingAction {
  var startingX = 0.0
  var startingY = 0.0
  var startingCanvasX = 0.0
  var startingCanvasY = 0.0

  override fun pressed(x: Int, y: Int) {
    startingX = xFromScreen(x)
    startingY = yFromScreen(y)
    startingCanvasX = currentCanvas.centerX
    startingCanvasY = currentCanvas.centerY
  }

  override fun dragged(x: Int, y: Int) {
    currentCanvas.centerX = startingCanvasX
    currentCanvas.centerY = startingCanvasY
    currentCanvas.update()

    val xx = startingCanvasX + xFromScreen(x) - startingX
    val yy = startingCanvasY + yFromScreen(y) - startingY

    currentCanvas.centerX = xx
    currentCanvas.centerY = yy
    currentCanvas.update()
  }

  override fun released(x: Int, y: Int) {
  }

  override fun drawWhileDragging(g2d: Graphics2D) {
  }
}