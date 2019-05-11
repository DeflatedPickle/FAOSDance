package com.deflatedpickle.faosdance;

import com.deflatedpickle.faosdance.settings.ExtensionSettings;
import org.jruby.RubyObject;

import java.util.ArrayList;
import java.util.List;

public class FAOSDance {
    public static void registerExtension(RubyObject object) {
        ArrayList newExtensions = new ArrayList(RubyThread.Companion.getExtensions());
        newExtensions.add(object);
        RubyThread.Companion.setExtensions(newExtensions);

        ExtensionSettings.Companion.getExtensionList().add(object);
    }

    public static Double getXMultiplier() {
        return GlobalValues.INSTANCE.getXMultiplier();
    }

    public static void setXMultiplier(Double value) {
        GlobalValues.INSTANCE.setXMultiplier(value);
    }

    public static Integer getZRotation() {
        return GlobalValues.INSTANCE.getZRotation();
    }

    public static void setZRotation(Integer value) {
        GlobalValues.INSTANCE.setZRotation(value);
    }
}
