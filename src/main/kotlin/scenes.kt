import mod.*
import mod.actions.*
import mod.actions.list.ClearList
import mod.actions.list.IsListEmpty
import mod.actions.sprite.*
import mod.actions.state.*
import mod.actions.variables.*
import kotlin.math.*

fun tilemap() {
  splitImage(imageArrays[4], 5, 7)
  project.add(TileMap(10, 16, 1.0, 1.0, imageArrays[4]))
}

fun snow() {
  val flake = addClass("Снежинка")
  val flakeImage = addClassImage("snowflake.png")

  val area = SpriteEntry("snow gen", Sprite(blankImage, 0.0, -10.0, 10.0, 2.0))
  flake.onCreate.apply {
    add(SpritePositionInAreaFactory(currentEntry, area))
    add(SpriteSetSizeFactory(currentEntry, RandomDoubleValue(0.25, 1.0)))
    add(SpriteSetImageFactory(currentEntry, flakeImage.images[0]))
    add(SpriteSetMovingVectorFactory(currentEntry, zero, RandomDoubleValue(1.0, 5.0)))
  }
  flake.always.add(SpriteMoveForwardFactory(currentEntry))

  val bounds = SpriteEntry("границы поля", Sprite(blankImage, world.centerX, world.centerY, world.width + 3.0,world.height + 3.0))

  flake.always.add(SpriteSetBoundsFactory(currentEntry, bounds))

  project.add(flake)
  actions.add(SpriteDelayedCreate(nullSprite, flake, 0.05))
}

fun starfield() {
  val star = addClass("Звезда")
  val flakeImage = addClassImage("snowflake.png")

  val area = SpriteEntry("snow gen", Sprite(blankImage, 0.0, -10.0, 10.0, 2.0))
  star.onCreate.apply {
    add(SpritePositionInAreaFactory(currentEntry, area))
    add(SpriteSetSizeFactory(currentEntry, RandomDoubleValue(0.25, 1.0)))
    add(SpriteSetImageFactory(currentEntry, flakeImage.images[0]))
    add(SpriteSetMovingVectorFactory(currentEntry, zero, RandomDoubleValue(1.0, 5.0)))
  }
  star.always.add(SpriteMoveForwardFactory(currentEntry))

  val bounds = SpriteEntry("границы поля", Sprite(blankImage, world.centerX, world.centerY, world.width + 3.0,world.height + 3.0))

  star.always.add(SpriteSetBoundsFactory(currentEntry, bounds))

  project.add(star)
  actions.add(SpriteDelayedCreate(nullSprite, star, 0.05))
}

