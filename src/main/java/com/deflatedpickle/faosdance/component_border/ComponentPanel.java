package com.deflatedpickle.faosdance.component_border;

import javax.swing.*;
import java.awt.*;

// http://www.java2s.com/Code/Java/Swing-Components/ComponentTitledPaneExample2.htm
public class ComponentPanel extends JPanel {
    protected ComponentBorder border;
    private JComponent component;
    public JPanel panel;
    private boolean transmittingAllowed;

    public ComponentPanel() {
        this(new JLabel(""));
    }

    public ComponentPanel(JComponent component) {
        this.component = component;
        border = new ComponentBorder(component);
        setBorder(border);
        panel = new JPanel();
        setLayout(null);
        add(component);
        add(panel);
        transmittingAllowed = false;
    }

    public JComponent getTitleComponent() {
        return component;
    }

    public void setTitleComponent(JComponent newComponent) {
        remove(component);
        add(newComponent);
        border.setTitleComponent(newComponent);
        component = newComponent;
    }

    public JPanel getContentPane() {
        return panel;
    }

    public void doLayout() {
        Insets insets = getInsets();
        Rectangle rect = getBounds();
        rect.x = 0;
        rect.y = 0;

        Rectangle compR = border.getComponentRect(rect, insets);
        component.setBounds(compR);
        rect.x += insets.left;
        rect.y += insets.top;
        rect.width -= insets.left + insets.right;
        rect.height -= insets.top + insets.bottom;
        panel.setBounds(rect);
    }

    public void setTransmittingAllowed(boolean enable) {
        transmittingAllowed = enable;
    }

    public boolean getTransmittingAllowed() {
        return transmittingAllowed;
    }
}
