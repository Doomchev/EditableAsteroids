package mod.dragging

import DraggingAction
import currentCanvas
import xFromScreen
import xToScreen
import yFromScreen
import yToScreen
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.MouseInfo

object pan: DraggingAction {
  var startingX = 0.0
  var startingY = 0.0
  var startingCanvasX = 0.0
  var startingCanvasY = 0.0

  override fun pressed(x: Double, y: Double) {
    startingX = x
    startingY = y
    startingCanvasX = currentCanvas.centerX
    startingCanvasY = currentCanvas.centerY
  }

  override fun dragged(x: Double, y: Double) {
    currentCanvas.centerX = startingCanvasX
    currentCanvas.centerY = startingCanvasY
    currentCanvas.update()

    val point = MouseInfo.getPointerInfo().location
    val fx = xFromScreen(point.x)
    val fy = yFromScreen(point.y)

    currentCanvas.centerX = startingCanvasX + startingX - fx
    currentCanvas.centerY = startingCanvasY + startingY - fy
    currentCanvas.update()
  }

  override fun released(x: Double, y: Double) {
  }

  override fun drawWhileDragging(g: Graphics2D) {
  }
}