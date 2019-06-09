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
      if @enabled
        GlobalValues.setOption "filter-box_blur-enabled", @box_blur_enabled
      end
    }
    if @enabled
      @box_blur_enabled = box_blur_border.titleComponent.to_java(javax::swing::JCheckBox).isSelected
      box_blur_border.titleComponent.setSelected GlobalValues.getOption("filter-box_blur-enabled")
    end
    box_blur_panel = box_blur_border.panel

    box_blur_width_widgets = FAOSDanceSettings.createOptionInteger(box_blur_panel, "Width:", 2, 10, 1)
    box_blur_width_widgets.third.addChangeListener {|it|
      @box_blur_filter.setHRadius it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if @enabled
        GlobalValues.setOption "filter-box_blur-radius-horizontal", @box_blur_filter.getHRadius
      end
    }
    if @enabled
      box_blur_width_widgets.third.setValue GlobalValues.getOption("filter-box_blur-radius-horizontal")
    end

    box_blur_height_widgets = FAOSDanceSettings.createOptionInteger(box_blur_panel, "Height:", 2, 10, 1)
    box_blur_height_widgets.third.addChangeListener {|it|
      @box_blur_filter.setVRadius it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if @enabled
        GlobalValues.setOption "filter-box_blur-radius-vertical", @box_blur_filter.getVRadius
      end
    }
    if @enabled
      box_blur_height_widgets.third.setValue GlobalValues.getOption("filter-box_blur-radius-vertical")
    end

    box_blur_iterations_widgets = FAOSDanceSettings.createOptionInteger(box_blur_panel, "Iterations:", 3, 10, 1)
    box_blur_iterations_widgets.third.addChangeListener {|it|
      @box_blur_filter.setIterations it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if @enabled
        GlobalValues.setOption "filter-box_blur-iterations", @box_blur_filter.getIterations
      end
    }
    if @enabled
      box_blur_iterations_widgets.third.setValue GlobalValues.getOption("filter-box_blur-iterations")
    end
  end

  def enable
    GlobalValues.setOption "filter-box_blur-enabled", @box_blur_enabled
    GlobalValues.setOption "filter-box_blur-radius-horizontal", @box_blur_filter.getHRadius
    GlobalValues.setOption "filter-box_blur-radius-vertical", @box_blur_filter.getVRadius
    GlobalValues.setOption "filter-box_blur-iterations", @box_blur_filter.getIterations
  end
end

FAOSDance.registerExtension FilterExtension.new
