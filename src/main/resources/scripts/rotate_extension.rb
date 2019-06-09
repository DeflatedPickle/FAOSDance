class RotateExtension < DanceExtension
  def initialize
    super "Rotate", "Rotates the sprite by an amount", "DeflatedPickle"

    @max = 360
    @increase = 8
    @counter = 0
  end

  def pre_draw(graphics)
    if @counter < @max
      @counter += @increase
    else
      @counter = 0
    end

    GlobalValues.setOption "sprite.rotation.z", @counter
  end

  def settings(panel)
    increase_widgets = FAOSDanceSettings.createOptionInteger panel, "Increase:", @increase, 180, 1
    increase_widgets.third.addChangeListener {|it|
      @increase = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).doubleValue
    }
  end

  def enable
    @original = GlobalValues.getOption "sprite.rotation.z"
  end

  def disable
    GlobalValues.setOption "sprite.rotation.z", @original
  end
end

FAOSDance.registerExtension RotateExtension.new
