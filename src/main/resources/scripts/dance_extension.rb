require "java"

java_import "com.deflatedpickle.faosdance.GlobalValues"
java_import "com.deflatedpickle.faosdance.FAOSDance"
java_import "com.deflatedpickle.faosdance.FAOSDanceSettings"

class DanceExtension
  # An extension for FAOSDance
  def initialize(name, description, author)
    @name = name
    @description = description
    @author = author

    @enabled = false
  end

  # Called when a core value is changed
  def update_value(name, value)
  end

  # Runs after drawing starts
  def pre_draw(graphics)
  end

  # Runs after drawing the sprite, but before before the sprite graphics are disposed
  def during_draw_sprite(sprite_graphics)
  end

  # Runs before the sprite is drawn
  def pre_draw_sprite(sprite_graphics)
  end

  # Runs after the sprite is drawn
  def post_draw_sprite(graphics)
  end

  # Runs before the reflection is drawn
  def pre_draw_reflection(reflection_graphics)
  end

  # Runs after drawing the reflection, but before before the reflection graphics are disposed
  def during_draw_reflection(graphics)
  end

  # Runs after the reflection is drawn
  def post_draw_reflection(graphics)
  end

  # Runs before drawing finishes
  def post_draw(graphics)
  end

  # Adds components to the settings dialog
  def settings(panel)
  end

  # Run when the extension is enabled
  def enable
  end

  # Run when the extension is disabled
  def disable
  end
end