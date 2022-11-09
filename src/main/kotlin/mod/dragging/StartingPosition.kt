package mod.dragging

import DraggingAction
import Pushable
import mousefx
import mousefy
import snapX
import snapY
import xFromScreen
import yFromScreen

abstract class StartingPosition: DraggingAction {
  var startingX:Double = 0.0
  var startingY:Double = 0.0

  fun pressed(snapToGrid: Boolean) {
    startingX = snapX(mousefx, snapToGrid)
    startingY = snapY(mousefy, snapToGrid)
  }
}