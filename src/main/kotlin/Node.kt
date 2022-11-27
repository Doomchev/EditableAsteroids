import mod.dragging.Element
import mod.dragging.project
import java.util.LinkedList

//class Attribute(var name:String, var value: String)

fun store() {
  val root = Node("world")
  val node = Node("scene")
  project.store(node)
  root.setField("scene", project)
}

class Node(var className: String) {
  val attributes = HashMap<String, String>()
  val fields = HashMap<String, Node>()
  val children = LinkedList<Node>()

  fun getInt(name: String): Int {
    return if(attributes.contains(name)) attributes[name]!!.toInt() else 0
  }
  fun setInt(name: String, value: Int) {
    attributes[name] = value.toString()
  }

  fun getDouble(name: String): Double {
    return if(attributes.contains(name)) attributes[name]!!.toDouble() else 0.0
  }
  fun setDouble(name: String, value: Double) {
    attributes[name] = value.toString()
  }

  fun getString(name: String): String {
    return if(attributes.contains(name)) attributes[name]!! else ""
  }
  fun setString(name: String, value: String) {
    attributes[name] = value
  }

  fun setObject(name: String, element: Element) {
    val node = Node(element.getClassName())
    element.store(node)
    fields[name] = node
  }

  fun setFormula(name: String, element: Formula) {
    attributes[name] = element.toString()
  }

  fun <T: Element> setField(name: String, list: LinkedList<T>) {
    val node = Node("List")
    //element.store(node)
    fields[name] = node
  }

  fun <T: Element> setChildren(element: LinkedList<T>) {

  }

  /*fun getField(name: String): Element {
    return fields[name]
  }*/
  fun setField(name: String, element: Element) {
    val node = Node(element.getClassName())
    element.store(node)
    fields[name] = node
  }
}