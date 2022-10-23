package mod.dragging

import DraggingAction
import Pushable
import xFromScreen
import yFromScreen

abstract class StartingPosition: DraggingAction {
  var startingX:Double = 0.0
  var startingY:Double = 0.0

  override fun pressed(x: Int, y: Int) {
    startingX = xFromScreen(x)
    startingY = yFromScreen(y)
  }
}