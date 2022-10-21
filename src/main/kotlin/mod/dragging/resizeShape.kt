package mod.dragging

import Shape
import xFromScreen
import xToScreen
import yFromScreen
import yToScreen
import java.awt.Graphics2D
import java.awt.event.MouseEvent.BUTTON1
import java.util.*
import kotlin.math.sign

enum class BlockType {nothing, horizontal, vertical, both}

class ResizerBlock(var x: Int, var y: Int, var type: BlockType)

object resizeShape: StartingPosition(), Drawing {
  var currentShape: Shape? = null
  var currentBlock: ResizerBlock? = null
  private const val cursorSize = 5
  private const val cursorDistance = 2
  private val blocks = Array(9) {
    ResizerBlock(-100, -100, BlockType.nothing)
  }

  override fun conditions(x: Int, y: Int, button: Int): Boolean {
    if(selectedShapes.size != 1 || button != BUTTON1) return false
    currentShape = selectedShapes.first
    currentBlock = underCursor(x, y)
    return currentBlock != null
  }

  private var mdx = 0.0
  private var mdy = 0.0

  private var leftX = 0.0
  private var topY = 0.0
  private var rightX = 0.0
  private var bottomY = 0.0

  override fun mousePressed(x: Int, y: Int, button: Int) {
    if(currentBlock == null) return

    mdx = sign(currentBlock!!.x - xToScreen(currentShape!!.centerX))
    mdy = sign(currentBlock!!.y - yToScreen(currentShape!!.centerY))

    leftX = currentShape!!.leftX
    topY = currentShape!!.topY
    rightX = currentShape!!.rightX
    bottomY = currentShape!!.bottomY

    super.mousePressed(x, y, button)
  }

  override fun mouseDragged(x: Int, y: Int) {
    if(currentBlock == null) return

    val dx = xFromScreen(x) - startingX
    val dy = yFromScreen(y) - startingY

    when(currentBlock!!.type) {
      BlockType.nothing -> {}
      BlockType.horizontal -> resizeHorizontally(dx)
      BlockType.vertical -> resizeVertically(dy)
      BlockType.both -> {
        resizeHorizontally(dx)
        resizeVertically(dy)
      }
    }
  }

  private fun resizeHorizontally(dx: Double) {
    if(mdx < 0) {
      //Editor.Grid.SnapWidth(DX, LeftX, RightX)
      currentShape!!.width = rightX - leftX - dx
      currentShape!!.centerX = rightX - currentShape!!.halfWidth
    } else {
      //Editor.Grid.SnapWidth(DX, RightX, LeftX)
      currentShape!!.width = rightX - leftX + dx
      currentShape!!.centerX = leftX + currentShape!!.halfWidth
    }
  }

  private fun resizeVertically(dy: Double) {
    if(mdy < 0) {
      //Editor.Grid.SnapHeight(DY, TopY, BottomY)
      currentShape!!.height = bottomY - topY - dy
      currentShape!!.centerY = bottomY - currentShape!!.halfHeight
    } else {
      //Editor.Grid.SnapHeight(DY, BottomY, TopY)
      currentShape!!.height = bottomY - topY + dy
      currentShape!!.centerY = currentShape!!.halfHeight
    }
  }

  override fun mouseReleased(x: Int, y: Int) {
    currentBlock = null
  }

  override fun drawWhileDragging(g2d: Graphics2D) {
    draw(g2d)
  }

  override fun draw(g2d: Graphics2D) {
    if(selectedShapes.size != 1) return
    setBlocks(selectedShapes.first)
    for(block in blocks) {
      g2d.fillRect(block.x, block.y, cursorSize, cursorSize)
    }
  }

  private fun setBlocks(shape: Shape) {
    for(yy in 0 .. 2) {
      val y = when(yy) {
        0 -> yToScreen(shape.topY).toInt() - cursorDistance - resizeShape.cursorSize
        1 -> yToScreen(shape.centerY).toInt() - cursorSize / 2
        else -> yToScreen(shape.bottomY).toInt() + cursorDistance
      }
      for(xx in 0 .. 2) {
        if(xx == 1 && yy == 1) continue
        val x = when(xx) {
          0 -> xToScreen(shape.leftX).toInt() - cursorDistance - resizeShape.cursorSize
          1 -> xToScreen(shape.centerX).toInt() - cursorSize / 2
          else -> xToScreen(shape.rightX).toInt() + cursorDistance
        }.toInt()
        val block = blocks[xx + yy * 3]
        block.x = x
        block.y = y
        block.type = BlockType.both
        if(yy == 1) block.type = BlockType.horizontal
        if(xx == 1) block.type = BlockType.vertical
      }
    }
  }

  private fun underCursor(x: Int, y: Int): ResizerBlock? {
    for(block in blocks) {
      if(x >= block.x && x < block.x + cursorSize
        && y >= block.y && y < block.y + cursorSize) return block
    }
    return null
  }
}
