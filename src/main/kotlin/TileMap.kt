import java.awt.Graphics2D
import java.util.*

class TileMap(private var columns: Int, private var rows: Int, private var cellWidth: Double = 1.0, private var cellHeight: Double = 1.0, private var tileSet: ImageArray): Shape() {
  private var map: IntArray
  var maxValue: Int = 0

  init {
    map = IntArray(rows * columns)
    update()
  }

  fun update() {
    width = cellWidth * columns
    height = cellHeight * rows
    maxValue = 0
    for(element in map) {
      if(element > maxValue) maxValue = element
    }
  }

  override fun select(selection: Sprite, selected: LinkedList<Sprite>) {}

  override fun remove(shape: Shape) {}

  override fun spriteUnderCursor(fx: Double, fy: Double): Sprite? = null

  override fun draw(g: Graphics2D) {
    val swidth = distToScreen(cellWidth) + 1
    val sheight = distToScreen(cellHeight) + 1
    for(y in 0 until rows) {
      for(x in 0 until columns) {
        tileSet.images[map[x + y * columns]].draw(g, xToScreen(leftX + cellWidth * x), yToScreen(topY + cellHeight * y), swidth, sheight)
      }
    }
  }
}