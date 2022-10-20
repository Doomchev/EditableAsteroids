package modules

import Action
import Canvas
import MouseButton
import Sprite
import currentCanvas
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object spriteCreating: Action() {
  var startingX:Double = 0.0
  var startingY:Double = 0.0
  var sprite: Sprite? = null
  var canvas: Canvas? = null

  init {
    MouseButton(1, 1, spriteCreating)
  }

  override fun conditions(): Boolean {
    return currentCanvas != null
  }

  override fun start(x: Int, y: Int) {
    if(currentCanvas == null) return
    canvas = currentCanvas
    startingX = canvas!!.xToField(x)
    startingY = canvas!!.yToField(y)
    sprite = Sprite()
  }

  override fun dragging(x: Int, y: Int) {
    val fx: Double = canvas!!.xToField(x)
    val fy: Double = canvas!!.yToField(y)
    sprite?.leftX = min(startingX, fx)
    sprite?.topY = max(startingY, fy)
    sprite?.width = abs(startingX - fx)
    sprite?.height = abs(startingY - fy)
  }

  override fun stop() {

  }
}