class ShakeExtension < DanceExtension
  def initialize
    super "Shake", "Shakes the sprite in random directions", "DeflatedPickle"

    @amount = 1
    @speed_max_x = 1.6
    @speed_max_y = 1.6

    @speed_min_x = 1.0
    @speed_min_y = 1.0

    @x = @speed_max_x
    @y = @speed_max_y

    @counter = @amount

    @random_bool = [true, false]
  end

  def pre_draw_sprite(graphics)
    if @counter == 0
      @counter = @amount

      @x = @speed_max_x
      @y = @speed_max_y
    else
      if @x < 0
        @x *= rand @speed_min_x..@speed_max_x
      end

      if @random_bool.sample
        @x = -@x
      end

      if @y < 0
        @y *= rand @speed_min_y..@speed_max_y
      end

      if @random_bool.sample
        @y = -@y
      end

      graphics.translate @x, @y

      @counter -= 1
    end
  end

  def pre_draw_reflection(graphics)
    graphics.translate @x, @y
  end

  def settings(panel)
    shake_amount_widgets = FAOSDanceSettings.createOptionInteger(panel, "Shake Amount:", @amount, 42, 1)

    grid_settings = GridBagConstraints.new GridBagConstraints::RELATIVE, GridBagConstraints::RELATIVE,
                                           2, 1,
                                           1.0, 0.0,
                                           GridBagConstraints::CENTER, GridBagConstraints::HORIZONTAL,
                                           Insets.new(0, 0, 0, 0),
                                           0, 0
    panel.getLayout.setConstraints shake_amount_widgets.second, grid_settings

    shake_amount_widgets.third.addChangeListener {|it|
      @amount = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if @enabled
        GlobalValues.setOption "shake-amount", @amount
      end
    }
    if @enabled
      shake_amount_widgets.third.setValue GlobalValues.getOption("shake-amount")
    end

    x_speed_widgets = FAOSDanceSettings.createOptionRangeDouble(panel, "X Speed:", @speed_max_x, @speed_min_x, 3.0, 0.0)
    x_speed_widgets.second.addChangeListener {|it|
      @speed_max_x = it.source.getDoubleHighValue
      @speed_min_x = it.source.getDoubleLowValue

      if @enabled
        GlobalValues.setOption "shake-speed_max_x", @speed_max_x
        GlobalValues.setOption "shake-speed_min_x", @speed_min_x
      end
    }
    if @enabled
      x_speed_widgets.second.setDoubleHighValue GlobalValues.getOption("shake-speed_max_x")
      x_speed_widgets.second.setDoubleLowValue GlobalValues.getOption("shake-speed_min_x")
    end

    y_speed_widgets = FAOSDanceSettings.createOptionRangeDouble(panel, "Y Speed:", @speed_max_y, @speed_min_y, 3.0, 0.0)
    y_speed_widgets.second.addChangeListener {|it|
      @speed_max_y = it.source.getDoubleHighValue
      @speed_min_y = it.source.getDoubleLowValue

      if @enabled
        GlobalValues.setOption "shake-speed_max_y", @speed_max_y
        GlobalValues.setOption "shake-speed_min_y", @speed_min_y
      end
    }
    if @enabled
      y_speed_widgets.second.setDoubleHighValue GlobalValues.getOption("shake-speed_max_y")
      y_speed_widgets.second.setDoubleLowValue GlobalValues.getOption("shake-speed_min_y")
    end
  end

  def enable
    GlobalValues.setOption "shake-amount", @amount
    GlobalValues.setOption "shake-speed_max_x", @speed_max_x
    GlobalValues.setOption "shake-speed_min_x", @speed_min_x
    GlobalValues.setOption "shake-speed_max_y", @speed_max_y
    GlobalValues.setOption "shake-speed_min_y", @speed_min_y
  end
end

FAOSDance.registerExtension ShakeExtension.new
