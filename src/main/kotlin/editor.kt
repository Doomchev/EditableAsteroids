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
import java.awt.event.MouseEvent
import javax.swing.*

val world = Canvas(0, 0, windowWidth, windowHeight - 100, 10.0, true)
var currentCanvas: Canvas = world
val objectMenu = JPopupMenu()
val imageMenu = JPopupMenu()
val actionMenu = JPopupMenu()
var backgroundColor = Color(9, 44, 84)

val assets = Canvas(0, windowHeight - 100, windowWidth,100, 64.0, true)

val properties = Canvas(0, 0, windowWidth, windowHeight, 1.0, false)

fun editor() {
  world.setZoom(zoom)
  world.update()
  world.setDefaultPosition()
  canvases.add(world)
  currentCanvas = world

  MouseButton(MouseEvent.BUTTON1, ide, world).apply {
    draggingActions.add(resizeSprite)
    draggingActions.add(rotateSprite)
    draggingActions.add(moveSprites)
    draggingActions.add(selectSprites)
    onClickActions.add(selectSprite)
  }

  MouseButton(MouseEvent.BUTTON1, user, world).onClickActions.add(object: Action {
    override fun execute() {
      if(selectedBlock == null) return
      selectedBlock!!.editElement()
    }
  })

  MouseButton(MouseEvent.BUTTON3, ide, world).apply {
    draggingActions.add(createSprite)
    onClickActions.add(showMenu(objectMenu))
  }

  MouseButton(MouseEvent.BUTTON2, ide, world).draggingActions.add(pan)
  Key(127, ide, world).onClickActions.add(deleteSprites)

  mouseWheelUp(ide, world).onClickActions.add(zoomIn)
  mouseWheelDown(ide, world).onClickActions.add(zoomOut)

  world.apply {
    add(grid)
    add(drawScene)
    add(selectSprites)
    add(rotateSprite)
    add(resizeSprite)
    add(drawDefaultCamera)
  }

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
    for(sprite in selectedSprites.reversed()) {
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
    actions.add(SpriteCreate(Sprite(currentImageArray!!.images[0]), selectClass(), mutableListOf()))
  }
  createItem.add(itemCreate)

  val tileMapItem = JMenuItem("Карту")
  tileMapItem.addActionListener {
    project.add(createTileMap())
  }
  createItem.add(tileMapItem)

  Key(118, ide, world).onClickActions.add(restoreCamera())

  Key(103, ide, world).onClickActions.add(object: Action {
    override fun execute() {
      showGrid = !showGrid
    }

    override fun toNode(node: Node) {
      TODO("Not yet implemented")
    }
  })

  Key(99, ide, world).onClickActions.add(object: Action {
    override fun execute() {
      showCollisionShapes = !showCollisionShapes
    }

    override fun toNode(node: Node) {
      TODO("Not yet implemented")
    }
  })

  /// IMAGES GUI

  canvases.add(assets)

  assets.add(drawImages)
  MouseButton(MouseEvent.BUTTON1, ide, assets).onClickActions.add(selectImage)
  MouseButton(MouseEvent.BUTTON3, ide, assets).onClickActions.add(showMenu(imageMenu))

  val itemCutImage = JMenuItem("Разрезать")
  itemCutImage.addActionListener {
    val xquantity = enterInt("Введите кол-во изображений по горизонтали:")
    val yquantity = enterInt("Введите кол-во изображений по вертикали:")
    splitImage(currentImageArray!!, xquantity, yquantity)
  }
  imageMenu.add(itemCutImage)

  val itemSetCenter = JMenuItem("Задать центр")
  itemSetCenter.addActionListener {
    val x = enterDouble("Введите горизонтальное смещение:").getDouble()
    val y = enterDouble("Введите вертикальное смещение:").getDouble()
    currentImageArray!!.setCenter(x, y)
  }
  imageMenu.add(itemSetCenter)

  val itemSetVisArea = JMenuItem("Задать размер обл. вывода")
  itemSetVisArea.addActionListener {
    val xk = enterDouble("Введите коэфф. к ширине:").getDouble()
    val yk = enterDouble("Введите коэфф. к высоте:").getDouble()
    currentImageArray!!.setVisibleArea(xk, yk)
  }
  imageMenu.add(itemSetVisArea)

  // PROPERTIES GUI

  properties.add(drawBlocks)
  canvases.add(properties)
  MouseButton(MouseEvent.BUTTON3, ide, properties).onClickActions.add(showMenu(actionMenu))

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
}