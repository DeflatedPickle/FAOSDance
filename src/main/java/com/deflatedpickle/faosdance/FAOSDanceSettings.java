package com.deflatedpickle.faosdance;

import kotlin.Triple;

import javax.swing.*;
import java.awt.*;

public class FAOSDanceSettings {
    public static JPanel createBorderPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    public static Triple<JButton, JSlider, JSpinner> createOptionDouble(Container parent, String name, Double defaultNumber, Double maxNumber, Double minNumber) {
        Triple<JButton, JSlider, JSpinner> components = GlobalValues.INSTANCE.addLabelSliderSpinnerDouble(parent, (GridBagLayout) parent.getLayout(), name, defaultNumber, maxNumber, minNumber);
        components.getFirst().setEnabled(false);
        components.getSecond().setEnabled(false);
        components.getThird().setEnabled(false);
        return components;
    }

    public static Triple<JButton, JSlider, JSpinner> createOptionInteger(Container parent, String name, Integer defaultNumber, Integer maxNumber, Integer minNumber) {
        Triple<JButton, JSlider, JSpinner> components = GlobalValues.INSTANCE.addLabelSliderSpinnerInteger(parent, (GridBagLayout) parent.getLayout(), name, defaultNumber, maxNumber, minNumber);
        components.getFirst().setEnabled(false);
        components.getSecond().setEnabled(false);
        components.getThird().setEnabled(false);
        return components;
    }
}
