import mod.drawing.drawShapes
import mod.dragging.*
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent.BUTTON1
import java.awt.event.MouseEvent.BUTTON3
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer

class Window(): JPanel() {
  override fun paintComponent(g: Graphics) {
    val g2d = g as Graphics2D
    g.clearRect(0, 0, width, height)
    for(module in displayingModules) {
      module.draw(g2d)
    }
    if(currentDraggingAction != null) {
      currentDraggingAction!!.drawWhileDragging(g2d)
    }
  }
}

val panel = Window()
object updatePanel: ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    panel.repaint()
  }
}

val windowWidth = 450
val windowHeight = 800

fun main() {
  val button1 = MouseButton(BUTTON1)
  val button3 = MouseButton(BUTTON3)
  val ctrl = Key(17)
  button3.add(createSprite)
  button1.add(resizeSprite)
  button1.add(moveSprites)
  button1.add(selectSprites)
  ctrl.add(pan)

  //mouseWheelUp.add(zoom)
  //mouseWheelDown.add(zoom)

  displayingModules.add(grid)
  displayingModules.add(drawShapes)
  displayingModules.add(selectSprites)
  displayingModules.add(resizeSprite)

  val timer = Timer(15, updatePanel)
  timer.start()

  canvas.update()

  val frame = JFrame("Elasmotherium")
  frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
  panel.isFocusable = true
  panel.requestFocus()
  panel.addKeyListener(listener)
  panel.addMouseListener(listener)
  panel.addMouseMotionListener(listener)
  panel.addMouseWheelListener(listener)
  frame.add(panel)
  frame.setSize(windowWidth, windowHeight)
  frame.isVisible = true
}