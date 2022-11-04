package mod.dragging

import Action
import currentCanvas
import mod.dragging.zoomIn.modifyZoom
import panel
import xToScreen
import yToScreen

var zoom = -21
var zk = 1.2

object zoomIn: Action {
  override fun execute(x: Double, y: Double) {
    modifyZoom(-1, x, y)
  }

  internal fun modifyZoom(dZoom: Int, x: Double, y: Double) {
    val pos = panel.mousePosition
    zoom += dZoom
    currentCanvas.setZoom(zoom, xToScreen(x).toInt(), yToScreen(y).toInt())
  }
}

object zoomOut: Action {
  override fun execute(x: Double, y: Double) {
    modifyZoom(1, x, y)
  }
}