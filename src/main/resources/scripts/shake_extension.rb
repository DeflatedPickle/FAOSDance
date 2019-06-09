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

  def pre_draw(graphics)
    if @counter == 0
      @counter = @amount

      @x = @speed_max_x
      @y = @speed_max_y

      # TODO: Translate the graphics instead of the window
      GlobalValues.getFrame.setLocation @original_location.x, @original_location.y
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

      GlobalValues.getFrame.setLocation @original_location.x + @x, @original_location.y + @y

      @counter -= 1
    end
  end

  def settings(panel)
    shake_amount_widgets = FAOSDanceSettings.createOptionInteger(panel, "Shake Amount:", @amount, 42, 1)
    shake_amount_widgets.third.addChangeListener {|it|
      @amount = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if @enabled
        GlobalValues.setOption "shake-amount", @amount
      end
    }
    if @enabled
      shake_amount_widgets.third.setValue GlobalValues.getOption("shake-amount")
    end

    max_x_speed_widgets = FAOSDanceSettings.createOptionDouble(panel, "Max X Speed:", @speed_max_x, 3.0, 0.0)
    max_x_speed_widgets.third.addChangeListener {|it|
      @speed_max_x = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
      if @enabled
        GlobalValues.setOption "shake-speed_max_x", @speed_max_x
      end
    }
    if @enabled
      max_x_speed_widgets.third.setValue GlobalValues.getOption("shake-speed_max_x")
    end
    min_x_speed_widgets = FAOSDanceSettings.createOptionDouble(panel, "Min X Speed:", @speed_min_x, 3.0, 0.0)
    min_x_speed_widgets.third.addChangeListener {|it|
      @speed_min_x = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
      if @enabled
        GlobalValues.setOption "shake-speed_min_x", @speed_min_x
      end
    }
    if @enabled
      min_x_speed_widgets.third.setValue GlobalValues.getOption("shake-speed_min_x")
    end

    max_y_speed_widgets = FAOSDanceSettings.createOptionDouble(panel, "Max Y Speed:", @speed_max_y, 3.0, 0.0)
    max_y_speed_widgets.third.addChangeListener {|it|
      @speed_max_y = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
      if @enabled
        GlobalValues.setOption "shake-speed_max_y", @speed_max_y
      end
    }
    if @enabled
      max_y_speed_widgets.third.setValue GlobalValues.getOption("shake-speed_max_y")
    end
    min_y_speed_widgets = FAOSDanceSettings.createOptionDouble(panel, "Min Y Speed:", @speed_min_y, 3.0, 0.0)
    min_y_speed_widgets.third.addChangeListener {|it|
      @speed_min_y = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
      if @enabled
        GlobalValues.setOption "shake-speed_min_y", @speed_min_y
      end
    }
    if @enabled
      min_y_speed_widgets.third.setValue GlobalValues.getOption("shake-speed_min_y")
    end
  end

  def enable
    @original_location = GlobalValues.getFrame.getLocation

    GlobalValues.setOption "shake-amount", @amount
    GlobalValues.setOption "shake-speed_max_x", @speed_max_x
    GlobalValues.setOption "shake-speed_min_x", @speed_min_x
    GlobalValues.setOption "shake-speed_max_y", @speed_max_y
    GlobalValues.setOption "shake-speed_min_y", @speed_min_y
  end
end

FAOSDance.registerExtension ShakeExtension.new
