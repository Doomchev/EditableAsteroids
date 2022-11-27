package mod.actions

import Node
import Sprite
import SpriteAction
import SpriteFactory
import frame
import sounds
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.swing.JOptionPane

class SoundPlayFactory(var file: File? = null): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SoundPlayFactory(selectSound())
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SoundPlay(sprite, file!!)
  }

  override fun fullText(): String = "Проиграть звук $file"

  override fun getClassName(): String = "SoundPlayFactory"

  override fun store(node: Node) {
    node.setString("file", file!!.name)
  }
}

class SoundPlay(sprite: Sprite, var file: File): SpriteAction(sprite) {
  override fun execute() {
    val audioInputStream = AudioSystem.getAudioInputStream(file.absoluteFile)
    val clip = AudioSystem.getClip()
    clip.open(audioInputStream)
    clip.start()
  }

  override fun getClassName(): String = "SoundPlay"

  override fun store(node: Node) {
    node.setString("file", file!!.name)
  }
}

fun selectSound(): File {
  val options = Array(sounds.size) {sounds[it]}
  return options[JOptionPane.showOptionDialog(frame, "Выберите звук:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])]
}