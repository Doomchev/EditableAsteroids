import mod.actions.*
import mod.dragging.*
import mod.drawing.drawImages
import mod.drawing.drawShapes
import java.awt.event.MouseEvent.BUTTON1
import java.awt.event.MouseEvent.BUTTON3
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.Timer


val canvases = LinkedList<Canvas>()
val imageArrays = LinkedList<Array<Image>>()
var currentCanvas: Canvas = Canvas(0, 0, 0, 0, 1.0)

val windowHeight = 800
val windowWidth = windowHeight * 9 / 16
val frame = JFrame("Elasmotherium")

val world = Canvas(0, 0, windowWidth, windowHeight - 100, 10.0)
val objectMenu = JPopupMenu()
val imageMenu = JPopupMenu()

fun main() {
  world.setZoom(zoom)
  world.update()
  canvases.add(world)
  currentCanvas = world

  val button1 = MouseButton(BUTTON1)
  button1.add(world, resizeSprite)
  button1.add(world, rotateSprite)
  button1.add(world, moveSprites)
  button1.add(world, selectSprites)

  val button2 = MouseButton(BUTTON3)
  button2.add(world, createSprite)

  Key(32).add(world, showMenu(objectMenu))
  Key(17).add(world, pan)
  Key(127).add(world, deleteShapes)

  mouseWheelUp.add(world, zoomIn)
  mouseWheelDown.add(world, zoomOut)

  world.add(grid)
  world.add(drawShapes)
  world.add(selectSprites)
  world.add(rotateSprite)
  world.add(resizeSprite)

  for(imageFile in File("./").listFiles()) {
    if(!imageFile.name.endsWith(".png")) continue
    val image = Image(ImageIO.read(imageFile))
    val array = Array(1) { image }
    imageArrays.add(array)
  }
  currentImageArray = imageArrays.first

  val assets = Canvas(0, windowHeight - 100, windowWidth,100, 64.0)
  canvases.add(assets)

  assets.add(drawImages)
  button1.add(assets, selectImage)
  Key(32).add(assets, showMenu(imageMenu))

  val timer = Timer(10, updatePanel)
  timer.start()

  frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
  frame.setSize(windowWidth, windowHeight)

  panel.isFocusable = true
  panel.requestFocus()
  panel.addKeyListener(listener)
  panel.addMouseListener(listener)
  panel.addMouseMotionListener(listener)
  panel.addMouseWheelListener(listener)
  panel.setSize(windowWidth, windowHeight)
  frame.contentPane = panel

  val behaviorMenu = JMenu("Добавить поведение")
  objectMenu.add(behaviorMenu)
  addMenuItem(behaviorMenu, "Поворот", SpriteRotation())

  addMenuItem(imageMenu, "Разрезать", cutImage)

  frame.isVisible = true
}