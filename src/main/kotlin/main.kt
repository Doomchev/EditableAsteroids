import mod.actions.deleteShapes
import mod.drawing.drawShapes
import mod.dragging.*
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent.BUTTON1
import java.awt.event.MouseEvent.BUTTON3
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer

val canvases = LinkedList<Canvas>()

class Window(): JPanel() {
  override fun paintComponent(g: Graphics) {
    val g2d = g as Graphics2D
    for(cnv in canvases) {
      cnv.draw(g2d)
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
  val world = Canvas(0, 0, windowWidth, windowHeight - 100)
  world.setZoom(zoom)
  world.update()
  canvases.add(world)

  //val assets = Canvas(0, windowHeight - 100, windowWidth, 100 )
  //canvases.add(assets)

  val button1 = MouseButton(BUTTON1)
  button1.add(world, resizeSprite)
  button1.add(world, moveSprites)
  button1.add(world, selectSprites)

  MouseButton(BUTTON3).add(world, createSprite)
  Key(17).add(world, pan)
  Key(127).add(world, deleteShapes)

  mouseWheelUp.add(world, zoomIn)
  mouseWheelDown.add(world, zoomOut)

  world.add(grid)
  world.add(drawShapes)
  world.add(selectSprites)
  world.add(resizeSprite)

  val timer = Timer(15, updatePanel)
  timer.start()

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