import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.util.*

private val whiteStroke = BasicStroke(1f, BasicStroke.CAP_BUTT
  , BasicStroke.JOIN_ROUND,1.0f, floatArrayOf(2f, 0f, 0f),2f)

open class Shape(var centerX: Double, var centerY: Double, var halfWidth: Double
                 , var halfHeight: Double) {

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

  fun draw(g: Graphics, image: BufferedImage) {
    g.drawImage(image, xToScreen(leftX).toInt(), yToScreen(topY).toInt()
      , distToScreen(width).toInt(), distToScreen(height).toInt(), null)
  }

  fun drawSelection(g: Graphics2D) {
    drawDashedRectangle(g, leftX, topY, width, height)
  }

  private fun drawDashedRectangle(g: Graphics2D, fx: Double, fy: Double
                                  , fwidth: Double, fheight: Double) {
    val width = distToScreen(fwidth).toInt()
    val height = distToScreen(fheight).toInt()
    val x = xToScreen(fx).toInt()
    val y = yToScreen(fy).toInt()

    val phase = (Date().time % 1000) / 125f
    val dash = floatArrayOf(4f)

    g.color = Color.BLACK
    g.stroke = BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND
      ,1.0f, dash, phase)
    g.drawRect(x, y, width, height)

    g.color = Color.WHITE
    g.stroke = BasicStroke(1f, BasicStroke.CAP_BUTT
      , BasicStroke.JOIN_ROUND,1.0f, dash, 4f + phase)
    g.drawRect(x, y, width, height)
  }

  fun collidesWithPoint(x: Double, y: Double): Boolean {
    return x >= leftX && x < rightX && y >= topY && y < bottomY
  }

  fun overlaps(shape: Shape): Boolean {
    return shape.leftX >= leftX && shape.topY >= topY
        && shape.rightX < rightX && shape.bottomY < bottomY
  }
}
