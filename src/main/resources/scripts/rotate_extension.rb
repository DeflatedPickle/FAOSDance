class RotateExtension < DanceExtension
  def initialize
    super "Rotate", "Rotates the sprite around using a sine function"

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
end

FAOSDance.registerExtension RotateExtension.new
