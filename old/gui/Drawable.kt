import java.awt.Graphics2D

interface Drawable {
  fun draw(g: Graphics2D, x: Int, y: Int, width: Int, height: Int)
}

object drawNothing: Drawable {
  override fun draw(g: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
  }
}

object drawRectangle: Drawable {
  override fun draw(g: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
    g.drawRect(x, y, width, height)
  }
}