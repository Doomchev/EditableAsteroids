import java.awt.MouseInfo
import java.util.*

val actions = LinkedList<Action>()

interface Action {
  fun conditions(): Boolean = true
  fun execute() {
  }

  fun settings() {
  }
}

abstract class SpriteAction: Action {
  var sprite: Sprite? = null
  abstract fun create(sprite: Sprite): SpriteAction
}