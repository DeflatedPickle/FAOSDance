package com.deflatedpickle.faosdance;

import javax.swing.*;

public class JDoubleSlider extends JSlider {
    private double factor;

    public JDoubleSlider(double min, double max, double value, double factor) {
        super((int) ((float) min * factor), (int) ((float) max * factor), (int) ((float) value * factor));
        this.factor = factor;
    }

    public double getDoubleMinimum() {
        return super.getMinimum() / this.factor;
    }

    public double getDoubleMaximum() {
        return super.getMaximum() / this.factor;
    }

    public double getDoubleValue() {
        return super.getValue() / this.factor;
    }
}
