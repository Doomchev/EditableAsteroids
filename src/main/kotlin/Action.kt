import java.util.*

val actions = LinkedList<Action>()
interface Action {
  fun conditions(x: Double, y: Double): Boolean = true
  fun execute(x: Double, y: Double)
}