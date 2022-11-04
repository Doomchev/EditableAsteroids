package mod.dragging

import Shape
import snapX
import snapY
import xToScreen
import yToScreen
import java.awt.Graphics
import java.awt.Graphics2D
import kotlin.math.abs

object resizeSprite: StartingPosition(), Drawing {
  class ResizerBlock(var x: Int, var y: Int, var type: BlockType)
  enum class BlockType {nothing, horizontal, vertical, both}

  var currentShape: Shape? = null
  var currentBlock: ResizerBlock? = null
  private const val cursorSize = 8
  private const val cursorDistance = 2
  private val blocks = Array(9) {
    ResizerBlock(0, 0, BlockType.nothing)
  }

  override fun conditions(x: Double, y: Double): Boolean {
    if(selectedShapes.size != 1) return false
    currentShape = selectedShapes.first
    currentBlock = underCursor(xToScreen(x).toInt(), yToScreen(y).toInt())
    return currentBlock != null
  }

  private var mdx = 0.0
  private var mdy = 0.0

  private var leftX = 0.0
  private var topY = 0.0
  private var rightX = 0.0
  private var bottomY = 0.0

  override fun pressed(x: Double, y: Double) {
    if(currentBlock == null) return

    mdx = currentBlock!!.x - xToScreen(currentShape!!.centerX)
    mdy = currentBlock!!.y - yToScreen(currentShape!!.centerY)

    leftX = snapX(currentShape!!.leftX)
    topY = snapY(currentShape!!.topY)
    rightX = snapX(currentShape!!.rightX)
    bottomY = snapY(currentShape!!.bottomY)

    pressed(x, y, true)
  }

  override fun dragged(x: Double, y: Double) {
    if(currentBlock == null) return

    val dx = x - startingX
    val dy = y - startingY

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
      val width = snapX(rightX - leftX - dx)
      currentShape!!.width = abs(width)
      if(width < 0) {
        currentShape!!.leftX = rightX
      } else {
        currentShape!!.rightX = rightX
      }
    } else {
      //Editor.Grid.SnapWidth(DX, RightX, LeftX)
      val width = snapX(rightX - leftX + dx)
      currentShape!!.width = abs(width)
      if(width < 0) {
        currentShape!!.rightX = leftX
      } else {
        currentShape!!.leftX = leftX
      }
    }
  }

  private fun resizeVertically(dy: Double) {
    if(mdy < 0) {
      //Editor.Grid.SnapHeight(DY, TopY, BottomY)
      val height = snapY(bottomY - topY - dy)
      currentShape!!.height = abs(height)
      if(height < 0) {
        currentShape!!.topY = bottomY
      } else {
        currentShape!!.bottomY = bottomY
      }
    } else {
      //Editor.Grid.SnapHeight(DY, BottomY, TopY)
      val height = snapY(bottomY - topY + dy)
      currentShape!!.height = abs(height)
      if(height < 0) {
        currentShape!!.bottomY = topY
      } else {
        currentShape!!.topY = topY
      }
    }
  }

  override fun released(x: Double, y: Double) {
    currentBlock = null
  }

  override fun drawWhileDragging(g: Graphics2D) {
    draw(g)
  }

  override fun draw(g: Graphics2D) {
    if(selectedShapes.size != 1) return
    setBlocks(selectedShapes.first)
    for(block in blocks) {
      if(block.type == BlockType.nothing) continue
      g.fillRect(block.x, block.y, cursorSize, cursorSize)
    }
  }

  private fun setBlocks(shape: Shape) {
    for(yy in 0 .. 2) {
      val y = when(yy) {
        0 -> yToScreen(shape.topY).toInt() - cursorDistance - resizeSprite.cursorSize
        1 -> yToScreen(shape.centerY).toInt() - cursorSize / 2
        else -> yToScreen(shape.bottomY).toInt() + cursorDistance
      }
      for(xx in 0 .. 2) {
        if(xx == 1 && yy == 1) continue
        val x = when(xx) {
          0 -> xToScreen(shape.leftX).toInt() - cursorDistance - resizeSprite.cursorSize
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
