import mod.Element
import java.util.*

private val idForElement = HashMap<Element, Int>()
private val elementForId = HashMap<Int, Element>()
val toRemove = HashMap<Element, Node>()
private var lastID = 0

class Node(var className: String) {
  val attributes = LinkedHashMap<String, String>()
  val fields = LinkedHashMap<String, Node>()
  val children = mutableListOf<Node>()

  constructor(element: Element): this(element.javaClass.kotlin.simpleName!!)

  private fun setNode(element: Element): Node {
    val id = idForElement[element]
    if(id == null) {
      val node = Node(element)
      element.toNode(node)
      if(element is SpriteAction) node.setField("sprite", element.sprite)
      if(element is SpriteActionFactory) node.setField("entry", element.spriteEntry)
      lastID++
      idForElement[element] = lastID
      node.setInt("id", lastID)
      toRemove[element] = node
      return node
    } else {
      val node = Node("Object")
      node.setInt("id", id)
      toRemove.remove(element)
      return node
    }
  }

  fun getInt(name: String): Int {
    val value = attributes[name]
    return value?.toInt() ?: 0
  }

  fun setInt(name: String, value: Int) {
    attributes[name] = value.toString()
  }

  fun getDouble(name: String): Double {
    val value = attributes[name]
    return value?.toDouble() ?: 0.0
  }

  fun setDouble(name: String, value: Double) {
    attributes[name] = format(value)
  }

  fun getString(name: String): String {
    return attributes[name] ?: ""
  }

  fun setString(name: String, value: String) {
    attributes[name] = value
  }

  fun getFormula(name: String): Formula {
    return doubleToFormula(attributes[name]!!)
  }

  fun setFormula(name: String, element: Formula) {
    attributes[name] = element.toString()
  }

  fun hasField(name: String): Boolean {
    return fields.contains(name)
  }

  fun <T: Element> getField(name: String, list: MutableList<T>) {
    val node = fields[name]!!
    node.getChildren(list)
  }

  fun <T: Element> setField(name: String, list: MutableList<T>) {
    val node = Node("List")
    node.setChildren(list)
    fields[name] = node
  }

  fun getField(name: String): Element {
    return fields[name]!!.createObject()
  }

  fun setField(name: String, element: Element) {
    fields[name] = setNode(element)
  }

  private fun createObject(): Element {
    if(className == "Object") {
      return elementForId[getInt("id")]!!
    }
    var element: Element? = null
    if(className.endsWith("Factory")) {
      val factory = factorySerializers[className]!!.factoryFromNode(this) as SpriteActionFactory
      factory.spriteEntry = getField("spriteEntry") as SpriteEntry
      element = factory
    }
    if(actionSerializers.contains(className)) {
      element = actionSerializers[className]!!.actionFromNode(this)
    }
    if(elementSerializers.contains(className)) {
      element = elementSerializers[className]!!.fromNode(this)
    }
    if(element == null) throw Error()
    if(attributes.contains("id")) {
      elementForId[getInt("id")] = element
    }
    return element
  }

  fun <T: Element> getChildren(list: MutableList<T>) {
    for(node in children) {
      list.add(node.createObject() as T)
    }
  }

  fun <T: Element> setChildren(elements: MutableList<T>) {
    for(element in elements) {
      children.add(setNode(element))
    }
  }

  fun removeAttribute(name: String) {
    attributes.remove(name)
  }

  fun toText(attr: String = ""): String {
    var text = "<${className}$attr"
    for(attribute in attributes) {
      text += " ${attribute.key}=\"${attribute.value}\""
    }
    if(fields.isEmpty() && children.isEmpty()) return "$text/>"

    text += ">"
    for(field in fields) {
      text += field.value.toText(" field=\"${field.key}\"")
    }
    for(node in children) {
      text += node.toText()
    }
    text += "</$className>"
    return text
  }

  override fun toString(): String {
    return className
  }
}