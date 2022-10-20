import java.util.*
import kotlin.system.exitProcess

var currentCanvas: Canvas = Canvas(0.0, 0.0, 0.0, 0.0)

abstract class Action {
  val triggers = LinkedList<Button>()
  open fun execute() {}
  open fun conditions(): Boolean {
    return true
  }
  open fun start(x: Int, y: Int) {}
  open fun dragging(x: Int, y: Int) {}
  open fun stop() {}
}

object exit: Action() {
  override fun execute() {
    exitProcess(0);
  }
}

interface ReversibleAction {
  fun undo()
}