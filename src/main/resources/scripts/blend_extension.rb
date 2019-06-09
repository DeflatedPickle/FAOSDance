java_import "java.awt.AlphaComposite"

class BlendExtension < DanceExtension
  def initialize
    super "Blend", "Renders the given amount of past and future frames", "DeflatedPickle"

    @past_frame_amount = 2
    @future_frame_amount = 2
    @opacity_multiplier = 0.16
  end

  def pre_draw_sprite(graphics)
    if @past_frame_amount > 0
      for i in 0..@past_frame_amount
        frame = GlobalValues.getOption "sprite.animation.frame"
        action = GlobalValues.getOption "sprite.animation.action"

        if GlobalValues.getOption "sprite.animation.rewind"
          sprite_image = GlobalValues.getSheet.getSpriteMap[action][frame + 1 <= 7 ? frame + 1 : 0]
        else
          sprite_image = GlobalValues.getSheet.getSpriteMap[action][frame - 1 >= 0 ? frame - 1 : 7]
        end

        @sprite = BufferedImage.new @width, @height, BufferedImage::TYPE_INT_ARGB
        sprite_graphics = @sprite.createGraphics

        sprite_graphics.composite = AlphaComposite.getInstance AlphaComposite::SRC_OVER, GlobalValues.getOption("sprite.opacity") * @opacity_multiplier * i / 9
        sprite_graphics.drawRenderedImage sprite_image, nil

        sprite_graphics.dispose
        graphics.drawRenderedImage @sprite, nil
      end
    end
  end

  def post_draw_sprite(graphics)
    if @future_frame_amount > 0
      for i in 0..@future_frame_amount
        frame = GlobalValues.getOption "sprite.animation.frame"
        action = GlobalValues.getOption "sprite.animation.action"

        if GlobalValues.getOption "sprite.animation.rewind"
          sprite_image = GlobalValues.getSheet.getSpriteMap[action][frame - 1 >= 0 ? frame - 1 : 7]
        else
          sprite_image = GlobalValues.getSheet.getSpriteMap[action][frame + 1 <= 7 ? frame + 1 : 0]
        end

        @sprite = BufferedImage.new @width, @height, BufferedImage::TYPE_INT_ARGB
        sprite_graphics = @sprite.createGraphics

        sprite_graphics.composite = AlphaComposite.getInstance AlphaComposite::SRC_OVER, GlobalValues.getOption("sprite.opacity") * @opacity_multiplier * i / 9
        sprite_graphics.drawRenderedImage sprite_image, nil

        sprite_graphics.dispose
        graphics.drawRenderedImage @sprite, nil
      end
    end
  end

  def settings(panel)
    opacity_multiplier_widgets = FAOSDanceSettings.createOptionDouble(panel, "Opacity Multiplier:", @opacity_multiplier, 1, 0.1)
    opacity_multiplier_widgets.third.addChangeListener {|it|
      @opacity_multiplier = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).floatValue
      if @enabled
        GlobalValues.setOption "blend-opacity_multiplier", @opacity_multiplier
      end
    }
    if @enabled
      opacity_multiplier_widgets.third.setValue GlobalValues.getOption("blend-opacity_multiplier")
    end

    FAOSDanceSettings.createSeparator(panel)

    past_frames_widgets = FAOSDanceSettings.createOptionInteger(panel, "Past Frames:", @past_frame_amount, 8, 0)
    past_frames_widgets.third.addChangeListener {|it|
      @past_frame_amount = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue + 1
      if @enabled
        GlobalValues.setOption "blend-past_frame_amount", @past_frame_amount
      end
    }
    if @enabled
      past_frames_widgets.third.setValue GlobalValues.getOption("blend-past_frame_amount")
    end

    future_frames_widgets = FAOSDanceSettings.createOptionInteger(panel, "Future Frames:", @future_frame_amount, 8, 0)
    future_frames_widgets.third.addChangeListener {|it|
      @future_frame_amount = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue + 1
      if @enabled
        GlobalValues.setOption "blend-future_frame_amount", @future_frame_amount
      end
    }
    if @enabled
      future_frames_widgets.third.setValue GlobalValues.getOption("blend-past_frame_amount")
    end
  end

  def enable
    @width = GlobalValues.getSheet.getSpriteWidth
    @height = GlobalValues.getSheet.getSpriteHeight

    GlobalValues.setOption "blend-past_frame_amount", @past_frame_amount
    GlobalValues.setOption "blend-future_frame_amount", @future_frame_amount
    GlobalValues.setOption "blend-opacity_multiplier", @opacity_multiplier
  end
end

FAOSDance.registerExtension BlendExtension.new
