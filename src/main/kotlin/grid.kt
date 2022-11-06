import mod.dragging.Drawing
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.round

object grid: Drawing {
  var cellWidth = 1.0
  var cellHeight = 1.0
  var xDivider = 2.0
  var yDivider = 2.0
  var angleDivider = PI * 15 / 180

  override fun draw(g: Graphics2D) {
    g.color = Color.MAGENTA

    val viewport = currentCanvas.viewport
    val leftX = viewport.leftX
    val rightX = viewport.rightX
    val topY = viewport.topY
    val bottomY = viewport.bottomY

    val startingX = xFromScreen(leftX)
    val endingX = xFromScreen(rightX)

    var x = floor(startingX / cellWidth) * cellWidth
    while(x < endingX) {
      val fx = xToScreen(x)
      g.drawLine(fx, topY, fx, bottomY)
      x += cellWidth
    }

    val startingY = yFromScreen(topY)
    val endingY = yFromScreen(bottomY)

    var y = floor(startingY / cellHeight) * cellHeight
    while(y < endingY) {
      val fy = yToScreen(y)
      g.drawLine(leftX, fy, rightX, fy)
      y += cellHeight
    }

    g.color = Color.BLACK
  }
}

private fun snap(x: Double, step: Double) = round(x / step) * step
fun snapX(x: Double): Double = snap(x, grid.cellWidth / grid.xDivider)
fun snapY(y: Double): Double = snap(y, grid.cellHeight / grid.yDivider)
fun snapX(x: Double, snap: Boolean): Double = if(snap) snapX(x) else x
fun snapY(y: Double, snap: Boolean): Double = if(snap) snapY(y) else y
fun snapAngle(angle: Double) = snap(angle, grid.angleDivider)
