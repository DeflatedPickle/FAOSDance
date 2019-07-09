package com.deflatedpickle.faosdance;

import com.deflatedpickle.faosdance.backend.RubyThread;
import com.deflatedpickle.faosdance.gui.settings.ExtensionSettings;
import org.jruby.RubyObject;

import java.util.ArrayList;

public class FAOSDance {
    public static void registerExtension(RubyObject object) {
        ArrayList newExtensions = new ArrayList(RubyThread.Companion.getExtensions());
        newExtensions.add(object);
        RubyThread.Companion.setExtensions(newExtensions);

        ExtensionSettings.Companion.getExtensionList().add(object);
    }
}
