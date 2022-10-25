package mod.dragging

import Action
import canvas
import mod.dragging.zoomIn.modifyZoom
import panel

var zoom = -21
var zk = 1.2

object zoomIn: Action {
  override fun execute() {
    modifyZoom(-1)
  }

  internal fun modifyZoom(dZoom: Int) {
    val pos = panel.mousePosition
    zoom += dZoom
    canvas.setZoom(zoom, pos.x, pos.y)
  }
}

object zoomOut: Action {
  override fun execute() {
    modifyZoom(1)
  }
}