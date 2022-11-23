import mod.dragging.SceneElement
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.util.*

abstract class Shape: SceneElement() {
  var centerX: Double = 0.0
  var centerY: Double = 0.0
  var halfWidth: Double = 0.0
  var halfHeight: Double = 0.0

  var width: Double
    inline get() = halfWidth * 2.0
    inline set(value) {
      halfWidth = value * 0.5
    }
  var height: Double
    inline get() = halfHeight * 2.0
    inline set(value) {
      halfHeight = value * 0.5
    }
  var leftX: Double
    inline get() = centerX - halfWidth
    inline set(value) {
      centerX = value + halfWidth
    }
  var topY: Double
    inline get() = centerY - halfHeight
    inline set(value) {
      centerY = value + halfHeight
    }
  var rightX: Double
    inline get() = centerX + halfWidth
    inline set(value) {
      centerX = value - halfWidth
    }
  var bottomY: Double
    inline get() = centerY + halfHeight
    inline set(value) {
      centerY = value - halfHeight
    }

  fun collidesWithPoint(x: Double, y: Double): Boolean {
    return x >= leftX && x < rightX && y >= topY && y < bottomY
  }

  fun overlaps(shape: Shape): Boolean {
    return shape.leftX >= leftX && shape.topY >= topY
        && shape.rightX < rightX && shape.bottomY < bottomY
  }

  fun drawSelection(g: Graphics2D) {
    drawDashedRectangle(g, leftX, topY, width, height, 4f)
  }
}

fun drawDashedRectangle(g: Graphics2D, fx: Double, fy: Double
                        , fwidth: Double, fheight: Double, stroke: Float) {
  val width = distToScreen(fwidth)
  val height = distToScreen(fheight)
  val x = xToScreen(fx)
  val y = yToScreen(fy)

  val phase = (Date().time % 1000) / 125f
  val dash = floatArrayOf(stroke)

  g.stroke = BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND
    ,1.0f, dash, phase)
  g.drawRect(x, y, width, height)

  g.color = Color.WHITE
  g.stroke = BasicStroke(1f, BasicStroke.CAP_BUTT
    , BasicStroke.JOIN_ROUND,1.0f, dash, 4f + phase)
  g.drawRect(x, y, width, height)

  g.stroke = BasicStroke()
  g.color = Color.BLACK
}