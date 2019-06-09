package com.deflatedpickle.faosdance;

import com.jidesoft.swing.RangeSlider;

public class DoubleRangeSlider extends RangeSlider {
    private double factor;

    public DoubleRangeSlider(double min, double max, double low, double high, double factor) {
        super((int) ((float) min * factor), (int) ((float) max * factor), (int) ((float) low * factor), (int) ((float) high * factor));
        this.factor = factor;
    }

    public double getDoubleMinimum() {
        return super.getMinimum() / this.factor;
    }

    public double getDoubleMaximum() {
        return super.getMaximum() / this.factor;
    }

    public double getDoubleLowValue() {
        return super.getLowValue() / this.factor;
    }

    public double getDoubleHighValue() {
        return super.getHighValue() / this.factor;
    }

    public void setDoubleLowValue(Double value) {
        super.setLowValue((int) (value * this.factor));
    }

    public void setDoubleHighValue(Double value) {
        super.setHighValue((int) (value * this.factor));
    }
}
