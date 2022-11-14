import mod.actions.*
import mod.actions.sprite.*
import mod.dragging.*
import mod.drawing.drawImages
import mod.drawing.drawSprites
import java.awt.event.MouseEvent.BUTTON1
import java.awt.event.MouseEvent.BUTTON3
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.Timer
import javax.swing.event.MenuListener
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
  cutSprite(currentImageArray!!, 8, 4)

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
  //addMenuItem(imageMenu, "Разрезать", MenuEventListener(cutImage))

  val sprite = Sprite(0.0, 0.0, 2.0, 2.0)
  sprite.image = imageArrays.last.images[0]
  sprites.add(sprite)

  val action1 = SpriteRotation()
  action1.sprite = sprite
  action1.speed = -1.5 * PI
  Key(97).onPressActions.add(Pushable.ActionEntry(world, action1))

  val action2 = SpriteRotation()
  action2.sprite = sprite
  action2.speed = 1.5 * PI
  Key(100).onPressActions.add(Pushable.ActionEntry(world, action2))

  val action3 = SpriteAcceleration()
  action3.sprite = sprite
  action3.acceleration = 50.0
  action3.limit = 10.0
  Key(119).onPressActions.add(Pushable.ActionEntry(world, action3))

  val action4 = SpriteMovement()
  action4.sprite = sprite
  actions.add(action4)

  val action5 = SpriteAcceleration()
  action5.sprite = sprite
  action5.acceleration = -15.0
  actions.add(action5)

  frame.isVisible = true
}

val menuOnClick = 0
val menuOnPress = 1
val menuAlways = 2

fun fillEventMenu(menu: JPopupMenu, spriteClass: SpriteClass?) {
  fillActionMenu(menu, "При клике...", spriteClass, menuOnClick)
  fillActionMenu(menu, "При нажатии...", spriteClass, menuOnPress)
  fillActionMenu(menu, "Всегда...", spriteClass, menuAlways)
}
fun fillActionMenu(parentMenu: JPopupMenu, caption: String, spriteClass: SpriteClass?
                   , eventNumber: Int) {
  val menu = JMenu(caption)
  addMenuItem(menu, MenuListener(spriteClass, eventNumber
    , SpriteRotation()))
  addMenuItem(menu, MenuListener(spriteClass, eventNumber
    , SpriteAnimation()))
  addMenuItem(menu, MenuListener(spriteClass, eventNumber
    , SpriteAcceleration()))
  addMenuItem(menu, MenuListener(spriteClass, eventNumber
    , SpriteMovement()))

  //addMenuItem(subMenu[1], "Ограничивать", AllMenuListener(SetBounds()))
  parentMenu.add(menu)
}