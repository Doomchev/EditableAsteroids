import mod.Element
import java.awt.Graphics2D
import java.awt.event.*
import kotlin.math.abs

abstract class Pushable(val project: Project, val canvas: Canvas): Element {
  init {
    buttons.add(this)
  }

  open fun correspondsTo(e: MouseEvent): Boolean = false
  open fun correspondsTo(e: MouseWheelEvent): Boolean = false
  open fun correspondsTo(e: KeyEvent): Boolean = false

  val draggingActions = mutableListOf<DraggingAction>()
  val onClickActions = mutableListOf<Action>()
  val onPressActions = mutableListOf<Action>()
  val onUnpressActions = mutableListOf<Action>()
}

object keySerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    val key = Key(node.getInt("code"), user, world)
    node.getField("onClick", key.onClickActions)
    node.getField("onPress", key.onPressActions)
    node.getField("onUnpress", key.onUnpressActions)
    return key
  }
}

val buttons = mutableListOf<Pushable>()
class Key(private var code: Int, project: Project, canvas: Canvas): Pushable(project, canvas) {
  override fun correspondsTo(e: KeyEvent): Boolean {
    return e.keyChar.code == code || e.keyCode == code
  }

  override fun toNode(node: Node) {
    node.setInt("code", code)
    node.setField("onClick", onClickActions)
    node.setField("onPress", onPressActions)
    node.setField("onUnpress", onUnpressActions)
  }

  override fun toString(): String {
    return "клавишу ${Char(code)} ($code)"
  }
}

object mouseButtonSerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    val key = Key(node.getInt("code"), node.getField("project") as Project, node.getField("canvas") as Canvas)

    node.getField("onClick", key.onClickActions)
    node.getField("onPress", key.onPressActions)
    node.getField("onUnpress", key.onPressActions)
    return key
  }
}

class MouseButton(private var button: Int, project: Project, canvas: Canvas): Pushable(project, canvas) {
  override fun correspondsTo(e: MouseEvent): Boolean {
    return e.button == button
  }

  override fun toNode(node: Node) {
    node.setInt("button", button)
  }

  override fun toString(): String {
    return "кнопку мыши $button"
  }
}

object mouseWheelUpSerializer: ElementSerializer {
  override fun fromNode(node: Node): Element = mouseWheelUp(user, world)
}

class mouseWheelUp(project: Project, canvas: Canvas): Pushable(project, canvas) {
  override fun correspondsTo(e: MouseWheelEvent): Boolean {
    return e.wheelRotation < 0
  }

  override fun toNode(node: Node) {
  }

  override fun toString(): String {
    return "колесо вверх"
  }
}

object mouseWheelDownSerializer: ElementSerializer {
  override fun fromNode(node: Node): Element = mouseWheelDown(user, world)
}

class mouseWheelDown(project: Project, canvas: Canvas): Pushable(project, canvas) {
  override fun correspondsTo(e: MouseWheelEvent): Boolean {
    return e.wheelRotation > 0
  }

  override fun toNode(node: Node) {
  }

  override fun toString(): String {
    return "колесо вниз"
  }
}

var mousefx = 0.0
var mousefy = 0.0
var mousesx = 0
var mousesy = 0

