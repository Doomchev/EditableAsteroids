import mod.Element
import java.util.*

interface Action: Element {
  fun conditions(): Boolean = true
  fun execute() {}
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

abstract class SpriteAction(var sprite: Sprite): Action

val actions = mutableListOf<SpriteAction>()
val newActions = mutableListOf<SpriteAction>()