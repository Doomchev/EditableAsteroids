import mod.actions.currentImage
import mod.actions.deleteShapes
import mod.actions.selectImage
import mod.drawing.drawShapes
import mod.dragging.*
import mod.drawing.drawImages
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent.BUTTON1
import java.awt.event.MouseEvent.BUTTON3
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer
import kotlin.math.floor

val canvases = LinkedList<Canvas>()
val imags = LinkedList<BufferedImage>()
var currentCanvas: Canvas = Canvas(0, 0, 0, 0, 1.0)

class Window(): JPanel() {
  override fun paintComponent(g: Graphics) {
    val oldCanvas = currentCanvas
    val g2d = g as Graphics2D
    for(cnv in canvases) {
      cnv.draw(g2d)
    }
    currentCanvas = oldCanvas
  }
}

val panel = Window()
object updatePanel: ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    panel.repaint()
  }
}

val windowHeight = 800
val windowWidth = windowHeight * 9 / 16

fun main() {
  val world = Canvas(0, 0, windowWidth, windowHeight - 100, 10.0)
  world.setZoom(zoom)
  world.update()
  canvases.add(world)
  currentCanvas = world

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

  for(imageFile in File("./").listFiles()) {
    if(!imageFile.name.endsWith(".png")) continue
    imags.add(ImageIO.read(imageFile))
  }
  currentImage = imags.first

  val assets = Canvas(0, windowHeight - 100, windowWidth, 100, 64.0)
  canvases.add(assets)

    assets.add(drawImages)
  button1.add(assets, selectImage)

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
  panel.setSize(windowWidth, windowHeight)
  frame.contentPane = panel
  frame.setSize(windowWidth, windowHeight)
  frame.isVisible = true
}