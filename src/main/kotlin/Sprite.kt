import mod.dragging.SceneElement
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.util.*
import kotlin.math.sqrt

private val whiteStroke = BasicStroke(1f, BasicStroke.CAP_BUTT
  , BasicStroke.JOIN_ROUND,1.0f, floatArrayOf(2f, 0f, 0f),2f)

var fps = 100.0
var fpsk = 1.0 / fps

class Vector(var x: Double, var y: Double) {
  val length get() = sqrt(x * x + y * y)
}

open class Sprite(): SceneElement() {
  var centerX: Double = 0.0
  var centerY: Double = 0.0
  var halfWidth: Double = 0.0
  var halfHeight: Double = 0.0
  var angle: Double = 0.0
  var image: Image? = null
  var movingVector = Vector(0.0, 0.0)

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

  constructor(centerX: Double, centerY: Double, width: Double, height: Double) : this() {
    this.centerX = centerX
    this.centerY = centerY
    this.width = width
    this.height = height
  }

  override fun select(selection: Sprite, selected: LinkedList<Sprite>) {
    if(selection.overlaps(this)) selected.add(this)
  }

  override fun remove(sprite: Sprite) {
  }

  override fun spriteUnderCursor(fx: Double, fy: Double): Sprite? {
    return if(collidesWithPoint(fx, fy)) this else null
  }

  override fun draw(g: Graphics2D) {
    image?.draw(g, xToScreen(leftX), yToScreen(topY), distToScreen(width), distToScreen(height), angle)
  }

  fun drawSelection(g: Graphics2D) {
    drawDashedRectangle(g, leftX, topY, width, height)
  }

  fun move() {
    centerX += fpsk * movingVector.x
    centerY += fpsk * movingVector.y
  }

  fun collidesWithPoint(x: Double, y: Double): Boolean {
    return x >= leftX && x < rightX && y >= topY && y < bottomY
  }

  fun overlaps(sprite: Sprite): Boolean {
    return sprite.leftX >= leftX && sprite.topY >= topY
        && sprite.rightX < rightX && sprite.bottomY < bottomY
  }
}

fun spriteUnderCursor(sprites: LinkedList<Sprite>, x: Double, y: Double): Sprite? {
  for(sprite in sprites.descendingIterator()) {
    if(sprite.collidesWithPoint(x, y)) return sprite
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