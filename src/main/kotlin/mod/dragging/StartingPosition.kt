package mod.dragging

import DraggingAction
import xFromScreen
import yFromScreen

abstract class StartingPosition: DraggingAction {
  var startingX:Double = 0.0
  var startingY:Double = 0.0

  override fun mousePressed(x: Int, y: Int, button: Int) {
    startingX = xFromScreen(x)
    startingY = yFromScreen(y)
  }
}