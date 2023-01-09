import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

enum class HorizontalAlign{left, center, right}
enum class VerticalAlign{top, center, bottom}

val defaultFont = Font("Dialog", Font.PLAIN, 16)

class Label(private var variable: Formula, centerX: Double, centerY: Double, width: Double, height: Double, private var horizontalAlign: HorizontalAlign = HorizontalAlign.center, var verticalAlign: VerticalAlign = VerticalAlign.center, var prefix: String = "", zeros: Boolean = true): Sprite(blankImage, centerX, centerY, width, height) {
  override fun draw(g: Graphics2D) {
    g.color = Color.white
    g.font = defaultFont

    val text = prefix + variable.toString()

    val metrics = g.getFontMetrics(defaultFont)
    val stringWidth = metrics.stringWidth(text)

    val xx = when(horizontalAlign) {
      HorizontalAlign.left -> xToScreen(leftX).toFloat()
      HorizontalAlign.center -> xToScreen(centerX).toFloat() - 0.5f * stringWidth
      HorizontalAlign.right -> xToScreen(rightX).toFloat() - stringWidth
    }
    val yy = yToScreen(centerY).toFloat() + 0.5f * metrics.height

    g.drawString(text, xx, yy)
    //g.drawRect((xx - 2).toInt(), (yy - 2).toInt(), 5, 5)
    //drawSelection(g)
  }
}