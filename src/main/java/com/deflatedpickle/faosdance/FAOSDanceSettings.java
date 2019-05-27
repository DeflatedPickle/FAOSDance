package com.deflatedpickle.faosdance;

import kotlin.Triple;

import javax.swing.*;
import java.awt.*;

public class FAOSDanceSettings {
    public static JPanel createBorderPanel(Container parent, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        parent.add(panel, new GridBagConstraints() {{
            this.fill = GridBagConstraints.BOTH;
            this.weightx = 1.0;
            this.weighty = 1.0;
            this.gridwidth = GridBagConstraints.REMAINDER;
        }});
        return panel;
    }

    public static Triple<JButton, JSlider, JSpinner> createOptionDouble(Container parent, String name, Double defaultNumber, Double maxNumber, Double minNumber) {
        return GlobalValues.INSTANCE.addLabelSliderSpinnerDouble(parent, (GridBagLayout) parent.getLayout(), name, defaultNumber, maxNumber, minNumber);
    }

    public static Triple<JButton, JSlider, JSpinner> createOptionInteger(Container parent, String name, Integer defaultNumber, Integer maxNumber, Integer minNumber) {
        return GlobalValues.INSTANCE.addLabelSliderSpinnerInteger(parent, (GridBagLayout) parent.getLayout(), name, defaultNumber, maxNumber, minNumber);
    }
}
