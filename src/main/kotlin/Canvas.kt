import mod.dragging.Drawing
import mod.dragging.zk
import java.awt.Graphics2D
import java.util.LinkedList
import kotlin.math.pow

class Canvas(fx: Int, fy:Int, fwidth: Int, fheight:Int)
  : Shape(0.0, 0.0, fwidth.toDouble() * 0.05, fheight.toDouble() * 0.05) {
  var vdx: Double = 1.0
  var vdy: Double = 1.0
  var k: Double = 1.0

  class Area(var leftX: Int, var topY:Int, var width: Int, var height:Int) {
    var rightX: Int
      inline get() = leftX + width
      inline set(value) {
        leftX = value - width
      }
    var bottomY: Int
      inline get() = topY + height
      inline set(value) {
        topY = value - height
      }
    fun hasPoint(px: Int, py: Int): Boolean {
      return px >= leftX && px < leftX + width && py >= topY && py < topY + height
    }
  }

  val viewport: Area
  init {
    viewport = Area(fx, fy, fwidth, fheight)
  }

  val drawingModules = LinkedList<Drawing>()
  fun add(obj: Drawing) {
    drawingModules.add(obj)
  }

  fun draw(g2d: Graphics2D) {
    currentCanvas = this
    update()
    g2d.setClip(viewport.leftX, viewport.topY, viewport.width, viewport.height)
    g2d.clearRect(viewport.leftX, viewport.topY, viewport.width, viewport.height)
    for(module in drawingModules) {
      module.draw(g2d)
    }
    if(currentDraggingCanvas == this && currentDraggingAction != null) {
      currentDraggingAction!!.drawWhileDragging(g2d)
    }
  }

  fun update() {
    k = 1.0 * viewport.width / width
    height = 1.0 * viewport.height / k
    vdx = centerX * k - (viewport.leftX + 0.5 * viewport.width)
    vdy = centerY * k - (viewport.topY + 0.5 * viewport.height)
  }

  fun setZoom(zoom: Int) {
    width = viewport.width * zk.pow(zoom.toDouble())
    update()
  }

  fun setZoom(zoom: Int, x: Int, y: Int) {
    val fx1 = xFromScreen(x)
    val fy1 = yFromScreen(y)
    setZoom(zoom)
    val fx2 = xFromScreen(x)
    val fy2 = yFromScreen(y)
    centerX += fx2 - fx1
    centerY += fy2 - fy1
    update()
  }

  fun hasPoint(x: Int, y: Int): Boolean {
    return viewport.hasPoint(x, y)
  }
}
fun xToScreen(fieldX: Double): Double = fieldX * currentCanvas.k + currentCanvas.vdx
fun yToScreen(fieldY: Double): Double = fieldY * currentCanvas.k + currentCanvas.vdy
fun distToScreen(fieldDist: Double): Double = fieldDist * currentCanvas.k

fun xFromScreen(screenX: Int): Double = (screenX - currentCanvas.vdx) / currentCanvas.k
fun yFromScreen(screenY: Int): Double = (screenY - currentCanvas.vdy) / currentCanvas.k
fun distFromScreen(screenDist: Int): Double = screenDist / currentCanvas.k