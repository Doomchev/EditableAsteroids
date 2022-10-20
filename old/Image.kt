class Image(
    var texture: Texture,
    var textureX: Double,
    var textureY: Double,
    var textureWidth: Double,
    var textureHeight: Double
) {
    fun draw(
        x: Double,
        y: Double,
        screenWidth: Double,
        height: Double
    ) {
        texture.draw(textureX, textureY, textureWidth, textureHeight)
    }
}
