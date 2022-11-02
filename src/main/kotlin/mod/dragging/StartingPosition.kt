package mod.dragging

import DraggingAction
import Pushable
import snapX
import snapY
import xFromScreen
import yFromScreen

abstract class StartingPosition: DraggingAction {
  var startingX:Double = 0.0
  var startingY:Double = 0.0

  fun pressed(x: Int, y: Int, snapToGrid: Boolean) {
    startingX = snapX(xFromScreen(x), snapToGrid)
    startingY = snapY(yFromScreen(y), snapToGrid)
  }
}