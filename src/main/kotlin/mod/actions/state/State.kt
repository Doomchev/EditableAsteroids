package mod.actions.state

import frame
import mod.project
import javax.swing.JOptionPane

val nullState = newState("")

class State(val name: String) {
  override fun toString(): String {
    return name
  }
}

fun newState(name: String): State {
  val state = State(name)
  project.states.add(state)
  return state
}

fun selectState(message:String = "Выберите состояние:"): State {
  val array = Array(project.states.size) { project.states[it]}
  return array[JOptionPane.showOptionDialog(frame, message, "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, array, project.states.first())]
}

fun findState(name: String): State {
  for(state in project.states) if(state.name == name) return state
  return newState(name)
}

fun findStates(names: String): MutableList<State> {
  val states = mutableListOf<State>()
  for(name in names.split(",")) {
    states.add(findState(name))
  }
  return states
}