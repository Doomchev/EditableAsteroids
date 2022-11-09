package mod.dragging

import Sprite
import mousefx
import mousefy
import snapX
import snapY
import java.awt.Graphics2D
import kotlin.math.abs
import kotlin.math.min

abstract class createRectangle: StartingPosition(), Drawing {
  var sprite: Sprite? = null

  fun dragged(snapToGrid: Boolean) {
    sprite!!.width = snapX(abs(startingX - mousefx), snapToGrid)
    sprite!!.height = snapY(abs(startingY - mousefy), snapToGrid)
    sprite!!.leftX = snapX(min(startingX, mousefx), snapToGrid)
    sprite!!.topY = snapY(min(startingY, mousefy), snapToGrid)
  }

  override fun drawWhileDragging(g: Graphics2D) {
    if(sprite == null) return
    sprite!!.drawSelection(g)
  }

  override fun draw(g: Graphics2D) {
  }
}