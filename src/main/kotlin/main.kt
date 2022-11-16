import mod.actions.cutImage
import mod.actions.showMenu
import mod.actions.sprite.*
import mod.dragging.*
import mod.drawing.drawImages
import mod.drawing.drawSprites
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent.BUTTON1
import java.awt.event.MouseEvent.BUTTON3
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.Timer
import kotlin.math.PI


val canvases = LinkedList<Canvas>()
val imageArrays = LinkedList<ImageArray>()
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
  button1.addOnClick(world, selectSprite)

  val button2 = MouseButton(BUTTON3)
  button2.add(world, createSprite)
  button2.addOnClick(world, showMenu(objectMenu, false))

  Key(17).add(world, pan)
  Key(127).addOnClick(world, deleteSprites)

  mouseWheelUp.addOnClick(world, zoomIn)
  mouseWheelDown.addOnClick(world, zoomOut)

  world.add(grid)
  world.add(drawSprites)
  world.add(selectSprites)
  world.add(rotateSprite)
  world.add(resizeSprite)

  for(imageFile in File("./").listFiles()) {
    if(!imageFile.name.endsWith(".png")) continue
    val image = Image(ImageIO.read(imageFile))
    imageArrays.add(ImageArray(Array(1) { image }))
  }
  currentImageArray = imageArrays.first
  cutImage(currentImageArray!!, 8, 4)
  //cutSprite(imageArrays[1], 1, 16)

  val assets = Canvas(0, windowHeight - 100, windowWidth,100, 64.0)
  canvases.add(assets)

  assets.add(drawImages)
  button1.addOnClick(assets, selectImage)
  button2.addOnClick(assets, showMenu(imageMenu, false))

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

  fillEventMenu(objectMenu, null)
  val item = JMenuItem("Разрезать")
  item.addActionListener(object: ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
      val xquantity = enterInt("Введите кол-во изображений по горизонтали:")
      val yquantity = enterInt("Введите кол-во изображений по вертикали:")
      cutImage(currentImageArray!!, xquantity, yquantity)
    }
  })

  imageMenu.add(item)

  val player = Sprite(-3.0, -5.0, 2.0, 2.0)
  player.image = imageArrays.last.images[0]
  sprites.add(player)

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

  val action12 = SpriteSetBounds()
  action12.sprite = player
  action12.settings()
  actions.add(action12)

  val bullet = addClass("Пуля")

  val action6 = SpritePositionAs()
  action6.sprite2 = player
  bullet.onCreate.add(action6)

  val action9 = SpriteSetSize()
  action9.width = 3.5
  action9.height = 0.5
  bullet.onCreate.add(action9)

  val action7 = SpriteDirectAs()
  action7.sprite2 = player
  bullet.onCreate.add(action7)

  val action11 = SpriteSetSpeed()
  action11.speed = 15.0
  bullet.onCreate.add(action11)

  bullet.always.add(SpriteMovement())

  currentImageArray = imageArrays[1]
  val action13 = SpriteAnimation()
  action13.speed = 16.0
  bullet.always.add(action13)

  val action8 = SpriteCreate()
  action8.spriteClass = bullet
  action8.delay = 0.1
  Key(32).onPressActions.add(Pushable.ActionEntry(world, action8))

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
  val actions = listOf(SpriteCreate(), SpritePositionAs(), SpriteSetSize(), SpriteRotation(), SpriteDirectAs(), SpriteMovement(), SpriteSetSpeed(), SpriteAcceleration(), SpriteSetImage(), SpriteAnimation())

  val menu = JMenu(caption)
  for(action in actions) {
    addMenuItem(menu, MenuListener(spriteClass, event, action))
  }

  //addMenuItem(subMenu[1], "Ограничивать", AllMenuListener(SetBounds()))
  return menu
}