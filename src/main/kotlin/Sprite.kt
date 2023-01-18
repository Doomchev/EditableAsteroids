import mod.*
import state.nullState
import java.awt.Graphics2D
import javax.swing.JOptionPane
import kotlin.math.PI

var fps = 100.0
var fpsk = 1.0 / fps

val spritesToRemove = mutableListOf<Sprite>()
val nullSprite = Sprite(blankImage)

object spriteSerializer: ElementSerializer {
  override fun fromNode(node: Node): SceneElement {
    return Sprite(node.getField("image") as Image, node.getDouble("centerX"), node.getDouble("centerY"), node.getDouble("width"), node.getDouble("height"), node.getDouble("angle"), node.getDouble("dx"), node.getDouble("dy"))
  }
}

open class Sprite(var image: Image, centerX: Double = 0.0, centerY: Double = 0.0, width: Double = 1.0, height: Double = 1.0, angleInDegrees: Double = 0.0, var dx: Double = 1.0, var dy: Double = 0.0, var active: Boolean = true): Shape(centerX, centerY, width, height) {
  var state= nullState
  var angle = angleInDegrees * PI / 180.0
  var visible: Boolean = true

  override fun select(selection: Sprite, selected: MutableList<Sprite>) {
    if(selection.overlaps(this)) selected.add(this)
  }

  override fun remove(shape: Shape) {
  }

  override fun spriteUnderCursor(fx: Double, fy: Double): Sprite? {
    return if(collidesWithPoint(fx, fy)) this else null
  }

  override fun draw(g: Graphics2D) {
    if(!visible) return
    if(image == blankImage) {
      drawDashedRectangle(g, leftX, topY, width, height, 1f)
    } else {
      image.draw(g, xToScreen(leftX), yToScreen(topY), distToScreen(width), distToScreen(height), angle, false)
    }
  }

  fun move() {
    centerX += fpsk * dx
    centerY += fpsk * dy
  }

  override fun toNode(node: Node) {
    super.toNode(node)
    node.setDouble("angle", angle)
    node.setField("image", image)
    node.setDouble("dx", dx)
    node.setDouble("dy", dy)
  }

  fun setName(name: String): SpriteEntry {
    val entry = SpriteEntry(name, this)
    objectsList.add(entry)
    return entry
  }

  override fun toString(): String {
    for(entry in objectsList) {
      if(entry.sprite == this) return entry.caption
    }
    return super.toString() + image.texture.fileName
  }
}

fun spriteUnderCursor(sprites: MutableList<Sprite>, x: Double, y: Double): Sprite? {
  for(sprite in sprites.reversed()) {
    if(sprite.collidesWithPoint(x, y)) return sprite
  }
  return null
}

fun selectSprite(message:String = "Выберите спрайт:"): SpriteEntry {
  val options = Array(objectsList.size + 4) {
    when(it) {
      0 -> currentEntry
      1 -> parentEntry
      2 -> sprite1Entry
      3 -> sprite2Entry
      else -> objectsList[it - 4]
    }
  }
  return options[JOptionPane.showOptionDialog(frame, message,"", JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]
  )]
}

val objectsList = mutableListOf<SpriteEntry>()