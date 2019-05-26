java_import "java.awt.Color"
java_import "java.awt.image.BufferedImage"
java_import "java.awt.AlphaComposite"
java_import "java.awt.event.ActionListener"

java_import "javax.swing.JButton"
java_import "javax.swing.JCheckBox"
java_import "javax.swing.Timer"

class ColourOverlayExtension < DanceExtension
  def initialize
    super "Colour Overlay", "Applies RGB values to the sprite", "DeflatedPickle"

    @default = -1

    @red = 0
    @green = 0
    @blue = 0
    @alpha = 0.5

    @rainbow = false

    @colour = nil
    @coloured_sprite = nil

    frequency = 0.14
    loop_var = 0
    @timer = Timer.new(GlobalValues.fps) {|_|
      if loop_var < 32
        loop_var += 1

        @red = (Math.sin(frequency * loop_var) * 127 + 128).round
        @green = (Math.sin(frequency * loop_var + 2) * 127 + 128).round
        @blue = (Math.sin(frequency * loop_var + 4) * 127 + 128).round

        @red_widgets.second.value = @red
        @green_widgets.second.value = @green
        @blue_widgets.second.value = @blue
      else
        loop_var = 0
      end
    }
  end

  def during_draw_sprite(graphics)
    # TODO: Cache coloured versions of all the animation frames and use those instead of new ones every frame
    create_image
    graphics.drawRenderedImage @coloured_sprite, nil
  end

  def during_draw_reflection(graphics)
    graphics.drawRenderedImage @coloured_sprite, nil
  end

  def settings(panel)
    @red_widgets = FAOSDanceSettings.createOptionInteger panel, "Red:", @default, 255, -1
    @red_widgets.third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @red = value
      end
    }

    @green_widgets = FAOSDanceSettings.createOptionInteger panel, "Green:", @default, 255, -1
    @green_widgets.third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @green = value
      end
    }

    @blue_widgets = FAOSDanceSettings.createOptionInteger panel, "Blue:", @default, 255, -1
    @blue_widgets.third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @blue = value
      end
    }

    alpha_widgets = FAOSDanceSettings.createOptionDouble panel, "Alpha:", @alpha, 1.0, 0.0
    alpha_widgets.third.addChangeListener {|it|
      @alpha = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
    }

    rainbow_checkbox = JCheckBox.new "Rainbow"
    rainbow_checkbox.addActionListener {|it|
      @rainbow = it

      if it
        @timer.start
      else
        @timer.stop
      end
    }
    panel.add rainbow_checkbox
  end

  def disable
    @timer.stop
  end

  def create_image
    @colour = Color.new @red, @green, @blue

    width = GlobalValues.getSheet.getSpriteWidth
    height = GlobalValues.getSheet.getSpriteHeight

    @coloured_sprite = BufferedImage.new width, height, BufferedImage::TYPE_INT_ARGB
    graphics = @coloured_sprite.createGraphics

    graphics.composite = AlphaComposite.getInstance(AlphaComposite::SRC_OVER, @alpha)
    graphics.drawImage GlobalValues.getSheet.getSpriteMap[GlobalValues.getCurrentAction][GlobalValues.getAnimFrame], 0, 0, nil
    graphics.setComposite AlphaComposite::SrcAtop
    graphics.setColor @colour
    graphics.fillRect 0, 0, width, height
    graphics.dispose
  end
end

FAOSDance.registerExtension ColourOverlayExtension.new

