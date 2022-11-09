package mod.dragging

import Sprite
import mousefx
import mousefy
import mousesx
import mousesy
import snapX
import snapY
import xToScreen
import yToScreen
import java.awt.Graphics2D
import kotlin.math.abs

object resizeSprite: StartingPosition(), Drawing {
  class ResizerBlock(var x: Int, var y: Int, var type: BlockType)
  enum class BlockType {nothing, horizontal, vertical, both}

  var currentSprite: Sprite? = null
  var currentBlock: ResizerBlock? = null
  private const val cursorSize = 8
  private const val cursorDistance = 2
  private val blocks = Array(9) {
    ResizerBlock(0, 0, BlockType.nothing)
  }

  override fun conditions(): Boolean {
    if(selectedSprites.size != 1) return false
    currentSprite = selectedSprites.first
    currentBlock = underCursor(mousesx, mousesy)
    return currentBlock != null
  }

  private var mdx = 0.0
  private var mdy = 0.0

  private var leftX = 0.0
  private var topY = 0.0
  private var rightX = 0.0
  private var bottomY = 0.0

  override fun pressed() {
    if(currentBlock == null) return

    mdx = currentBlock!!.x - xToScreen(currentSprite!!.centerX).toDouble()
    mdy = currentBlock!!.y - yToScreen(currentSprite!!.centerY).toDouble()

    leftX = snapX(currentSprite!!.leftX)
    topY = snapY(currentSprite!!.topY)
    rightX = snapX(currentSprite!!.rightX)
    bottomY = snapY(currentSprite!!.bottomY)

    pressed(true)
  }

  override fun dragged() {
    if(currentBlock == null) return

    val dx = mousefx - startingX
    val dy = mousefy - startingY

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
      currentSprite!!.width = abs(width)
      if(width < 0) {
        currentSprite!!.leftX = rightX
      } else {
        currentSprite!!.rightX = rightX
      }
    } else {
      //Editor.Grid.SnapWidth(DX, RightX, LeftX)
      val width = snapX(rightX - leftX + dx)
      currentSprite!!.width = abs(width)
      if(width < 0) {
        currentSprite!!.rightX = leftX
      } else {
        currentSprite!!.leftX = leftX
      }
    }
  }

  private fun resizeVertically(dy: Double) {
    if(mdy < 0) {
      //Editor.Grid.SnapHeight(DY, TopY, BottomY)
      val height = snapY(bottomY - topY - dy)
      currentSprite!!.height = abs(height)
      if(height < 0) {
        currentSprite!!.topY = bottomY
      } else {
        currentSprite!!.bottomY = bottomY
      }
    } else {
      //Editor.Grid.SnapHeight(DY, BottomY, TopY)
      val height = snapY(bottomY - topY + dy)
      currentSprite!!.height = abs(height)
      if(height < 0) {
        currentSprite!!.bottomY = topY
      } else {
        currentSprite!!.topY = topY
      }
    }
  }

  override fun released() {
    currentBlock = null
  }

  override fun drawWhileDragging(g: Graphics2D) {
    draw(g)
  }

  override fun draw(g: Graphics2D) {
    if(selectedSprites.size != 1) return
    setBlocks(selectedSprites.first)
    for(block in blocks) {
      if(block.type == BlockType.nothing) continue
      g.fillRect(block.x, block.y, cursorSize, cursorSize)
    }
  }

  private fun setBlocks(sprite: Sprite) {
    for(yy in 0 .. 2) {
      val y = when(yy) {
        0 -> yToScreen(sprite.topY) - cursorDistance - cursorSize
        1 -> yToScreen(sprite.centerY) - cursorSize / 2
        else -> yToScreen(sprite.bottomY) + cursorDistance
      }
      for(xx in 0 .. 2) {
        if(xx == 1 && yy == 1) continue
        val x = when(xx) {
          0 -> xToScreen(sprite.leftX) - cursorDistance - cursorSize
          1 -> xToScreen(sprite.centerX) - cursorSize / 2
          else -> xToScreen(sprite.rightX) + cursorDistance
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