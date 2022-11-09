import java.awt.Graphics
import java.awt.Graphics2D

interface DraggingAction {
  fun conditions(): Boolean = true
  fun pressed() {}
  fun dragged() {}
  fun released() {}
  fun drawWhileDragging(g: Graphics2D) {}
}