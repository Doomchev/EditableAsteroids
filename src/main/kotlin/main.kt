import mod.drawing.shapesDrawing
import mod.dragging.*
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
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
    canvas.update()
    panel.repaint()
  }
}

val windowWidth = 450
val windowHeight = 800

fun main() {
  draggingActions.add(createSprite)
  draggingActions.add(resizeShape)
  draggingActions.add(moveSprites)
  draggingActions.add(selectSprites)

  displayingModules.add(shapesDrawing)
  displayingModules.add(selectSprites)
  displayingModules.add(resizeShape)

  val timer = Timer(15, updatePanel)
  timer.start()

  val frame = JFrame("Elasmotherium")
  frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
  panel.addMouseListener(listener)
  panel.addMouseMotionListener(listener)
  frame.add(panel)
  frame.setSize(windowWidth, windowHeight)
  frame.isVisible = true
}