import java.awt.MouseInfo
import java.util.*

val actions = LinkedList<Action>()
interface Action {
  fun conditions(x: Double, y: Double): Boolean = true
  fun execute() {
    val point = MouseInfo.getPointerInfo().location
    val fx = xFromScreen(point.x)
    val fy = yFromScreen(point.y)
    execute(fx, fy)
  }

  fun execute(x: Double, y: Double)

  fun onButtonDown() {
    val point = MouseInfo.getPointerInfo().location
    val fx = xFromScreen(point.x)
    val fy = yFromScreen(point.y)
    onButtonDown(fx, fy)
  }

  fun onButtonDown(x: Double, y: Double) {
  }
  fun settings() {
  }
}