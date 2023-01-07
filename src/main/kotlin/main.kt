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
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import javax.swing.*
import javax.swing.Timer

var showCollisionShapes = false
var showGrid = false

val canvases = mutableListOf<Canvas>()
val imageArrays = mutableListOf<ImageArray>()
var blankImage: Image = Image(Texture(""))

val sounds = mutableListOf<File>()

const val windowHeight = 800
const val windowWidth = windowHeight * 9 / 16
val frame = JFrame("Elasmotherium")

class Project(): Element {
  override fun toNode(node: Node) {
  }
}

val ide = Project()
val user = Project()

fun main() {
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

  editor()

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
  // SCENE

  registerSerializers()

  if(false) {
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
    //exitProcess(0)
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