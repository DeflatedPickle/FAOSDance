class SpinExtension < DanceExtension
  def initialize
    super "Spin", "Spins the sprite around using a sine function", "DeflatedPickle"

    @original = FAOSDance.getXMultiplier
    @counter = @original
    @increase = 0.1
  end

  def pre_draw(graphics)
    if @counter < @original
      @counter += @increase
    else
      @counter = -@original
    end

    new_size = Math.sin(@counter)
    FAOSDance.setXMultiplier new_size
  end

  def settings(panel)
    increase_widgets = FAOSDanceSettings.createOptionDouble panel, "Increase:", @increase, 1.0, 0.1
    increase_widgets.third.addChangeListener {|it|
      @increase = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).doubleValue
    }
  end

  def enable
    @original = FAOSDance.getXMultiplier
  end

  def disable
    FAOSDance.setXMultiplier @original
  end
end

FAOSDance.registerExtension SpinExtension.new
