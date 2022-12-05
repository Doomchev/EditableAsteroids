import mod.Element
import java.util.*
import kotlin.reflect.full.createInstance



private val ids = HashMap<Element, Int>()
public val toRemove = HashMap<Element, Node>()
private var lastID = 0

class Node(var className: String) {
  val attributes = HashMap<String, String>()
  private val fields = HashMap<String, Node>()
  val children = LinkedList<Node>()

  constructor(element: Element) : this(element.javaClass.kotlin.simpleName!!) {
  }

  private fun createObject(): Element {
    return Class.forName(className).getDeclaredConstructor().newInstance() as Element
  }

  private fun newInstance(element: Element): Element {
    return element.javaClass.kotlin.createInstance()
  }

  private fun setNode(element: Element): Node {
    val id = ids[element]
    if(id == null) {
      val node = Node(element)
      element.toNode(node)
      lastID++
      ids[element] = lastID
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
    attributes[name] = value.toString()
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

  fun <T: Element> getField(name: String, list: LinkedList<T>) {
    val node = fields[name]!!
    node.getChildren(list)
  }

  fun <T: Element> setField(name: String, list: LinkedList<T>) {
    val node = Node("List")
    node.setChildren(list)
    fields[name] = node
  }

  fun getField(name: String): Element {
    val node = fields[name]!!
    val element = node.createObject()
    element.fromNode(node)
    return element
  }

  fun setField(name: String, element: Element) {
    fields[name] = setNode(element)
  }

  fun <T: Element> getChildren(list: LinkedList<T>) {
    for(node in children) {
      val obj = node.createObject() as T
      obj.fromNode(node)
      list.add(obj)
    }
  }

  fun <T: Element> setChildren(elements: LinkedList<T>) {
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
    return "$className"
  }
}