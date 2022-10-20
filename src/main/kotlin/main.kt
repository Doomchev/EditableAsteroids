import mod.dragging.*
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer

val image = ImageIO.read(File("img.png"))

class Window(): JPanel() {
  override fun paintComponent(g: Graphics) {
    val g2d = g as Graphics2D
    g.clearRect(0, 0, width, height)
    for(module in allModules) {
      module.draw(g2d)
    }
    if(currentDraggingAction != null) {
      currentDraggingAction!!.draw(g2d)
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
  draggingActions.add(moveSprites)
  draggingActions.add(selectSprites)

  allModules.add(shapesModule)
  allModules.add(selectedShapesModule)

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