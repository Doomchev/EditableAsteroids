import java.util.*

interface Action {
  fun conditions(): Boolean = true
  fun execute() {}
}

class ActionEntry(val canvas: Canvas, val action: Action) {
  override fun toString(): String {
    return action.toString()
  }
}

val actions = LinkedList<SpriteAction>()
val newActions = LinkedList<SpriteAction>()
val actionsToRemove = LinkedList<SpriteAction>()

abstract class SpriteAction(val sprite: Sprite): Action {
  override fun toString(): String {
    return sprite.name
  }
}