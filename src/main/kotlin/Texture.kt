import mod.Element
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO

object textureSerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    return Texture(node.getString("fileName"))
  }
}

class Texture(val fileName: String): Element {
  val image: BufferedImage = if(fileName.isEmpty()) BufferedImage(1, 1, TYPE_INT_RGB) else ImageIO.read(File(fileName))

  override fun toNode(node: Node) {
    node.setString("fileName", fileName)
  }
}