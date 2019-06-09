java_import "java.awt.Color"
java_import "java.awt.image.BufferedImage"
java_import "java.awt.AlphaComposite"
java_import "java.awt.event.ActionListener"
java_import "java.awt.GridBagLayout"
java_import "java.awt.GridBagConstraints"

java_import "javax.swing.JButton"
java_import "javax.swing.JCheckBox"
java_import "javax.swing.Timer"

class ColourOverlayExtension < DanceExtension
  def initialize
    super "Colour Overlay", "Applies RGB values to the sprite", "DeflatedPickle"

    @default = 0.0
    @loop_var = 0

    @red = 0
    @green = 0
    @blue = 0
    @alpha = 0.5

    @rainbow_enabled = false
    @red_offset = 2
    @green_offset = 0
    @blue_offset = 4

    @colour = nil
    @coloured_sprite = nil

    # https://krazydad.com/tutorials/makecolors.php
    @frequency = 0.14

    @amplitude = 255 / 2
    @centre = 255 / 2
  end

  def during_draw_sprite(graphics)
    if @rainbow_enabled
      if @loop_var < 42
        @loop_var += 1

        @red = (Math.sin(@frequency * @loop_var + @red_offset) * @amplitude + @centre).round
        @green = (Math.sin(@frequency * @loop_var + @green_offset) * @amplitude + @centre).round
        @blue = (Math.sin(@frequency * @loop_var + @blue_offset) * @amplitude + @centre).round

        @red_widgets.second.value = @red
        @green_widgets.second.value = @green
        @blue_widgets.second.value = @blue
      else
        @loop_var = 0
      end
    end

    create_image
    graphics.drawRenderedImage @coloured_sprite, nil
  end

  def during_draw_reflection(graphics)
    graphics.drawRenderedImage @coloured_sprite, nil
  end

  def settings(panel)
    @red_widgets = FAOSDanceSettings.createOptionInteger panel, "Red:", @default, 255, 0.0
    @red_widgets.third.addChangeListener {|it|
      @red = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      GlobalValues.setOption "colour_overlay-red", @red
    }
    if @enabled
      @red_widgets.third.setValue GlobalValues.getOption("colour_overlay-red")
    end

    @green_widgets = FAOSDanceSettings.createOptionInteger panel, "Green:", @default, 255, 0.0
    @green_widgets.third.addChangeListener {|it|
      @green = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      GlobalValues.setOption "colour_overlay-green", @green
    }
    if @enabled
      @green_widgets.third.setValue GlobalValues.getOption("colour_overlay-green")
    end

    @blue_widgets = FAOSDanceSettings.createOptionInteger panel, "Blue:", @default, 255, 0.0
    @blue_widgets.third.addChangeListener {|it|
      @blue = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      GlobalValues.setOption "colour_overlay-blue", @blue
    }
    if @enabled
      @blue_widgets.third.setValue GlobalValues.getOption("colour_overlay-blue")
    end

    alpha_widgets = FAOSDanceSettings.createOptionDouble panel, "Alpha:", @alpha, 1.0, 0.0
    alpha_widgets.third.addChangeListener {|it|
      @alpha = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
      GlobalValues.setOption "colour_overlay-alpha", @alpha
    }
    if @enabled
      alpha_widgets.third.setValue GlobalValues.getOption("colour_overlay-alpha")
    end

    rainbow_border = FAOSDanceSettings.createBorderPanel panel, "Rainbow", true
    rainbow_border.titleComponent.addActionListener {|it|
      @rainbow_enabled = it.source.to_java(javax::swing::JCheckBox).isSelected
      GlobalValues.setOption "colour_overlay-rainbow-visible", @rainbow_enabled
    }
    if @enabled
      @rainbow_enabled = rainbow_border.titleComponent.to_java(javax::swing::JCheckBox).isSelected
      rainbow_border.titleComponent.setSelected GlobalValues.getOption("filter-box_blur-rainbow-visible")
    end
    rainbow_panel = rainbow_border.panel

    frequency_widgets = FAOSDanceSettings.createOptionDouble(rainbow_panel, "Frequency:", @frequency, 3.0, 0.1)
    frequency_widgets.third.addChangeListener {|it|
      @frequency = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
      GlobalValues.setOption "colour_overlay-rainbow-frequency", @frequency
    }
    if @enabled
      frequency_widgets.third.setValue GlobalValues.getOption("colour_overlay-rainbow-frequency")
    end

    FAOSDanceSettings.createSeparator(rainbow_panel)

    red_offset_widgets = FAOSDanceSettings.createOptionInteger(rainbow_panel, "Red Offset:", @frequency, 8, -8)
    red_offset_widgets.third.addChangeListener {|it|
      @red_offset = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      GlobalValues.setOption "colour_overlay-rainbow-red_offset", @red_offset
      @loop_var = 0
    }
    if @enabled
      frequency_widgets.third.setValue GlobalValues.getOption("colour_overlay-rainbow-red_offset")
    end

    green_offset_widgets = FAOSDanceSettings.createOptionInteger(rainbow_panel, "Green Offset:", @frequency, 8, -8)
    green_offset_widgets.third.addChangeListener {|it|
      @green_offset = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      GlobalValues.setOption "colour_overlay-rainbow-green_offset", @green_offset
      @loop_var = 0
    }
    if @enabled
      green_offset_widgets.third.setValue GlobalValues.getOption("colour_overlay-rainbow-green_offset")
    end

    blue_offset_widgets = FAOSDanceSettings.createOptionInteger(rainbow_panel, "Blue Offset:", @frequency, 8, -8)
    blue_offset_widgets.third.addChangeListener {|it|
      @blue_offset = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      GlobalValues.setOption "colour_overlay-rainbow-blue_offset", @blue_offset
      @loop_var = 0
    }
    if @enabled
      blue_offset_widgets.third.setValue GlobalValues.getOption("colour_overlay-rainbow-blue_offset")
    end

    FAOSDanceSettings.createSeparator(rainbow_panel)

    amplitude_widgets = FAOSDanceSettings.createOptionDouble(rainbow_panel, "Amplitude:", @amplitude, 360.0, 1.0)
    amplitude_widgets.third.addChangeListener {|it|
      @amplitude = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
      GlobalValues.setOption "colour_overlay-rainbow-amplitude", @amplitude
    }
    if @enabled
      amplitude_widgets.third.setValue GlobalValues.getOption("colour_overlay-rainbow-amplitude")
    end

    centre_widgets = FAOSDanceSettings.createOptionDouble(rainbow_panel, "Centre:", @centre, 360.0, 1.0)
    centre_widgets.third.addChangeListener {|it|
      @centre = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
      GlobalValues.setOption "colour_overlay-rainbow-centre", @centre
    }
    if @enabled
      centre_widgets.third.setValue GlobalValues.getOption("colour_overlay-rainbow-centre")
    end
  end

  def enable
    GlobalValues.setOption "colour_overlay-red", @red
    GlobalValues.setOption "colour_overlay-green", @green
    GlobalValues.setOption "colour_overlay-blue", @blue
    GlobalValues.setOption "colour_overlay-alpha", @alpha

    GlobalValues.setOption "colour_overlay-rainbow-visible", @rainbow_enabled
    GlobalValues.setOption "colour_overlay-rainbow-red_offset", @red_offset
    GlobalValues.setOption "colour_overlay-rainbow-green_offset", @green_offset
    GlobalValues.setOption "colour_overlay-rainbow-blue_offset", @blue_offset
    GlobalValues.setOption "colour_overlay-rainbow-frequency", @frequency
    GlobalValues.setOption "colour_overlay-rainbow-amplitude", @amplitude
    GlobalValues.setOption "colour_overlay-rainbow-centre", @centre
  end

  def create_image
    @colour = Color.new @red, @green, @blue

    width = GlobalValues.getSheet.getSpriteWidth
    height = GlobalValues.getSheet.getSpriteHeight

    @coloured_sprite = BufferedImage.new width, height, BufferedImage::TYPE_INT_ARGB
    graphics = @coloured_sprite.createGraphics

    graphics.composite = AlphaComposite.getInstance(AlphaComposite::SRC_OVER, @alpha)
    graphics.drawImage GlobalValues.getSheet.getSpriteMap[GlobalValues.getOption "sprite.animation.action"][GlobalValues.getOption "sprite.animation.frame"], 0, 0, nil
    graphics.setComposite AlphaComposite::SrcAtop
    graphics.setColor @colour
    graphics.fillRect 0, 0, width, height
    graphics.dispose

    GlobalValues.mutableSprite = @coloured_sprite
  end
end

FAOSDance.registerExtension ColourOverlayExtension.new

