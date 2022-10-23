import mod.dragging.Drawing
import java.awt.Color
import java.awt.Graphics2D

object grid: Drawing {
  var cellWidth = 1.0
  var cellHeight = 1.0
  var xDivider = 2.0
  var yDivider = 2.0

  override fun draw(g2d: Graphics2D) {
    g2d.color = Color.MAGENTA

    g2d.color = Color.BLACK
  }


}