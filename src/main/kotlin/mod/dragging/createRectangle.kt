package mod.dragging

import Sprite
import currentCanvas
import currentDraggingCanvas
import snapX
import snapY
import java.awt.Graphics2D
import kotlin.math.abs
import kotlin.math.min

abstract class createRectangle: StartingPosition(), Drawing {
  var sprite: Sprite? = null

  fun dragged(x: Double, y: Double, snapToGrid: Boolean) {
    sprite!!.width = snapX(abs(startingX - x), snapToGrid)
    sprite!!.height = snapY(abs(startingY - y), snapToGrid)
    sprite!!.leftX = snapX(min(startingX, x), snapToGrid)
    sprite!!.topY = snapY(min(startingY, y), snapToGrid)
  }

  override fun drawWhileDragging(g: Graphics2D) {
    if(sprite == null) return
    currentCanvas = currentDraggingCanvas!!
    sprite!!.drawSelection(g)
  }

  override fun draw(g: Graphics2D) {
  }
}