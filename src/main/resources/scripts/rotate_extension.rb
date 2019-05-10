class RotateExtension < DanceExtension
  def initialize
    super "Rotate", "Rotates the sprite around using a sine function"

    @original = FAOSDance.getZRotation
    @max = 360
    @counter = 0
  end

  def pre_draw(g2d)
    if @counter < @max
      @counter += 8
    else
      @counter = 0
    end

    FAOSDance.setZRotation @counter
  end

  def enable
    @original = FAOSDance.getZRotation
  end

  def disable
    FAOSDance.setZRotation @original
  end
end

FAOSDance.registerExtension RotateExtension.new
