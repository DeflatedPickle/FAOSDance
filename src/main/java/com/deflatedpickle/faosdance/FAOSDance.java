package com.deflatedpickle.faosdance;

public class FAOSDance {
    public static Double getXMultiplier() {
        return GlobalValues.INSTANCE.getXMultiplier();
    }

    public static void setXMultiplier(Double value) {
        GlobalValues.INSTANCE.setXMultiplier(value);
    }
}
