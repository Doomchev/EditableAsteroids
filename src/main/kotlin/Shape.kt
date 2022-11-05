import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.util.*

private val whiteStroke = BasicStroke(1f, BasicStroke.CAP_BUTT
  , BasicStroke.JOIN_ROUND,1.0f, floatArrayOf(2f, 0f, 0f),2f)

open class Shape(var centerX: Double, var centerY: Double, var halfWidth: Double
                 , var halfHeight: Double) {
  var angle: Double = 0.0
  var image: BufferedImage? = null

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

  open fun draw(g: Graphics2D) {
    val oldTransform = g.transform
    g.rotate(angle, xToScreen(centerX).toDouble(), yToScreen(centerY).toDouble())
    g.drawImage(image, xToScreen(leftX), yToScreen(topY)
      , distToScreen(width), distToScreen(height), null)
    g.transform = oldTransform
  }

  fun drawSelection(g: Graphics2D) {
    drawDashedRectangle(g, leftX, topY, width, height)
  }

  fun collidesWithPoint(x: Double, y: Double): Boolean {
    return x >= leftX && x < rightX && y >= topY && y < bottomY
  }

  fun overlaps(shape: Shape): Boolean {
    return shape.leftX >= leftX && shape.topY >= topY
        && shape.rightX < rightX && shape.bottomY < bottomY
  }
}

fun shapeUnderCursor(shapes: LinkedList<Shape>, x: Double, y: Double): Shape? {
  for(shape in shapes.descendingIterator()) {
    if(shape.collidesWithPoint(x, y)) return shape
  }
  return null
}

fun drawDashedRectangle(g: Graphics2D, fx: Double, fy: Double
                        , fwidth: Double, fheight: Double) {
  val width = distToScreen(fwidth)
  val height = distToScreen(fheight)
  val x = xToScreen(fx)
  val y = yToScreen(fy)

  val phase = (Date().time % 1000) / 125f
  val dash = floatArrayOf(4f)

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