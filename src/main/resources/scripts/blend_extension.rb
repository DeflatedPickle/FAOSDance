java_import "java.awt.AlphaComposite"

class BlendExtension < DanceExtension
  def initialize
    super "Blend", "Renders the given amount of past and future frames", "DeflatedPickle"

    @blend_past_frame_amount = 2
    @blend_future_frame_amount = 2
    @blend_opacity_multiplier = 0.16
  end

  def pre_draw_sprite(graphics)
    if @blend_past_frame_amount > 0
      for i in 0..@blend_past_frame_amount
        if GlobalValues.getRewind
          sprite_image = GlobalValues.getSheet.getSpriteMap[GlobalValues.getCurrentAction][GlobalValues.getAnimFrame + 1 <= 7 ? GlobalValues.getAnimFrame + 1 : 0]
        else
          sprite_image = GlobalValues.getSheet.getSpriteMap[GlobalValues.getCurrentAction][GlobalValues.getAnimFrame - 1 >= 0 ? GlobalValues.getAnimFrame - 1 : 7]
        end

        @sprite = BufferedImage.new @width, @height, BufferedImage::TYPE_INT_ARGB
        sprite_graphics = @sprite.createGraphics

        sprite_graphics.composite = AlphaComposite.getInstance AlphaComposite::SRC_OVER, GlobalValues.getOpacity * @blend_opacity_multiplier * i / 9
        sprite_graphics.drawRenderedImage sprite_image, nil

        sprite_graphics.dispose
        graphics.drawRenderedImage @sprite, nil
      end
    end
  end

  def post_draw_sprite(graphics)
    if @blend_future_frame_amount > 0
      for i in 0..@blend_future_frame_amount
        if GlobalValues.getRewind
          sprite_image = GlobalValues.getSheet.getSpriteMap[GlobalValues.getCurrentAction][GlobalValues.getAnimFrame - 1 >= 0 ? GlobalValues.getAnimFrame - 1 : 7]
        else
          sprite_image = GlobalValues.getSheet.getSpriteMap[GlobalValues.getCurrentAction][GlobalValues.getAnimFrame + 1 <= 7 ? GlobalValues.getAnimFrame + 1 : 0]
        end

        @sprite = BufferedImage.new @width, @height, BufferedImage::TYPE_INT_ARGB
        sprite_graphics = @sprite.createGraphics

        sprite_graphics.composite = AlphaComposite.getInstance AlphaComposite::SRC_OVER, GlobalValues.getOpacity * @blend_opacity_multiplier * i / 9
        sprite_graphics.drawRenderedImage sprite_image, nil

        sprite_graphics.dispose
        graphics.drawRenderedImage @sprite, nil
      end
    end
  end

  def settings(panel)
    FAOSDanceSettings.createOptionDouble(panel, "Opacity Multiplier:", @blend_opacity_multiplier, 1, 0.1).third.addChangeListener {|it|
      @blend_opacity_multiplier = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
    }

    FAOSDanceSettings.createSeparator(panel)

    FAOSDanceSettings.createOptionInteger(panel, "Past Frames:", @blend_past_frame_amount, 8, 0).third.addChangeListener {|it|
      @blend_past_frame_amount = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue + 1
    }

    FAOSDanceSettings.createOptionInteger(panel, "Future Frames:", @blend_future_frame_amount, 8, 0).third.addChangeListener {|it|
      @blend_future_frame_amount = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue + 1
    }
  end

  def enable
    @width = GlobalValues.getSheet.getSpriteWidth
    @height = GlobalValues.getSheet.getSpriteHeight
  end
end

FAOSDance.registerExtension BlendExtension.new
