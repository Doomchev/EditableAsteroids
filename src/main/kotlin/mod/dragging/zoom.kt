package mod.dragging

import Action
import currentCanvas
import mod.dragging.zoomIn.modifyZoom
import mousefx
import mousefy
import mousesx
import mousesy
import xToScreen
import yToScreen

var zoom = -21
var zk = 1.2

object zoomIn: Action {
  override fun execute() {
    modifyZoom(-1)
  }

  internal fun modifyZoom(dZoom: Int) {
    zoom += dZoom
    currentCanvas.setZoom(zoom, mousesx, mousesy)
  }
}

object zoomOut: Action {
  override fun execute() {
    modifyZoom(1)
  }
}