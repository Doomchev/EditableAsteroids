import java.util.*

val actions = LinkedList<Action>()

interface Action {
  fun conditions(): Boolean = true
  fun execute() {
  }
}

abstract class SpriteAction(val sprite: Sprite): Action {
}