import mod.SceneElement
import java.awt.Graphics2D
import java.util.*

object tileMapSerializer: ElementSerializer {
  override fun fromNode(node: Node): SceneElement {
    return TileMap(node.getInt("columns"), node.getInt("rows"), node.getDouble("cellWidth"), node.getDouble("cellHeight"), node.getField("tileSet") as ImageArray)
  }
}

class TileMap(private var columns: Int, private var rows: Int, private var cellWidth: Double = 1.0, private var cellHeight: Double = 1.0, private var tileSet: ImageArray): Shape() {
  private var map: IntArray = IntArray(rows * columns)
  private var maxValue: Int = 0

  init {
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