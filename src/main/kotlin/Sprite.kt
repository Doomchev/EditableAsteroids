import mod.SceneElement
import mod.ElementSerializer
import mod.dragging.spritesList
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.util.*

private val whiteStroke = BasicStroke(1f, BasicStroke.CAP_BUTT
  , BasicStroke.JOIN_ROUND,1.0f, floatArrayOf(2f, 0f, 0f),2f)

var fps = 100.0
var fpsk = 1.0 / fps

val spritesToRemove = LinkedList<Sprite>()
val nullSprite = Sprite(blankImage)

object spriteSerializer: ElementSerializer {
  override fun fromNode(node: Node): SceneElement {
    return Sprite(node.getField("image") as Image, node.getDouble("canterX"), node.getDouble("centerY"), node.getDouble("width"), node.getDouble("height"), node.getString("name"), node.getDouble("angle"), node.getDouble("dx"), node.getDouble("dy"))
  }
}

open class Sprite(var image: Image, centerX: Double = 0.0, centerY: Double = 0.0, width: Double = 1.0, height: Double = 1.0, var name:String = "", var angle: Double = 0.0, var dx: Double = 0.0, var dy: Double = 0.0): Shape(centerX, centerY, width, height) {

  init {
    if(name.isNotEmpty()) spritesList.add(this)
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

  override fun toNode(node: Node) {
    super.toNode(node)
    node.setString("name", name)
    node.setDouble("angle", angle)
    if(image != null) node.setField("image", image!!)
    node.setDouble("dx", dx)
    node.setDouble("dy", dy)
  }

  override fun toString(): String {
    return name.ifEmpty { super.toString() }
  }
}

fun spriteUnderCursor(sprites: LinkedList<Sprite>, x: Double, y: Double): Sprite? {
  for(sprite in sprites.descendingIterator()) {
    if(sprite.collidesWithPoint(x, y)) return sprite
  }
  return null
}