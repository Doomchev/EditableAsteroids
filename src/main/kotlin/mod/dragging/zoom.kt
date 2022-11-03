package mod.dragging

import Action
import currentCanvas
import mod.dragging.zoomIn.modifyZoom
import panel

var zoom = -21
var zk = 1.2

object zoomIn: Action {
  override fun execute(x: Int, y: Int) {
    modifyZoom(-1, x, y)
  }

  internal fun modifyZoom(dZoom: Int, x: Int, y: Int) {
    val pos = panel.mousePosition
    zoom += dZoom
    currentCanvas.setZoom(zoom, x, y)
  }
}

object zoomOut: Action {
  override fun execute(x: Int, y: Int) {
    modifyZoom(1, x, y)
  }
}