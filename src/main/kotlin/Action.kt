import java.util.*

val actions = LinkedList<Action>()
interface Action {
  fun conditions(x: Int, y: Int): Boolean = true
  fun execute(x: Int, y: Int)
}