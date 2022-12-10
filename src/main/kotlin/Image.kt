import mod.Element
import java.awt.Color
 import java.awt.Graphics2D
import java.util.*

object imageSerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    return Image(node.getField("texture") as Texture, node.getInt("x"), node.getInt("y"), node.getInt("width"), node.getInt("height"), node.getDouble("xMul"), node.getDouble("yMul"), node.getDouble("widthMul"), node.getDouble("heightMul"))
  }
}

class Image(var texture: Texture, var x: Int, var y: Int, var width: Int, var height: Int, var xMul: Double = 0.5, var yMul: Double = 0.5, var widthMul: Double = 1.0, var heightMul: Double = 1.0): Element {

  constructor(texture: Texture) : this(texture, 0, 0, texture.image.width, texture.image.height)

  fun draw(g: Graphics2D, sx: Int, sy: Int, swidth: Int, sheight: Int) {
    g.drawImage(texture.image, sx, sy, sx + swidth, sy + sheight, x, y, x + width, y + height, null)
  }

  fun draw(g: Graphics2D, sx: Int, sy: Int, swidth: Int, sheight: Int, angle: Double, icon: Boolean) {
    val oldTransform = g.transform

    val newWidth = (swidth * if(icon) 1.0 else widthMul).toInt()
    val newHeight = (sheight * if(icon) 1.0 else heightMul).toInt()
    val newX = sx + swidth / 2 - (newWidth * if(icon) 0.5 else xMul).toInt()
    val newY = sy + sheight / 2 - (newHeight * if(icon) 0.5 else yMul).toInt()

    if(!icon) g.rotate(angle, sx + 0.5 * swidth, sy + 0.5 * sheight)
    g.drawImage(texture.image, newX, newY, newX + newWidth, newY + newHeight, x, y, x + width, y + height, null)
    if(showCollisionShapes && !icon) {
      g.color = Color(255, 0, 255, 127)
      g.fillOval(sx, sy, swidth, sheight)
      g.color = Color.black
    }
    g.transform = oldTransform
  }

  override fun toNode(node: Node) {
    node.setField("texture", texture)
    node.setInt("x", x)
    node.setInt("y", y)
    node.setInt("width", width)
    node.setInt("height", height)
    node.setDouble("xMul", xMul)
    node.setDouble("yMul", yMul)
    node.setDouble("widthMul", widthMul)
    node.setDouble("heightMul", heightMul)
  }
}

object imageArraySerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    val list = LinkedList<Image>()
    node.getChildren(list)
    return ImageArray(Array(list.size) { list[it] }, node.getString("name"))
  }
}

class ImageArray(var images: Array<Image>, private val name: String): Element {
  fun setCenter(x: Double, y: Double) {
    for(image in images) {
      image.xMul = x// / image.width
      image.yMul = y// / image.height
    }
  }

  fun setVisibleArea(xk: Double, yk: Double) {
    for(image in images) {
      image.widthMul = xk
      image.heightMul = yk
    }
  }

  override fun toNode(node: Node) {
    node.setString("name", name)
    val list = LinkedList<Image>()
    for(image in images) {
      list.add(image)
    }
    node.setChildren(list)
  }

  override fun toString(): String {
    return name
  }
}