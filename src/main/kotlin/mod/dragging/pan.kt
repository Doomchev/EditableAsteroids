package mod.dragging

import DraggingAction
import currentCanvas
import mousefx
import mousefy

object pan: DraggingAction {
  var startingX = 0.0
  var startingY = 0.0
  var startingCanvasX = 0.0
  var startingCanvasY = 0.0

  override fun pressed() {
    startingX = mousefx
    startingY = mousefy
    startingCanvasX = currentCanvas.centerX
    startingCanvasY = currentCanvas.centerY
  }

  override fun dragged() {
    currentCanvas.centerX = startingCanvasX
    currentCanvas.centerY = startingCanvasY
    currentCanvas.update()

    currentCanvas.centerX = startingCanvasX + startingX - mousefx
    currentCanvas.centerY = startingCanvasY + startingY - mousefy
    currentCanvas.update()
  }
}