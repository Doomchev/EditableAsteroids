package mod.actions

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

  override fun toString(): String = "Проиграть звук"
}

class SoundPlay(sprite: Sprite, var file: File): SpriteAction(sprite) {
  override fun execute() {
    val audioInputStream = AudioSystem.getAudioInputStream(file.absoluteFile)
    val clip = AudioSystem.getClip()
    clip.open(audioInputStream)
    clip.start()
  }
}

fun selectSound(): File {
  val options = Array(sounds.size) {sounds[it]}
  return options[JOptionPane.showOptionDialog(frame, "Выберите звук:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])]
}