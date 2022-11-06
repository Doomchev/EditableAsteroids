 import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Image(var texture: BufferedImage, var x: Int, var y: Int, var width: Int, var height: Int) {
  constructor(texture: BufferedImage) : this(texture, 0, 0, texture.width, texture.height) {
  }

  fun draw(g: Graphics2D, sx: Int, sy: Int, swidth: Int, sheight: Int, angle: Double) {
    val oldTransform = g.transform
    g.rotate(angle, sx + 0.5 * swidth, sy + 0.5 * sheight)
    g.drawImage(texture, sx, sy, sx + swidth, sy + sheight, x, y, x + width, y + height, null)
    g.transform = oldTransform
  }
}

class ImageArray(var images: Array<Image>)