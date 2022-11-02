import mod.dragging.Drawing
import mod.dragging.zk
import java.awt.Graphics2D
import java.lang.Math.pow
import java.util.LinkedList
import kotlin.math.pow

class Canvas(x: Int, y:Int, width: Int, height:Int): Shape(x + width * 0.5, y + width * 0.5, width * 0.5, height * 0.5) {
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
    viewport = Area(x, y, width, height)
  }

  val drawingModules = LinkedList<Drawing>()
  fun add(obj: Drawing) {
    drawingModules.add(obj)
  }

  fun draw(g2d: Graphics2D) {
    canvas = this
    g2d.clipRect(viewport.leftX, viewport.topY, viewport.width, viewport.height)
    g2d.clearRect(viewport.leftX, viewport.topY, viewport.width, viewport.height)
    for(module in drawingModules) {
      module.draw(g2d)
    }
  }

  fun update() {
    k = viewport.width / width
    height = viewport.height / k
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

var canvas: Canvas = Canvas(0, 0, 0, 0)

fun xToScreen(fieldX: Double): Double = fieldX * canvas.k + canvas.vdx
fun yToScreen(fieldY: Double): Double = fieldY * canvas.k + canvas.vdy
fun distToScreen(fieldDist: Double): Double = fieldDist * canvas.k

fun xFromScreen(screenX: Int): Double = (screenX - canvas.vdx) / canvas.k
fun yFromScreen(screenY: Int): Double = (screenY - canvas.vdy) / canvas.k
fun distFromScreen(screenDist: Int): Double = screenDist / canvas.k