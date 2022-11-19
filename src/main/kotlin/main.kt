import mod.actions.SoundPlay
import mod.actions.cutImage
import mod.actions.restoreCamera
import mod.actions.showMenu
import mod.actions.sprite.*
import mod.dragging.*
import mod.drawing.drawDefaultCamera
import mod.drawing.drawImages
import mod.drawing.drawScene
import java.awt.Color
import java.awt.event.MouseEvent.BUTTON1
import java.awt.event.MouseEvent.BUTTON3
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.Timer
import kotlin.math.PI

val showCollisionShapes = true

val canvases = LinkedList<Canvas>()
val imageArrays = LinkedList<ImageArray>()
var blankImage: Image? = null

val sounds = LinkedList<File>()
var soundOptions: Array<File>? = null

var currentCanvas: Canvas = Canvas(0, 0, 0, 0, 1.0)
val windowHeight = 800
val windowWidth = windowHeight * 9 / 16
val frame = JFrame("Elasmotherium")

val world = Canvas(0, 0, windowWidth, windowHeight - 100, 10.0)
val objectMenu = JPopupMenu()
val imageMenu = JPopupMenu()
var backgroundColor: Color = Color.white

val assets = Canvas(0, windowHeight - 100, windowWidth,100, 64.0)

fun main() {
  world.setZoom(zoom)
  world.update()
  world.setDefaultPosition()
  canvases.add(world)
  currentCanvas = world

  val button1 = MouseButton(BUTTON1)
  button1.add(world, resizeSprite)
  button1.add(world, rotateSprite)
  button1.add(world, moveSprites)
  button1.add(world, selectSprites)
  button1.addOnClick(world, selectSprite)

  val button2 = MouseButton(BUTTON3)
  button2.add(world, createSprite)
  button2.addOnClick(world, showMenu(objectMenu, false))

  Key(17).add(world, pan)
  Key(127).addOnClick(world, deleteSprites)

  mouseWheelUp.addOnClick(world, zoomIn)
  mouseWheelDown.addOnClick(world, zoomOut)

  world.add(grid)
  world.add(drawScene)
  world.add(selectSprites)
  world.add(rotateSprite)
  world.add(resizeSprite)
  world.add(drawDefaultCamera)

  /// ASSETS LOADING

  for(imageFile in File("./").listFiles()) {
    if(!imageFile.name.endsWith(".png")) continue
    val image = Image(ImageIO.read(imageFile))
    imageArrays.add(ImageArray(Array(1) { image }))
  }

  for(soundFile in File("./").listFiles()) {
    if(!soundFile.name.endsWith(".wav")) continue
    sounds.add(soundFile)
  }
  soundOptions = Array(sounds.size) {sounds[it]}

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

  fillEventMenu(objectMenu, null)

  val itemToTop = JMenuItem("Наверх")
  itemToTop.addActionListener {
    for(sprite in selectedSprites) {
      scene.remove(sprite)
      scene.add(sprite)
    }
  }
  objectMenu.add(itemToTop)

  val itemToBottom = JMenuItem("Вниз")
  itemToBottom.addActionListener {
    for(sprite in selectedSprites.descendingIterator()) {
      scene.remove(sprite)
      scene.addFirst(sprite)
    }
  }
  objectMenu.add(itemToBottom)

  val itemSetBackground = JMenuItem("Цвет фона")
  itemSetBackground.addActionListener {
    backgroundColor = JColorChooser.showDialog(frame, "Выберите цвет фона:", backgroundColor)
  }
  objectMenu.add(itemSetBackground)

  Key(99).addOnClick(world, restoreCamera())

  /// IMAGES GUI

  canvases.add(assets)

  assets.add(drawImages)
  button1.addOnClick(assets, selectImage)
  button2.addOnClick(assets, showMenu(imageMenu, false))

  val itemCutImage = JMenuItem("Разрезать")
  itemCutImage.addActionListener {
    val xquantity = enterInt("Введите кол-во изображений по горизонтали:")
    val yquantity = enterInt("Введите кол-во изображений по вертикали:")
    cutImage(currentImageArray!!, xquantity, yquantity)
  }
  imageMenu.add(itemCutImage)

  val itemSetCenter = JMenuItem("Задать центр")
  itemSetCenter.addActionListener {
    val x = enterDouble("Введите горизонтальное смещение (%):")
    val y = enterDouble("Введите вертикальное смещение (%):")
    currentImageArray!!.setCenter(x, y)
  }
  imageMenu.add(itemSetCenter)

  val itemSetVisArea = JMenuItem("Задать размер обл. вывода")
  itemSetVisArea.addActionListener {
    val xk = enterDouble("Введите коэфф. к ширине:")
    val yk = enterDouble("Введите коэфф. к высоте:")
    currentImageArray!!.setVisibleArea(xk, yk)
  }
  imageMenu.add(itemSetVisArea)

  /// IMAGES

  val asteroidImage = imageArrays.first
  cutImage(asteroidImage, 8, 4)
  currentImageArray = asteroidImage

  val bulletImage = imageArrays[1]
  cutImage(bulletImage, 1, 16)
  bulletImage.setCenter(43.0 / 48.0, 5.5 / 12.0)
  bulletImage.setVisibleArea(10.5, 3.0)

  val shipImage = imageArrays[2]
  shipImage.setCenter(0.35, 0.5)
  shipImage.setVisibleArea(1.5, 1.5)

  /// SPRITES

  val player = Sprite(-3.0, -5.0, 1.5, 1.5)
  player.image = imageArrays[2].images[0]

  val action1 = SpriteRotation()
  action1.sprite = player
  action1.speed = -1.5 * PI
  Key(97).onPressActions.add(Pushable.ActionEntry(world, action1))

  val action2 = SpriteRotation()
  action2.sprite = player
  action2.speed = 1.5 * PI
  Key(100).onPressActions.add(Pushable.ActionEntry(world, action2))

  val action3 = SpriteAcceleration()
  action3.sprite = player
  action3.acceleration = 50.0
  action3.limit = 10.0
  Key(119).onPressActions.add(Pushable.ActionEntry(world, action3))

  val action4 = SpriteMovement()
  action4.sprite = player
  actions.add(action4)

  val action5 = SpriteAcceleration()
  action5.sprite = player
  action5.acceleration = -15.0
  actions.add(action5)

  /*val action12 = SpriteSetBounds()
  action12.sprite = player
  action12.settings()
  actions.add(action12)*/

  val bullet = addClass("Пуля")

  val action6 = SpritePositionAs()
  action6.sprite2 = player
  bullet.onCreate.add(action6)

  val action9 = SpriteSetSize()
  action9.width = 0.15
  action9.height = 0.15
  bullet.onCreate.add(action9)

  val action7 = SpriteDirectAs()
  action7.sprite2 = player
  bullet.onCreate.add(action7)

  val action11 = SpriteSetSpeed()
  action11.speed = 15.0
  bullet.onCreate.add(action11)

  bullet.always.add(SpriteMovement())

  val action13 = SpriteAnimation()
  action13.array = imageArrays[1]
  action13.speed = 16.0
  bullet.always.add(action13)

  val action8 = SpriteCreate()
  action8.spriteClass = bullet
  action8.delay = 0.1
  Key(32).onPressActions.add(Pushable.ActionEntry(world, action8))

  scene.add(player)
  scene.add(bullet)

  blankImage = Image(imageArrays[0].images[0].texture, 0, 0, 0, 0)
  imageArrays.addFirst(ImageArray(Array(1) {blankImage!!}))

  frame.isVisible = true
}

