class ShakeExtension < DanceExtension
  def initialize
    super "Shake", "Shakes the sprite in random directions", "DeflatedPickle"

    @shake_amount = 1
    @shake_speed_max_x = 1.6
    @shake_speed_max_y = 1.6

    @shake_speed_min_x = 1.0
    @shake_speed_min_y = 1.0

    @shake_x = @shake_speed_max_x
    @shake_y = @shake_speed_max_y

    @counter = @shake_amount

    @random_bool = [true, false]
  end

  def pre_draw(graphics)
    if @counter == 0
      @counter = @shake_amount

      @shake_x = @shake_speed_max_x
      @shake_y = @shake_speed_max_y

      # TODO: Translate the graphics instead of the window
      GlobalValues.getFrame.setLocation @original_location.x, @original_location.y
    else
      if @shake_x < 0
        @shake_x *= rand @shake_speed_min_x..@shake_speed_max_x
      end

      if @random_bool.sample
        @shake_x = -@shake_x
      end

      if @shake_y < 0
        @shake_y *= rand @shake_speed_min_y..@shake_speed_max_y
      end

      if @random_bool.sample
        @shake_y = -@shake_y
      end

      GlobalValues.getFrame.setLocation @original_location.x + @shake_x, @original_location.y + @shake_y

      @counter -= 1
    end
  end

  def settings(panel)
    FAOSDanceSettings.createOptionInteger(panel, "Shake Amount:", @shake_amount, 42, 1).third.addChangeListener {|it|
      @shake_amount = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
    }

    FAOSDanceSettings.createOptionDouble(panel, "Max X Speed:", @shake_speed_max_x, 3.0, 0.0).third.addChangeListener {|it|
      @shake_speed_max_x = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
    }
    FAOSDanceSettings.createOptionDouble(panel, "Min X Speed:", @shake_speed_min_x, 3.0, 0.0).third.addChangeListener {|it|
      @shake_speed_min_x = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
    }

    FAOSDanceSettings.createOptionDouble(panel, "Max Y Speed:", @shake_speed_max_y, 3.0, 0.0).third.addChangeListener {|it|
      @shake_speed_max_y = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
    }
    FAOSDanceSettings.createOptionDouble(panel, "Min Y Speed:", @shake_speed_min_y, 3.0, 0.0).third.addChangeListener {|it|
      @shake_speed_min_y = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
    }
  end

  def enable
    @original_location = GlobalValues.getFrame.getLocation
  end
end

FAOSDance.registerExtension ShakeExtension.new
