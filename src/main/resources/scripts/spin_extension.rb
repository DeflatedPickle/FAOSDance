class SpinExtension < DanceExtension
  def initialize
    super "Spin", "Spins the sprite around using a sine function", "DeflatedPickle"

    @increase = 0.1
  end

  def pre_draw(graphics)
    if @counter < @original
      @counter += @increase
    else
      @counter = -@original
    end

    new_size = Math.sin(@counter)
    GlobalValues.setOption "sprite.size.width", new_size
  end

  def settings(panel)
    increase_widgets = FAOSDanceSettings.createOptionDouble panel, "Increase:", @increase, 1.0, 0.1
    increase_widgets.third.addChangeListener {|it|
      @increase = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).doubleValue
      if @enabled
        GlobalValues.setOption "spin-increase", @increase
      end
    }
    if @enabled
      increase_widgets.setSelectedItem GlobalValues.getOption("spin-increase")
    end
  end

  def enable
    @original = GlobalValues.getOption "sprite.size.width"
    @counter = @original

    GlobalValues.setOption "spin-increase", @increase
  end

  def disable
    GlobalValues.setOption "sprite.size.width", @original
  end
end

FAOSDance.registerExtension SpinExtension.new
