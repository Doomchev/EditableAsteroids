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
  fun draw(g: Graphics, image: BufferedImage) {
    g.drawImage(image, xToScreen(leftX).toInt(), yToScreen(topY).toInt()
      , distToScreen(width).toInt(), distToScreen(height).toInt(), null)
  }

  fun drawSelection(g: Graphics2D) {
    val x = xToScreen(leftX).toInt()
    val y = yToScreen(topY).toInt()
    val width = distToScreen(width).toInt()
    val height = distToScreen(height).toInt()

    val phase = (Date().time % 1000) / 125f
    val dash = floatArrayOf(4f)

    g.color = Color.BLACK
    g.setStroke(BasicStroke(1f, BasicStroke.CAP_BUTT
      , BasicStroke.JOIN_ROUND,1.0f, dash, phase))
    g.drawRect(x, y, width, height)

    g.color = Color.WHITE
    g.setStroke(BasicStroke(1f, BasicStroke.CAP_BUTT
      , BasicStroke.JOIN_ROUND,1.0f, dash, 4f + phase))
    g.drawRect(x, y, width, height)
  }

  var width: Double
    get() = halfWidth * 2.0
    set(value) {
      halfWidth = value * 0.5
    }
  var height: Double
    get() = halfHeight * 2.0
    set(value) {
      halfHeight = value * 0.5
    }
  var leftX: Double
    get() = centerX - halfWidth
    set(value) {
      centerX = value + halfWidth
    }
  var topY: Double
    get() = centerY - halfHeight
    set(value) {
      centerY = value + halfHeight
    }
  var rightX: Double
    get() = centerX + halfWidth
    set(value) {
      centerX = value - halfWidth
    }
  var bottomY: Double
    get() = centerY + halfHeight
    set(value) {
      centerY = value - halfHeight
    }
}