fun addClass(caption: String): SpriteClass {
  val newClass = SpriteClass(caption)
  classes.add(newClass)
  val classMenu = JMenu(caption)
  objectMenu.add(classMenu)
  classMenu.add(actionMenu("При создании...", newClass, MenuEvent.onCreate))
  classMenu.add(actionMenu("Всегда...", newClass, MenuEvent.always))
  return newClass
}

enum class MenuEvent {
  onClick,
  onPress,
  always,
  onCreate
}

fun fillEventMenu(menu: JPopupMenu, spriteClass: SpriteClass?) {
  menu.add(actionMenu("При клике...", spriteClass, MenuEvent.onClick))
  menu.add(actionMenu("При нажатии...", spriteClass, MenuEvent.onPress))
  menu.add(actionMenu("Всегда...", spriteClass, MenuEvent.always))

  val classItem = JMenuItem("Создать класс")
  classItem.addActionListener {
    addClass(enterString("Введите название класса:"))
  }
  objectMenu.add(classItem)
}

fun actionMenu(caption: String, spriteClass: SpriteClass?, event: MenuEvent): JMenu {
  val actions = listOf(SpriteCreate(), SpritePositionAs(), SpriteSetSize(), SpriteRotation(), SpriteDirectAs(), SpriteMovement(), SpriteSetSpeed(), SpriteAcceleration(), SpriteSetImage(), SpriteAnimation(), SoundPlay(), SpriteSetBounds(), SpriteLoopArea())

  val menu = JMenu(caption)
  for(action in actions) {
    addMenuItem(menu, MenuListener(spriteClass, event, action))
  }

  return menu
}