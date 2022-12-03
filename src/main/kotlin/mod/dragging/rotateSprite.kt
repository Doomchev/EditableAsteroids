package mod.dragging

import DraggingAction
import Sprite
import distFromScreen
import mod.Drawing
import mod.selectedSprites
import mousefx
import mousefy
import snapAngle
import xToScreen
import yToScreen
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

object rotateSprite: DraggingAction, Drawing {
  private const val cursorSize = 8
  private val block = Sprite()
  var currentSprite: Sprite? = null

  override fun conditions(): Boolean {
    if(selectedSprites.size != 1) return false
    currentSprite = selectedSprites.first
    return block.collidesWithPoint(mousefx, mousefy)
  }

  override fun dragged() {
    if(currentSprite == null) return
    currentSprite!!.angle = snapAngle(atan2(mousefy - currentSprite!!.centerY, mousefx - currentSprite!!.centerX))
  }

  override fun drawWhileDragging(g: Graphics2D) {
    draw(g)
  }

  override fun draw(g: Graphics2D) {
    if(selectedSprites.size != 1) return
    setBlock(selectedSprites.first)
    val xx = xToScreen(block.leftX)
    val yy = yToScreen(block.topY)
    g.color = Color.WHITE
    g.fillOval(xx, yy, cursorSize, cursorSize)
    g.color = Color.BLACK
    g.drawOval(xx, yy, cursorSize, cursorSize)
  }

  private fun setBlock(sprite: Sprite) {
    val size = 0.5 * (sprite.halfWidth + sprite.halfHeight) * 1.41 + distFromScreen(16)
    val csize = distFromScreen(cursorSize)
    block.centerX = sprite.centerX + size * cos(sprite.angle)
    block.centerY = sprite.centerY + size * sin(sprite.angle)
    block.width = csize
    block.height = csize
  }
}