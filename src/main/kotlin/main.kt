import mod.Element
import mod.actions.restoreCamera
import mod.actions.showMenu
import mod.actions.splitImage
import mod.actions.sprite.SpriteCreate
import mod.actions.sprite.currentImageArray
import mod.actions.sprite.deleteSprites
import mod.actions.sprite.selectImage
import mod.actions.tilemap.createTileMap
import mod.dragging.*
import mod.drawing.*
import mod.project
import mod.selectedSprites
import java.awt.Color
import java.awt.event.MouseEvent.*
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.Timer
import kotlin.system.exitProcess

var showCollisionShapes = false
var showGrid = false

val canvases = LinkedList<Canvas>()
val imageArrays = LinkedList<ImageArray>()
var blankImage: Image = Image(Texture(""))

val sounds = LinkedList<File>()

const val windowHeight = 800
const val windowWidth = windowHeight * 9 / 16
val frame = JFrame("Elasmotherium")

val world = Canvas(0, 0, windowWidth, windowHeight - 100, 10.0, true)
var currentCanvas: Canvas = world
val objectMenu = JPopupMenu()
val imageMenu = JPopupMenu()
val actionMenu = JPopupMenu()
var backgroundColor = Color(9, 44, 84)

val assets = Canvas(0, windowHeight - 100, windowWidth,100, 64.0, true)

val properties = Canvas(0, 0, windowWidth, windowHeight, 1.0, false)

class Project(): Element {
  override fun toNode(node: Node) {
  }
}

val ide = Project()
val user = Project()

