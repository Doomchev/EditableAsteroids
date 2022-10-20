import java.awt.Graphics2D
import java.util.*

open class Block(val element: Element, var style: Style = containerStyle) {
  var x: Int = 0
  var y: Int = 0
  var width: Int = 0
  var height: Int = 0
  var children = LinkedList<Block>()
  open fun draw(g: Graphics2D, dx: Int, dy: Int) {
    style.draw(g, x + dx, y + dy, width, height)
  }
}

class Label(element: Element, var text: String, x: Int = 0, y: Int = 0
            , width: Int = 0, height:Int = 0, style: Style = containerStyle
): Block(element, style) {
  override fun draw(g: Graphics2D, dx: Int, dy: Int) {
    style.draw(g, x + dx, y + dy, width, height)
    g.drawString(text, x + dx, y + dy)
  }
}