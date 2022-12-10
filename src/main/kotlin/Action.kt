import mod.Element
import java.util.*

interface Action : Element {
  fun conditions(): Boolean = true
  open fun execute() {}
  override fun toNode(node: Node) {}
}

object actionEntrySerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    return ActionEntry(world, node.getField("action") as Action)
  }
}

class ActionEntry(val canvas: Canvas, val action: Action): Element {
  override fun toNode(node: Node) {
    node.setField("action", action)
  }

  override fun toString(): String {
    return action.toString()
  }
}

val actions = LinkedList<SpriteAction>()
val newActions = LinkedList<SpriteAction>()

abstract class SpriteAction(var sprite: Sprite): Action, Element {
  override fun toString(): String {
    return sprite.name
  }
}