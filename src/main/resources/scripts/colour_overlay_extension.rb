java_import "java.awt.Color"
java_import "javax.swing.JButton"
java_import "java.awt.image.BufferedImage"
java_import "java.awt.AlphaComposite"

class ColourOverlayExtension < DanceExtension
  def initialize
    super "Colour Overlay", "Applies RGB values to the sprite", "DeflatedPickle"

    @default = -1

    @red = 0
    @green = 0
    @blue = 0
    @alpha = 0.5

    @colour = nil
    @coloured_sprite = nil
  end

  def during_draw_sprite(graphics)
    create_image
    graphics.drawRenderedImage @coloured_sprite, nil
  end

  def during_draw_reflection(graphics)
    graphics.drawRenderedImage @coloured_sprite, nil
  end

  def settings(panel)
    red_widgets = FAOSDanceSettings.createOptionInteger panel, "Red:", @default, 255, -1
    red_widgets.third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @red = value
      end
    }

    green_widgets = FAOSDanceSettings.createOptionInteger panel, "Green:", @default, 255, -1
    green_widgets.third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @green = value
      end
    }

    blue_widgets = FAOSDanceSettings.createOptionInteger panel, "Blue:", @default, 255, -1
    blue_widgets.third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @blue = value
      end
    }

    alpha_widgets = FAOSDanceSettings.createOptionDouble panel, "Alpha:", @alpha, 1.0, 0.0
    alpha_widgets.third.addChangeListener {|it|
      @alpha = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
    }
  end

  def disable
    GlobalValues.setZRotation @original
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

