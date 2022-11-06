 import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Image(var texture: BufferedImage) {
  var x: Int = 0
  var y: Int = 0
  var width: Int = texture.width
  var height: Int = texture.height

  fun draw(g: Graphics2D, sx: Int, sy: Int, swidth: Int, sheight: Int, angle: Double) {
    val oldTransform = g.transform
    g.rotate(angle, sx + 0.5 * swidth, sy + 0.5 * sheight)
    g.drawImage(texture, sx, sy, sx + swidth, sy + sheight, x, y, x + width, y + height, null)
    g.transform = oldTransform
  }
}