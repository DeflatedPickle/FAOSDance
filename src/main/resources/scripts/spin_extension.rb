class SpinExtension < DanceExtension
  def initialize
    super "Spin", "Spins the sprite around using a sine function"

    @original = FAOSDance.getXMultiplier()
    @counter = @original
  end

  def pre_draw(g2d)
    if @counter < @original
      @counter += 0.1
    else
      @counter = -@original
    end

    new_size = Math.sin(@counter)
    FAOSDance.setXMultiplier new_size
  end
end

$extension = SpinExtension.new
