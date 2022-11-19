import java.awt.Color
 import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Image(var texture: BufferedImage, var x: Int, var y: Int, var width: Int
, var height: Int) {

   var xMul: Double = 0.5 // 43.0 / 48.0
   var yMul: Double = 0.5 // 5.5 / 12.0
   var widthMul: Double = 1.0 // 13.5
   var heightMul: Double = 1.0 // 3.0
  constructor(texture: BufferedImage) : this(texture, 0, 0, texture.width
    , texture.height)

  fun draw(g: Graphics2D, sx: Int, sy: Int, swidth: Int, sheight: Int,
           angle: Double, icon: Boolean) {
    val oldTransform = g.transform

    val newWidth = (swidth * if(icon) 1.0 else widthMul).toInt()
    val newHeight = (sheight * if(icon) 1.0 else heightMul).toInt()
    val newX = sx + swidth / 2 - (newWidth * if(icon) 0.5 else xMul).toInt()
    val newY = sy + sheight / 2 - (newHeight * if(icon) 0.5 else yMul).toInt()

    if(!icon) g.rotate(angle, sx + 0.5 * swidth, sy + 0.5 * sheight)
    g.drawImage(texture, newX, newY, newX + newWidth, newY + newHeight, x, y, x + width, y + height, null)
    if(showCollisionShapes && !icon) {
      g.color = Color(255, 0, 255, 127)
      g.fillOval(sx, sy, swidth, sheight)
      g.color = Color.black
    }
    g.transform = oldTransform
  }
}

class ImageArray(var images: Array<Image>) {
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
}