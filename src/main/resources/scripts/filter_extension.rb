java_import "com.jhlabs.image.BoxBlurFilter"

class FilterExtension < DanceExtension
  def initialize
    super "Filter", "Applies different filters to the sprite", "DeflatedPickle"

    @box_blur_enabled = false
    @box_blur_width = 2
    @box_blur_height = 2
    @box_blur_iterations = 3
  end

  def pre_draw_sprite(graphics)
    width = GlobalValues.getSheet.getSpriteWidth
    height = GlobalValues.getSheet.getSpriteHeight

    @sprite = BufferedImage.new width, height, BufferedImage::TYPE_INT_ARGB

    if @box_blur_enabled
      # TODO: Create the filter once and adjust it's values instead
      box_blur_filter = BoxBlurFilter.new @box_blur_width, @box_blur_height, @box_blur_iterations
      box_blur_filter.filter GlobalValues.mutableSprite, @sprite
    end

    GlobalValues.mutableSprite = @sprite
  end

  def settings(panel)
    box_blur_panel = FAOSDanceSettings.createBorderPanel panel, "Box Blur"

    rainbow_checkbox = JCheckBox.new "Visible"
    rainbow_checkbox.addActionListener {|it|
      @box_blur_enabled = it.source.to_java(javax::swing::JCheckBox).isSelected
    }
    box_blur_panel.add rainbow_checkbox
    grid_settings = GridBagConstraints.new
    grid_settings.gridwidth = GridBagConstraints::REMAINDER
    box_blur_panel.getLayout.to_java(java::awt::GridBagLayout).setConstraints rainbow_checkbox, grid_settings

    FAOSDanceSettings.createOptionInteger(box_blur_panel, "Width:", @box_blur_width, 10, 1).third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @box_blur_width = value
      end
    }

    FAOSDanceSettings.createOptionInteger(box_blur_panel, "Height:", @box_blur_height, 10, 1).third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @box_blur_height = value
      end
    }

    FAOSDanceSettings.createOptionInteger(box_blur_panel, "Iterations:", @box_blur_height, 10, 1).third.addChangeListener {|it|
      value = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if value > -1
        @box_blur_iterations = value
      end
    }
  end
end

FAOSDance.registerExtension FilterExtension.new
