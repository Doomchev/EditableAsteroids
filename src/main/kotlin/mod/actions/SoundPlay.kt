package mod.actions

import Action
import Sprite
import SpriteAction
import mod.actions.sprite.SpriteDirectAs
import mod.dragging.selectedSprites
import sounds
import javax.sound.sampled.AudioSystem

class SoundPlay: SpriteAction() {
  override fun create(sprite: Sprite?): SpriteAction {
    return SoundPlay()
  }

  override fun settings() {
  }

  override fun execute() {
    val file = sounds[0]
    val audioInputStream = AudioSystem.getAudioInputStream(file.absoluteFile)
    val clip = AudioSystem.getClip()
    clip.open(audioInputStream)
    clip.start()
  }

  override fun toString(): String = "Проиграть звук"
}