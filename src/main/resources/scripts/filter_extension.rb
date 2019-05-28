java_import "com.jhlabs.image.BoxBlurFilter"

class FilterExtension < DanceExtension
  def initialize
    super "Filter", "Applies different filters to the sprite", "DeflatedPickle"

    @box_blur_enabled = false
    @box_blur_filter = BoxBlurFilter.new 2, 2, 3
  end

  def pre_draw_sprite(graphics)
    width = GlobalValues.getSheet.getSpriteWidth
    height = GlobalValues.getSheet.getSpriteHeight

    @sprite = BufferedImage.new width, height, BufferedImage::TYPE_INT_ARGB

    if @box_blur_enabled
      # TODO: Create the filter once and adjust it's values instead
      @box_blur_filter.filter GlobalValues.mutableSprite, @sprite
    end

    GlobalValues.mutableSprite = @sprite
  end

  def settings(panel)
    box_blur_border = FAOSDanceSettings.createBorderPanel panel, "Box Blur", true
    box_blur_border.titleComponent.addActionListener {|it|
      @box_blur_enabled = it.source.to_java(javax::swing::JCheckBox).isSelected
    }

    box_blur_panel = box_blur_border.panel

    FAOSDanceSettings.createOptionInteger(box_blur_panel, "Width:", 2, 10, 1).third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @box_blur_filter.setHRadius value
      end
    }

    FAOSDanceSettings.createOptionInteger(box_blur_panel, "Height:", 2, 10, 1).third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @box_blur_filter.setVRadius value
      end
    }

    FAOSDanceSettings.createOptionInteger(box_blur_panel, "Iterations:", 3, 10, 1).third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @box_blur_filter.setIterations value
      end
    }
  end
end

FAOSDance.registerExtension FilterExtension.new
