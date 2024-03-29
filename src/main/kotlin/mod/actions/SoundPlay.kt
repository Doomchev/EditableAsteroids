package mod.actions

import Action
import Node
import Serializer
import SpriteAction
import ActionFactory
import frame
import nullSprite
import nullSpriteEntry
import sounds
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import javax.swing.JOptionPane


object soundPlaySerializer: Serializer {
  override fun newFactory(): ActionFactory {
    return SoundPlayFactory(selectSound())
  }

  override fun factoryFromNode(node: Node): ActionFactory {
    return SoundPlayFactory(File(node.getString("file")))
  }

  override fun actionFromNode(node: Node): Action {
    return SoundPlay(File(node.getString("file")))
  }
}

class SoundPlayFactory(private var file: File): ActionFactory() {
  override fun create(): SpriteAction {
    return SoundPlay(file)
  }

  override fun fullText(): String = "Проиграть звук $file"

  override fun toNode(node: Node) {
    node.setString("file", file.name)
  }
}

class SoundPlay(private var file: File): SpriteAction(nullSprite) {
  override fun execute() {
    //return
    val audioInputStream = AudioSystem.getAudioInputStream(file.absoluteFile)
    val clip = AudioSystem.getClip()
    clip.open(audioInputStream)
    val gainControl = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
    gainControl.value = -15.0f
    clip.start()
  }

  override fun toNode(node: Node) {
    node.setString("file", file.name)
  }
}

fun selectSound(): File {
  val options = Array(sounds.size) {sounds[it]}
  return options[JOptionPane.showOptionDialog(frame, "Выберите звук:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])]
}