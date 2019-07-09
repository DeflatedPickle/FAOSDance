package com.deflatedpickle.faosdance;

import com.deflatedpickle.faosdance.component_border.ComponentPanel;
import com.jidesoft.swing.RangeSlider;
import kotlin.Pair;
import kotlin.Triple;
import org.jdesktop.swingx.JXCollapsiblePane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class FAOSDanceSettings {
    public static ComponentPanel createBorderPanel(Container parent, String title, Boolean toggled) {
        ComponentPanel panel = new ComponentPanel();
        JComponent widget;
        if (toggled) {
            widget = new JCheckBox(title) {{
                addItemListener(e -> {
                    for (Component i : panel.panel.getComponents()) {
                        i.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                    }
                });
            }};
        } else {
            widget = new JLabel(title);
        }

        panel.setTitleComponent(widget);
        panel.panel.setLayout(new GridBagLayout());

        parent.add(panel, new GridBagConstraints() {{
            this.fill = GridBagConstraints.BOTH;
            this.weightx = 1.0;
            this.weighty = 1.0;
            this.gridwidth = GridBagConstraints.REMAINDER;
        }});
        return panel;
    }

    public static Triple<JButton, JSlider, JSpinner> createOptionDouble(Container parent, String name, Double defaultNumber, Double maxNumber, Double minNumber) {
        if (parent instanceof JXCollapsiblePane) {
            return GlobalValues.INSTANCE.addLabelSliderSpinnerDouble(parent, (GridBagLayout) ((JXCollapsiblePane) parent).getContentPane().getLayout(), name, defaultNumber, maxNumber, minNumber);
        }
        else {
            return GlobalValues.INSTANCE.addLabelSliderSpinnerDouble(parent, (GridBagLayout) parent.getLayout(), name, defaultNumber, maxNumber, minNumber);
        }
    }

    public static Triple<JButton, JSlider, JSpinner> createOptionInteger(Container parent, String name, Integer defaultNumber, Integer maxNumber, Integer minNumber) {
        if (parent instanceof JXCollapsiblePane) {
            return GlobalValues.INSTANCE.addLabelSliderSpinnerInteger(parent, (GridBagLayout) ((JXCollapsiblePane) parent).getContentPane().getLayout(), name, defaultNumber, maxNumber, minNumber);
        }
        else {
            return GlobalValues.INSTANCE.addLabelSliderSpinnerInteger(parent, (GridBagLayout) parent.getLayout(), name, defaultNumber, maxNumber, minNumber);
        }
    }

    public static Triple<JButton, RangeSlider, Pair<JSpinner, JSpinner>> createOptionRangeDouble(Container parent, String name, Double highNumber, Double lowNumber, Double maxNumber, Double minNumber) {
        if (parent instanceof JXCollapsiblePane) {
            return GlobalValues.INSTANCE.addLabelRangeSliderSpinnerDouble(parent, (GridBagLayout) ((JXCollapsiblePane) parent).getContentPane().getLayout(), name, highNumber, lowNumber, maxNumber, minNumber);
        }
        else {
            return GlobalValues.INSTANCE.addLabelRangeSliderSpinnerDouble(parent, (GridBagLayout) parent.getLayout(), name, highNumber, lowNumber, maxNumber, minNumber);
        }
    }

    public static Triple<JButton, RangeSlider, Pair<JSpinner, JSpinner>> createOptionRangeInteger(Container parent, String name, Double highNumber, Double lowNumber, Double maxNumber, Double minNumber) {
        if (parent instanceof JXCollapsiblePane) {
            return GlobalValues.INSTANCE.addLabelRangeSliderSpinnerInteger(parent, (GridBagLayout) ((JXCollapsiblePane) parent).getContentPane().getLayout(), name, highNumber, lowNumber, maxNumber, minNumber);
        }
        else {
            return GlobalValues.INSTANCE.addLabelRangeSliderSpinnerInteger(parent, (GridBagLayout) parent.getLayout(), name, highNumber, lowNumber, maxNumber, minNumber);
        }
    }

    public static JSeparator createSeparator(Container parent) {
        JSeparator separator = new JSeparator();
        parent.add(separator, new GridBagConstraints() {{
            this.fill = GridBagConstraints.BOTH;
            this.weightx = 1.0;
            this.gridwidth = GridBagConstraints.REMAINDER;
        }});
        return separator;
    }
}
