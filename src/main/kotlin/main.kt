import mod.actions.SoundPlayFactory
import mod.actions.splitImage
import mod.actions.restoreCamera
import mod.actions.showMenu
import mod.actions.sprite.*
import mod.actions.tilemap.createTileMap
import mod.dragging.*
import mod.drawing.drawDefaultCamera
import mod.drawing.drawImages
import mod.drawing.drawScene
import java.awt.Color
import java.awt.event.MouseEvent.*
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.Timer
import kotlin.math.PI

var showCollisionShapes = true
var showGrid = false

val canvases = LinkedList<Canvas>()
val imageArrays = LinkedList<ImageArray>()
var blankImage: Image = Image(BufferedImage(1, 1, TYPE_INT_RGB))

val sounds = LinkedList<File>()

var currentCanvas: Canvas = Canvas(0, 0, 0, 0, 1.0)
const val windowHeight = 800
const val windowWidth = windowHeight * 9 / 16
val frame = JFrame("Elasmotherium")

val world = Canvas(0, 0, windowWidth, windowHeight - 100, 10.0)
val objectMenu = JPopupMenu()
val imageMenu = JPopupMenu()
var backgroundColor = Color(9, 44, 84)

val assets = Canvas(0, windowHeight - 100, windowWidth,100, 64.0)

val newActions = LinkedList<Action>()

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

  val panButton = MouseButton(BUTTON2)
  panButton.add(world, pan)
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

  val itemCreate = JMenuItem("Создать элемент")
  itemCreate.addActionListener {
    actions.add(SpriteCreate(Sprite(), selectClass(), enterDouble(
      "Введите интервал:").get()))
  }
  objectMenu.add(itemCreate)

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

  val classItem = JMenuItem("Создать класс")
  classItem.addActionListener {
    addClass(enterString("Введите название класса:"))
  }
  objectMenu.add(classItem)

  val tileMapItem = JMenuItem("Создать карту")
  tileMapItem.addActionListener {
    scene.add(createTileMap())
  }
  objectMenu.add(tileMapItem)

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
    splitImage(currentImageArray!!, xquantity, yquantity)
  }
  imageMenu.add(itemCutImage)

  val itemSetCenter = JMenuItem("Задать центр")
  itemSetCenter.addActionListener {
    val x = enterDouble("Введите горизонтальное смещение (%):").get()
    val y = enterDouble("Введите вертикальное смещение (%):").get()
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

  asteroids()

  blankImage = Image(imageArrays[0].images[0].texture, 0, 0, 0, 0)
  imageArrays.addFirst(ImageArray(Array(1) {blankImage}))
  currentImageArray = imageArrays[0]

  frame.isVisible = true
}

fun tilemap() {
  splitImage(imageArrays[4], 5, 7)
  scene.add(TileMap(10, 16, 1.0, 1.0, imageArrays[4]))
}

fun snow() {
  val flake = addClass("Снежинка")

  val area = Sprite(0.0, -10.0, 10.0, 2.0)
  flake.onCreate.add(SpritePositionInAreaFactory(area))
  flake.onCreate.add(SpriteSetSizeFactory(RandomDoubleValue(0.25, 1.0)))
  flake.onCreate.add(SpriteSetImageFactory(imageArrays[3].images[0]))
  flake.onCreate.add(SpriteSetMovingVectorFactory(zero, RandomDoubleValue(1.0, 5.0)))
  flake.always.add(SpriteMoveFactory())

  scene.add(area)
  actions.add(SpriteCreate(Sprite(), flake, 0.1))
}

fun asteroids() {
  /// IMAGES

  val asteroidImage = imageArrays.first
  splitImage(asteroidImage, 8, 4)

  val bulletImage = imageArrays[1]
  splitImage(bulletImage, 1, 16)
  bulletImage.setCenter(43.0 / 48.0, 5.5 / 12.0)
  bulletImage.setVisibleArea(10.5, 3.0)

  val shipImage = imageArrays[2]
  shipImage.setCenter(0.35, 0.5)
  shipImage.setVisibleArea(1.5, 1.5)

  /// SPRITES

  val players = addClass("Игрок")
  val player = Sprite(-3.0, -5.0, 1.5, 1.5)
  player.image = imageArrays[2].images[0]
  players.add(player)

  Key(97).onPressActions.add(Pushable.ActionEntry(world, SpriteRotation(player, -1.5 * PI)))
  Key(100).onPressActions.add(Pushable.ActionEntry(world, SpriteRotation(player, 1.5 * PI)))
  Key(119).onPressActions.add(Pushable.ActionEntry(world, SpriteAcceleration(player, 50.0, 10.0)))

  val bounds = Sprite(world.centerX, world.centerY, world.width + 2.0, world.height + 2.0)
  actions.add(SpriteAcceleration(player, -15.0, 100.0))
  actions.add(SpriteLoopArea(player, bounds))
  actions.add(SpriteMove(player))

  val bullet = addClass("Пуля")

  bullet.onCreate.add(SpritePositionAsFactory(player))
  bullet.onCreate.add(SpriteDirectAsFactory(player))
  bullet.onCreate.add(SpriteSetSizeFactory(DoubleValue(0.15)))
  bullet.onCreate.add(SpriteSetSpeedFactory(DoubleValue(15.0)))
  bullet.always.add(SpriteMoveFactory())
  bullet.always.add(SpriteAnimationFactory(imageArrays[1], DoubleValue(16.0)))
  bullet.always.add(SpriteSetBoundsFactory(bounds))

  Key(32).onPressActions.add(Pushable.ActionEntry(world, SpriteCreate(player, bullet, 0.1)))

  scene.add(player)
  scene.add(bullet)
}

fun addClass(caption: String): SpriteClass {
  val newClass = SpriteClass(caption)
  classes.add(newClass)
  val classMenu = JMenu(caption)
  objectMenu.add(classMenu)
  classMenu.add(actionMenu("При создании...", newClass, MenuEvent.onCreate))
  classMenu.add(actionMenu("Всегда...", newClass, MenuEvent.always))
  scene.add(newClass)
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
}

fun actionMenu(caption: String, spriteClass: SpriteClass?, event: MenuEvent): JMenu {
  val actions = listOf(SpritePositionAsFactory(), SpritePositionInAreaFactory(), SpriteSetSizeFactory(), SpriteSetAngleFactory(), SpriteRotationFactory(), SpriteDirectAsFactory(), SpriteMoveFactory(), SpriteSetMovingVectorFactory(), SpriteSetSpeedFactory(), SpriteAccelerationFactory(), SpriteSetImageFactory(), SpriteAnimationFactory(), SoundPlayFactory(), SpriteSetBoundsFactory(), SpriteLoopAreaFactory())

  val menu = JMenu(caption)
  for(action in actions) {
    addMenuItem(menu, MenuListener(spriteClass, event, action))
  }

  return menu
}