fun main() {
  world.setZoom(zoom)
  world.update()
  world.setDefaultPosition()
  canvases.add(world)
  currentCanvas = world

  val button1 = MouseButton(BUTTON1, ide)
  button1.add(world, resizeSprite)
  button1.add(world, rotateSprite)
  button1.add(world, moveSprites)
  button1.add(world, selectSprites)
  button1.addOnClick(world, selectSprite)

  val button2 = MouseButton(BUTTON3, ide)
  button2.add(world, createSprite)
  button2.addOnClick(world, showMenu(objectMenu))

  val panButton = MouseButton(BUTTON2, ide)
  panButton.add(world, pan)
  Key(127, ide).addOnClick(world, deleteSprites)

  mouseWheelUp(ide).addOnClick(world, zoomIn)
  mouseWheelDown(ide).addOnClick(world, zoomOut)

  world.add(grid)
  world.add(drawScene)
  world.add(selectSprites)
  world.add(rotateSprite)
  world.add(resizeSprite)
  world.add(drawDefaultCamera)

  /// ASSETS LOADING

  for(imageFile in File("./").listFiles()) {
    if(!imageFile.name.endsWith(".png")) continue
    val image = Image(Texture(imageFile.name))
    imageArrays.add(ImageArray(Array(1) { image }, imageFile.name))
  }

  for(soundFile in File("./").listFiles()) {
    if(!soundFile.name.endsWith(".wav")) continue
    sounds.add(soundFile)
  }

  /// GUI

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

  /// SCENE OBJECTS GUI

  val itemToTop = JMenuItem("Наверх")
  itemToTop.addActionListener {
    for(sprite in selectedSprites) {
      project.remove(sprite)
      project.add(sprite)
    }
  }
  objectMenu.add(itemToTop)

  val itemToBottom = JMenuItem("Вниз")
  itemToBottom.addActionListener {
    for(sprite in selectedSprites.descendingIterator()) {
      project.remove(sprite)
      project.addFirst(sprite)
    }
  }
  objectMenu.add(itemToBottom)

  val itemSetBackground = JMenuItem("Цвет фона")
  itemSetBackground.addActionListener {
    backgroundColor = JColorChooser.showDialog(frame, "Выберите цвет фона:", backgroundColor)
  }
  objectMenu.add(itemSetBackground)

  val createItem = JMenu("Создать...")
  objectMenu.add(createItem)

  val classItem = JMenuItem("Класс")
  classItem.addActionListener {
    project.add(addClass(enterString("Введите название класса:")))
  }
  createItem.add(classItem)

  val itemCreate = JMenuItem("Элемент")
  itemCreate.addActionListener {
    actions.add(SpriteCreate(Sprite(currentImageArray!!.images[0]), selectClass()))
  }
  createItem.add(itemCreate)

  val tileMapItem = JMenuItem("Карту")
  tileMapItem.addActionListener {
    project.add(createTileMap())
  }
  createItem.add(tileMapItem)

  Key(118, ide).addOnClick(world, restoreCamera())

  Key(103, ide).addOnClick(world, object: Action {
    override fun execute() {
      showGrid = !showGrid
    }
  })

  Key(99, ide).addOnClick(world, object: Action {
    override fun execute() {
      showCollisionShapes = !showCollisionShapes
    }
  })

  /// IMAGES GUI

  canvases.add(assets)

  assets.add(drawImages)
  button1.addOnClick(assets, selectImage)
  button2.addOnClick(assets, showMenu(imageMenu))

  val itemCutImage = JMenuItem("Разрезать")
  itemCutImage.addActionListener {
    val xquantity = enterInt("Введите кол-во изображений по горизонтали:")
    val yquantity = enterInt("Введите кол-во изображений по вертикали:")
    splitImage(currentImageArray!!, xquantity, yquantity)
  }
  imageMenu.add(itemCutImage)

  val itemSetCenter = JMenuItem("Задать центр")
  itemSetCenter.addActionListener {
    val x = enterDouble("Введите горизонтальное смещение:").get()
    val y = enterDouble("Введите вертикальное смещение:").get()
    currentImageArray!!.setCenter(x, y)
  }
  imageMenu.add(itemSetCenter)

  val itemSetVisArea = JMenuItem("Задать размер обл. вывода")
  itemSetVisArea.addActionListener {
    val xk = enterDouble("Введите коэфф. к ширине:").get()
    val yk = enterDouble("Введите коэфф. к высоте:").get()
    currentImageArray!!.setVisibleArea(xk, yk)
  }
  imageMenu.add(itemSetVisArea)

  blankImage = Image(imageArrays[0].images[0].texture, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0)
  imageArrays.addFirst(ImageArray(Array(1) {blankImage}, "Пустое"))
  currentImageArray = imageArrays[0]

  // PROPERTIES GUI

  properties.add(drawBlocks)
  canvases.add(properties)
  button2.addOnClick(properties, showMenu(actionMenu))

  val addProperty = JMenuItem("Добавить действие")
  addProperty.addActionListener {
    selectedBlock!!.addElement()
  }
  actionMenu.add(addProperty)

  val removeProperty = JMenuItem("Удалить")
  removeProperty.addActionListener {
    selectedBlock!!.removeElement()
  }
  actionMenu.add(removeProperty)

  val addSpriteEvent = JMenu("Добавить событие спрайта")
  actionMenu.add(addSpriteEvent)

  addEventMenu(addSpriteEvent, false, "При клике...", MenuEvent.onClick)
  addEventMenu(addSpriteEvent, false, "При нажатии...", MenuEvent.onPress)
  addEventMenu(addSpriteEvent, false, "Всегда...", MenuEvent.always)

  val addClassEvent = JMenu("Добавить событие класса")
  actionMenu.add(addClassEvent)

  addEventMenu(addClassEvent, true, "При создании...", MenuEvent.onCreate)
  addEventMenu(addClassEvent, true, "При столкновении...", MenuEvent.onCollision)
  addEventMenu(addClassEvent, true, "Всегда...", MenuEvent.always)

  // SCENE

  registerSerializers()

  if(true) {
    imageArrays.clear()
    val reader = FileReader("test.xml")
    parser.text = reader.readText()
    val node = parser.fromText()
    project.fromNode(node!!)
    reader.close()
    blankImage = imageArrays[0].images[0]
  } else {
    asteroids()
    val node = Node("root")
    project.toNode(node)
    val writer = FileWriter("test.xml")
    writer.write(node.toText(""))
    writer.close()
    exitProcess(0)
  }

  updateActions()
  frame.isVisible = true
}

enum class MenuEvent {
  onClick,
  onPress,
  always,
  onCollision,
  onCreate
}