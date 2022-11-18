 import java.awt.Color
 import java.awt.Graphics2D
import java.awt.image.BufferedImage

 class Image(var texture: BufferedImage, var x: Int, var y: Int, var width: Int
, var height: Int) {

   var xMul: Double = 0.5 // 43.0 / 48.0
   var yMul: Double = 0.5 // 5.5 / 12.0
   var widthMul: Double = 1.0 // 12.0
   var heightMul: Double = 1.0 // 3.0
  constructor(texture: BufferedImage) : this(texture, 0, 0, texture.width
    , texture.height)


  fun draw(g: Graphics2D, sx: Int, sy: Int, swidth: Int, sheight: Int, angle: Double) {
    val oldTransform = g.transform

    val newWidth = (swidth * widthMul).toInt()
    val newHeight = (sheight * heightMul).toInt()
    val newX = sx + swidth / 2 - (newWidth * xMul).toInt()
    val newY = sy + sheight / 2 - (newHeight * yMul).toInt()

    g.rotate(angle, sx + 0.5 * swidth, sy + 0.5 * sheight)
    g.drawImage(texture, newX, newY, newX + newWidth, newY + newHeight
      , x, y, x + width, y + height, null)
    if(showCollisionShapes) {
      g.color = Color(255, 0, 255, 127)
      g.fillOval(sx, sy, swidth, sheight)
      g.color = Color.black
    }
    g.transform = oldTransform
  }
}

class ImageArray(var images: Array<Image>)