object listener: MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
  private fun updateMouse(x: Int, y: Int) {
    mousesx = x
    mousesy = y
    mousefx = xFromScreen(mousesx)
    mousefy = yFromScreen(mousesy)
  }

  override fun mouseClicked(e: MouseEvent) {
    updateMouse(e.x, e.y)
    for(button in buttons) {
      if(!button.correspondsTo(e)) continue
      onClick(button, button.onClickActions)
    }
  }

  private fun onClick(button: Pushable, actions: MutableList<Action>) {
    for(action in actions) {
      if(!button.canvas.active || !button.canvas.hasMouse() || !action.conditions()) continue
      currentCanvas = button.canvas
      action.execute()
    }
  }

  private const val minDraggingDistance = 3
  private var pressedEvent: MouseEvent? = null

  override fun mousePressed(e: MouseEvent) {
    updateMouse(e.x, e.y)
    pressedEvent = e
  }

  private var currentDraggingAction: DraggingAction? = null
  private var currentDraggingCanvas: Canvas? = null

  override fun mouseDragged(e: MouseEvent) {
    updateMouse(e.x, e.y)
    if(currentDraggingAction == null) {
      if(abs(e.x - pressedEvent!!.x) < minDraggingDistance
        && abs(e.y - pressedEvent!!.y) < minDraggingDistance) {
        return
      }

      for(button in buttons) {
        if(!button.correspondsTo(pressedEvent!!)) continue
        onDragStart(button, button.draggingActions)
      }
      return
    }

    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction!!.dragged()
  }

  private fun onDragStart(button: Pushable, actions: MutableList<DraggingAction>) {
    if(!button.canvas.hasMouse()) return
    for(action in actions) {
      currentCanvas = button.canvas
      updateMouse(pressedEvent!!.x, pressedEvent!!.y)
      if(!action.conditions()) continue
      currentDraggingCanvas = button.canvas
      currentDraggingAction = action
      action.pressed()
      return
    }
  }

  override fun mouseReleased(e: MouseEvent) {
    updateMouse(e.x, e.y)
    if(currentDraggingAction == null) {
      for(button in buttons) {
        if(!button.correspondsTo(e)) continue
        onClick(button, button.onClickActions)
        break
      }
      return
    }

    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction!!.released()
    currentDraggingAction = null
  }

  override fun mouseEntered(e: MouseEvent) {
  }

  override fun mouseExited(e: MouseEvent) {
  }

  override fun mouseMoved(e: MouseEvent) {
    updateMouse(e.x, e.y)
    if(currentDraggingAction == null) return
    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction!!.dragged()
  }

  override fun mouseWheelMoved(e: MouseWheelEvent) {
    for(wheel in buttons) {
      if(!wheel.correspondsTo(e)) continue
      onClick(wheel, wheel.onClickActions)
    }
  }

  override fun keyTyped(e: KeyEvent) {
    if(e.keyChar.code == 96) {
      world.toggle()
      assets.toggle()
      properties.toggle()
      return
    }

    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      onClick(key, key.onClickActions)
    }
  }

  private val keysPressed = mutableListOf<KeyEntry>()
  class KeyEntry(val key: Pushable, val canvas: Canvas, var remove: Boolean = false)

  override fun keyPressed(e: KeyEvent) {
    for(keyEntry in keysPressed) {
      if(keyEntry.key.correspondsTo(e)) return
    }

    for(key in buttons) {
      if(!key.correspondsTo(e)) continue
      if(!key.canvas.hasMouse()) continue
      for(entry in key.onPressActions) {
        keysPressed.add(KeyEntry(key, key.canvas))
      }
      onDragStart(key, key.draggingActions)
    }
  }

  fun onKeyDown() {
    val it = keysPressed.iterator()
    while(it.hasNext()) {
      val entry = it.next()
      if(entry.remove) {
        it.remove()
        return
      }
      for(action in entry.key.onPressActions) {
        if(!action.conditions()) {
          it.remove()
          continue
        }
        action.execute()
      }
    }
  }

  override fun keyReleased(e: KeyEvent) {
    for(key in buttons) {
      if(!key.correspondsTo(e) || !key.canvas.active || !key.canvas.hasMouse()) continue
      currentCanvas = key.canvas
      for(action in key.onUnpressActions) {
        if(!action.conditions()) continue
        action.execute()
      }
    }
    for(keyEntry in keysPressed) {
      if(!keyEntry.key.correspondsTo(e)) continue
      keyEntry.remove = true
    }
    if(currentDraggingAction == null) return
    currentCanvas = currentDraggingCanvas!!
    currentDraggingAction!!.released()
    currentDraggingAction = null
  }

  fun draw(g:Graphics2D, canvas: Canvas) {
    if(currentDraggingCanvas == canvas && currentDraggingAction != null) {
      currentDraggingAction!!.drawWhileDragging(g)
    }
  }
}
