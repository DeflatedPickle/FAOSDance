java_import "java.awt.Insets"
java_import "javax.swing.JLabel"
java_import "javax.swing.JComboBox"
java_import "javax.swing.JProgressBar"

class AutoActionExtension < DanceExtension
  def initialize
    super "Auto Action", "Changes the action automatically", "DeflatedPickle"

    @wait = 12
    @wait_limit = @wait
  end

  def pre_draw(graphics)
    if @wait > 0.0
      @wait -= 1
      @progress_bar.setValue @wait
    else
      @wait = @wait_limit

      case @selection_type_combobox.getSelectedIndex
      when 0
        # Iterative
        action_index = @action_list.find_index GlobalValues.getOption "sprite.animation.action"
        if action_index > @animation_length
          GlobalValues.setOption "sprite.animation.action", @action_list[0]
        else
          GlobalValues.setOption "sprite.animation.action", @action_list[action_index + 1]
        end
      when 1
        # Random
        GlobalValues.setOption "sprite.animation.action", @action_list.sample
      end
    end
  end

  def settings(panel)
    grid_settings = GridBagConstraints.new GridBagConstraints::RELATIVE, GridBagConstraints::RELATIVE,
                                           1, 1,
                                           0.0, 0.0,
                                           GridBagConstraints::EAST, GridBagConstraints::NONE,
                                           Insets.new(0, 0, 0, 0),
                                           0, 0
    panel.add JLabel.new("Selection Type:"), grid_settings
    combo_list = %w(Iterative Random)
    @selection_type_combobox = JComboBox.new combo_list.to_java
    @selection_type_combobox.addActionListener {|it|
      if @enabled
        GlobalValues.setOption "auto_action-selection_type", it.source.to_java(javax::swing::JComboBox).getSelectedItem
      end
    }
    if @enabled
      @selection_type_combobox.setSelectedItem GlobalValues.getOption("auto_action-selection_type")
    end
    grid_settings = GridBagConstraints.new GridBagConstraints::RELATIVE, GridBagConstraints::RELATIVE,
                                           GridBagConstraints::REMAINDER, 1,
                                           1.0, 0.0,
                                           GridBagConstraints::CENTER, GridBagConstraints::HORIZONTAL,
                                           Insets.new(0, 0, 0, 0),
                                           0, 0
    panel.add @selection_type_combobox, grid_settings

    @wait_widgets = FAOSDanceSettings.createOptionInteger panel, "Wait:", @wait, 144, 0
    if @enabled
      @wait_widgets.third.setValue GlobalValues.getOption("auto_action-wait")
    end

    panel.add JLabel.new "Frames Till Next Action:"
    @progress_bar = JProgressBar.new 0, @wait
    panel.add @progress_bar, grid_settings

    @wait_widgets.third.addChangeListener {|it|
      @wait = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      if @enabled
        GlobalValues.setOption "auto_action-wait", @wait
      end
      @wait_limit = @wait
      @progress_bar.setMaximum @wait
    }
  end

  def enable
    @action_list = GlobalValues.getSheet.spriteMap.keys
    @animation_length = GlobalValues.getSheet.spriteNumX

    GlobalValues.setOption "auto_action-selection_type", "Iterative"
    GlobalValues.setOption "auto_action-wait", @wait
  end
end

FAOSDance.registerExtension AutoActionExtension.new
