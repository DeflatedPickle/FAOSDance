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
        action_index = @action_list.find_index GlobalValues.getCurrentAction
        if action_index > @animation_length
          GlobalValues.setCurrentAction @action_list[0]
        else
          GlobalValues.setCurrentAction @action_list[action_index + 1]
        end
      when 1
        # Random
        GlobalValues.setCurrentAction @action_list.sample
      end
    end
  end

  def settings(panel)
    panel.add JLabel.new "Selection Type:"
    @selection_type_combobox = JComboBox.new %w(Iterative Random).to_java
    grid_settings = GridBagConstraints.new
    grid_settings.fill = GridBagConstraints::HORIZONTAL
    grid_settings.weightx = 1.0
    grid_settings.gridwidth = GridBagConstraints::REMAINDER
    panel.add @selection_type_combobox, grid_settings

    @wait_widgets = FAOSDanceSettings.createOptionInteger panel, "Wait:", @wait, 144, 0

    panel.add JLabel.new "Frames Till Next Action:"
    @progress_bar = JProgressBar.new 0, @wait
    panel.add @progress_bar, grid_settings

    @wait_widgets.third.addChangeListener {|it|
      @wait = it.source.to_java(javax::swing::JSpinner).model.value.to_java(java::lang::Float).intValue
      @wait_limit = @wait
      @progress_bar.setMaximum @wait
    }
  end

  def enable
    @action_list = GlobalValues.getSheet.spriteMap.keys
    @animation_length = GlobalValues.getSheet.spriteNumX
  end
end

FAOSDance.registerExtension AutoActionExtension.new
