import java.util.*

val actions = LinkedList<Action>()

interface Action {
  fun conditions(): Boolean = true
  fun settings() {
  }
  fun execute() {
  }
}

abstract class SpriteAction: Action {
  var sprite: Sprite? = null
  abstract fun create(sprite: Sprite?): SpriteAction
}