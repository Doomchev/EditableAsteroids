import java.awt.BasicStroke
import java.awt.Graphics2D
import java.util.*

private val whiteStroke = BasicStroke(1f, BasicStroke.CAP_BUTT
  , BasicStroke.JOIN_ROUND,1.0f, floatArrayOf(2f, 0f, 0f),2f)

var fps = 100.0
var fpsk = 1.0 / fps

val nullSprite = Sprite()

open class Sprite(centerX: Double = 0.0, centerY: Double = 0.0, width:  Double = 1.0, height: Double = 1.0): Shape(centerX, centerY, width, height) {
  var angle: Double = 0.0
  var image: Image? = null
  var dx: Double = 0.0
  var dy: Double = 0.0

  fun copy(): Sprite {
    val sprite = Sprite()
    sprite.centerX = centerX
    sprite.centerY = centerY
    sprite.width = width
    sprite.height = height
    return sprite
  }

  override fun select(selection: Sprite, selected: LinkedList<Sprite>) {
    if(selection.overlaps(this)) selected.add(this)
  }

  override fun remove(shape: Shape) {
  }

  override fun spriteUnderCursor(fx: Double, fy: Double): Sprite? {
    return if(collidesWithPoint(fx, fy)) this else null
  }

  override fun draw(g: Graphics2D) {
    if(image == blankImage) {
      drawDashedRectangle(g, leftX, topY, width, height, 1f)
    } else {
      image?.draw(g, xToScreen(leftX), yToScreen(topY), distToScreen(width),
        distToScreen(height), angle, false)
    }
  }

  fun move() {
    centerX += fpsk * dx
    centerY += fpsk * dy
  }
}

fun spriteUnderCursor(sprites: LinkedList<Sprite>, x: Double, y: Double): Sprite? {
  for(sprite in sprites.descendingIterator()) {
    if(sprite.collidesWithPoint(x, y)) return sprite
  }
  return null
}