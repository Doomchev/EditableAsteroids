import mod.dragging.Element
import java.util.*

interface Action {
  fun conditions(): Boolean = true
  open fun execute() {}
}

class ActionEntry(val canvas: Canvas, val action: Action) {
  override fun toString(): String {
    return action.toString()
  }
}

val actions = LinkedList<SpriteAction>()
val newActions = LinkedList<SpriteAction>()

abstract class SpriteAction(val sprite: Sprite): Action, Element {
  override fun toString(): String {
    return sprite.name
  }
}