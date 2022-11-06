package mod.dragging

import DraggingAction
import Shape
import distFromScreen
import snapAngle
import xToScreen
import yToScreen
import java.awt.Graphics2D
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

object rotateSprite: DraggingAction, Drawing {
  val cursorSize = 8
  val block = Shape(0.0, 0.0, 0.0, 0.0)
  var currentShape: Shape? = null

  override fun conditions(x: Double, y: Double): Boolean {
    if(selectedShapes.size != 1) return false
    currentShape = selectedShapes.first
    return block.collidesWithPoint(x, y)
  }

  override fun pressed(x: Double, y: Double) {
  }

  override fun dragged(x: Double, y: Double) {
    if(currentShape == null) return
    currentShape!!.angle = snapAngle(atan2(currentShape!!.centerY - y, currentShape!!.centerX - x))
  }

  override fun released(x: Double, y: Double) {
  }

  override fun drawWhileDragging(g: Graphics2D) {
    draw(g)
  }

  override fun draw(g: Graphics2D) {
    if(selectedShapes.size != 1) return
    setBlock(selectedShapes.first)
    g.drawOval(xToScreen(block.leftX), yToScreen(block.topY), cursorSize, cursorSize)
  }

  private fun setBlock(shape: Shape) {
    val size = 0.5 * (shape.halfWidth + shape.halfHeight) * 1.41 + distFromScreen(16)
    val csize = distFromScreen(cursorSize)
    block.centerX = shape.centerX - size * cos(shape.angle)
    block.centerY = shape.centerY - size * sin(shape.angle)
    block.width = csize
    block.height = csize
  }
}