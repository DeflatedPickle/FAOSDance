require "java"

java_import "com.deflatedpickle.faosdance.FAOSDance"

class DanceExtension
  # An extension for FAOSDance
  def initialize(name, description)
    @name = name
    @description = description
  end

  # Runs after drawing starts
  def pre_draw(g2d)
  end

  # Runs before the sprite is drawn
  def pre_draw_sprite(g2d)
  end

  # Runs after the sprite is drawn
  def post_draw_sprite(g2d)
  end

  # Runs before the reflection is drawn
  def pre_draw_reflection(g2d)
  end

  # Runs after the reflection is drawn
  def post_draw_reflection(g2d)
  end

  # Runs before drawing finishes
  def post_draw(g2d)
  end
end