fun asteroids() {

  /// IMAGES

  val asteroidImage = addClassImage("asteroid.png")
  splitImage(asteroidImage, 8, 4)
  asteroidImage.setVisibleArea(1.25, 1.25)

  val bulletImage = addClassImage("bullet.png")
  splitImage(bulletImage, 1, 16)
  bulletImage.setCenter(43.0 / 48.0, 5.5 / 12.0)
  bulletImage.setVisibleArea(10.5, 3.0)

  val shipImage = addClassImage("ship.png")
  shipImage.setCenter(0.35, 0.5)
  shipImage.setVisibleArea(1.5, 1.5)

  val explosionImage = addClassImage("explosion.png")
  splitImage(explosionImage, 4, 4)
  explosionImage.setVisibleArea(1.5, 1.5)

  val flameImage = addClassImage("flame.png")
  splitImage(flameImage, 3, 3)
  flameImage.setCenter(0.5, 0.2)

  // global variables

  val startingLives = IntVariable("начальное кол-во жизней", 3)

  val acceleration = DoubleVariable("ускорение",25.0)
  val accelerationLimit = DoubleVariable("предел ускорения",7.5)
  val deceleration = DoubleVariable("замедление", 15.0)
  val maxSpeed = DoubleVariable("макс скорость", 3.0)
  val rotationSpeed = DoubleVariable("скорость вращения", 1.5)

  val bulletSpeed = DoubleVariable("скорость пули",15.0)
  val bulletDelay = DoubleVariable("интервал между пулями", 0.15)

  // HUD

  val score = IntVariable("очки", 0)
  val lives = IntVariable("жизни", startingLives.getInt(), "d")
  val level = IntVariable("уровень", 0)

  val scoreDisplay = Label(score, 0.0, world.topY + 0.5, world.width - 1.0, 1.0, HorizontalAlign.left)
  val levelDisplay = Label(level, 0.0, world.topY + 0.5, world.width - 1.0, 1.0, HorizontalAlign.center, VerticalAlign.center, "УРОВЕНЬ ", false)
  val livesDisplay = Label(lives, 0.0, world.topY + 0.5, world.width - 1.0, 1.0, HorizontalAlign.right)
  val spaceDisplay = Label(null, 0.0, 0.0, world.width, 1.0, HorizontalAlign.center, VerticalAlign.center, "НАЖМИТЕ ПРОБЕЛ")
  spaceDisplay.visible = false
  val gameOverDisplay = Label(null, 0.0, 0.0, world.width, 1.0, HorizontalAlign.center, VerticalAlign.center, "ИГРА ОКОНЧЕНА")
  gameOverDisplay.visible = false

  /// SPRITES

  val player = addClass("Игрок")
  val playerSprite = Sprite(shipImage.images[0], 0.0, 0.0, 1.0, 1.0)
  playerSprite.setName("игрок")
  player.sprites.add(playerSprite)
  val start = Sprite(blankImage, 0.0, 0.0, 1.0, 1.0)

  val alive = newState("живой")
  val destroyed = newState("уничтожен")
  val gameOver = newState("игра окончена")
  playerSprite.state = alive

  val flame = Sprite(flameImage.images[0], -1.0, 0.0, 1.0, 1.0, -90.0)
  playerSprite.setName("пламя")
  flame.visible = false
  addConstraint(flame, playerSprite)
  actions.add(SpriteAnimation(flame, flameImage, 16.0, 0.0))

  val gun = Sprite(blankImage, 1.0, 0.0)
  val gunEntry = SpriteEntry("дуло", gun)
  addConstraint(gun, playerSprite)

  Key(97, user, world).onPressActions.add(SpriteRotation(playerSprite, -rotationSpeed.getDouble() * PI))
  Key(100, user, world).onPressActions.add(SpriteRotation(playerSprite, rotationSpeed.getDouble() * PI))
  val forward: Key = Key(119, user, world)
  forward.onPressActions.apply {
    add(SpriteAcceleration(playerSprite, acceleration.getDouble(), accelerationLimit.getDouble()))
    add(IfState(playerSprite, mutableListOf(alive), mutableListOf(SpriteShow(flame))))
  }
  forward.onUnpressActions.add(SpriteHide(flame))

  val bounds = SpriteEntry("границы поля", Sprite(blankImage, world.centerX, world.centerY, world.width + 3.0,world.height + 3.0))

  val asteroid = addClass("Астероид")
  val big = newState("большой")
  val medium = newState("средний")
  val small = newState("маленький")
  val asteroidArea = SpriteEntry("зона появления астероидов", Sprite(blankImage, world.centerX, world.topY - 1.5, world.width + 3.0,0.01))

  actions.apply {
    add(SpriteAcceleration(playerSprite, -deceleration.getDouble(), 100.0))
    add(SpriteLoopArea(playerSprite, bounds.sprite!!))
    add(SpriteMoveForward(playerSprite))
    add(IsListEmpty(asteroid, mutableListOf(
      VariableAddFactory("уровень", 1),
      RepeatFactory(level,
        SpriteCreateFactory(currentEntry, asteroid, mutableListOf(
          SpriteSetStateFactory(currentEntry, big),
          SpriteSetSizeFactory(currentEntry, DoubleValue(3.0)),
          SpriteSetAngleFactory(currentEntry, RandomDirection(RandomDoubleValue(45.0, 135.0))),
          SpriteSetSpeedFactory(currentEntry, RandomDoubleValue(2.0, 3.0)),
          SpritePositionInAreaFactory(currentEntry, asteroidArea)
      )))
    )))
  }

  val bullet = addClass("Пуля")

  bullet.onCreate.apply {
    add(SoundPlayFactory(sounds[0]))
    add(SpritePositionAsFactory(currentEntry, gunEntry))
    add(SpriteDirectAsFactory(currentEntry, parentEntry))
    add(SpriteSetSizeFactory(currentEntry, DoubleValue(0.15)))
    add(SpriteSetSpeedFactory(currentEntry, bulletSpeed))
  }
  bullet.always.apply {
    add(SpriteMoveForwardFactory(currentEntry))
    add(SpriteAnimationFactory(currentEntry, bulletImage, DoubleValue(16.0)))
    add(SpriteSetBoundsFactory(currentEntry, bounds))
  }

  val space = Key(32, user, world)
  space.onPressActions.apply {
    add(IfState(playerSprite, mutableListOf(alive), mutableListOf(
      SpriteDelayedCreate(playerSprite, bullet, bulletDelay.getDouble()))
    ))
  }
  space.onClickActions.apply {
    add(
      IfState(playerSprite, mutableListOf(gameOver), mutableListOf(
        SpriteHide(gameOverDisplay)
      , VariableSet("жизни", startingLives.getInt())
      , VariableSet("уровень", 0)
      , ClearList(asteroid)
      , SpriteSetState(playerSprite, alive)
      , SpriteShow(playerSprite)
      , SpriteActivate(playerSprite)
    )))
    add(
      IfState(playerSprite, mutableListOf(destroyed), mutableListOf(
        SpritePositionAs(playerSprite, start)
      , SpriteSetState(playerSprite, alive)
      , SpriteHide(spaceDisplay)
      , SpriteShow(playerSprite)
      , SpriteActivate(playerSprite)
      , VariableAdd("жизни", -1)
    ))
    )
  }

  asteroid.always.apply {
    add(SpriteAnimationFactory(currentEntry, asteroidImage, RandomDirection(RandomDoubleValue(12.0, 20.0))))
    add(SpriteRotationFactory(currentEntry, RandomDoubleValue(-180.0, 180.0)))
    add(SpriteLoopAreaFactory(currentEntry, bounds))
    add(SpriteMoveForwardFactory(currentEntry))
  }

  val explosion = addClass("Взрыв")

  explosion.onCreate.apply {
    add(SoundPlayFactory(sounds[1]))
    add(SpritePositionAsFactory(currentEntry, parentEntry))
  }
  
  explosion.always.apply {
    add(SpriteAnimationFactory(currentEntry, explosionImage, DoubleValue(16.0)))
    add(SpriteDelayedRemoveFactory(currentEntry, DoubleValue(1.0)))
  }

  bullet.addOnCollision(asteroid
    /*, SpriteCreateFactory(sprite1Entry, explosion
      , SpriteSetSizeFactory(currentEntry, DoubleValue(1.0)))*/
    , IfStateFactory(sprite2Entry, big
      , SpriteCreateFactory(sprite2Entry, explosion
        , SpriteSetSizeFactory(currentEntry, DoubleValue(2.5)))
      , SpriteCreateFactory(sprite2Entry, asteroid
        , SpriteSetStateFactory(currentEntry, medium)
        , SpritePositionAsFactory(currentEntry, sprite2Entry)
        , SpriteSetSizeFactory(currentEntry, DoubleValue(1.75))
        , SpriteDirectToFactory(currentEntry, sprite1Entry)
        , SpriteTurnFactory(currentEntry, RandomDoubleValue(150.0, 210.0))
        , SpriteSetSpeedFactory(currentEntry, RandomDoubleValue(3.0, 5.0))
        , VariableAddFactory("очки", 100)
      ))

    , IfStateFactory(sprite2Entry, medium
      , VariableAddFactory("очки", 200)
    )

    , IfStateFactory(sprite2Entry, mutableListOf(big, medium)
      , SpriteCreateFactory(sprite2Entry, explosion
        , SpriteSetSizeFactory(currentEntry, DoubleValue(1.0)))
      , SpriteCreateFactory(sprite2Entry, asteroid
        , SpriteSetStateFactory(currentEntry, small)
        , SpritePositionAsFactory(currentEntry, sprite2Entry)
        , SpriteSetSizeFactory(currentEntry, DoubleValue(1.0))
        , SpriteDirectToFactory(currentEntry, sprite1Entry)
        , SpriteTurnFactory(currentEntry, RandomDoubleValue(210.0, 270.0))
        , SpriteSetSpeedFactory(currentEntry, RandomDoubleValue(5.0, 7.0)))

      , SpriteCreateFactory(sprite2Entry, asteroid
        , SpriteSetStateFactory(currentEntry, small)
        , SpritePositionAsFactory(currentEntry, sprite2Entry)
        , SpriteSetSizeFactory(currentEntry, DoubleValue(1.0))
        , SpriteDirectToFactory(currentEntry, sprite1Entry)
        , SpriteTurnFactory(currentEntry, RandomDoubleValue(90.0, 150.0))
        , SpriteSetSpeedFactory(currentEntry, RandomDoubleValue(5.0, 7.0))))

    , IfStateFactory(sprite2Entry, small
      , SpriteCreateFactory(sprite2Entry, explosion
        , SpriteSetSizeFactory(currentEntry, DoubleValue(1.0))
        , VariableAddFactory("очки", 300)
      ))

    , SpriteRemoveFactory(sprite1Entry)

    , SpriteDeactivateFactory(sprite2Entry)
    , SpriteRemoveFactory(sprite2Entry)
  )

  player.addOnCollision(asteroid
    , IfStateFactory(sprite1Entry, alive
      , SpriteCreateFactory(sprite1Entry, explosion
        , SpriteSetSizeFactory(currentEntry, DoubleValue(2.5)))
      , SpriteHideFactory(sprite1Entry)
      , SpriteHideFactory(SpriteEntry("пламя", flame))
      , SpriteDeactivateFactory(sprite1Entry)
      , SpriteSetStateFactory(sprite1Entry, destroyed)
      , SpriteShowFactory(SpriteEntry("пробел", spaceDisplay))
      , VariableIfEqualFactory("жизни", IntValue(0), mutableListOf(
          SpriteHideFactory(SpriteEntry("пробел", spaceDisplay))
        , SpriteShowFactory(SpriteEntry("игра окончена", gameOverDisplay))
        , SpriteSetStateFactory(SpriteEntry("игрок", playerSprite), gameOver)
      ))
    )
  )
  
  // Elements

  project.apply {
    add(bounds.sprite!!)
    add(asteroidArea.sprite!!)
    add(bullet)
    add(asteroid)
    add(flame)
    add(player)
    add(explosion)
    add(scoreDisplay)
    add(levelDisplay)
    add(livesDisplay)
    add(gameOverDisplay)
    add(spaceDisplay)
  }
}