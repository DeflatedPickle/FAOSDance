package com.deflatedpickle.faosdance

import com.deflatedpickle.faosdance.util.GlobalValues
import org.jruby.Ruby
import org.jruby.RubyObject
import org.jruby.embed.IsolatedScriptingContainer
import org.jruby.embed.LocalVariableBehavior

class RubyThread : Runnable {
    companion object {
        @Volatile
        var queue = listOf<String>()

        @Volatile
        var extensions = listOf<RubyObject>()

        val rubyContainer = IsolatedScriptingContainer(LocalVariableBehavior.PERSISTENT)
        val ruby: Ruby

        init {
            ruby = rubyContainer.provider.runtime
        }
    }

    var run = true

    override fun run() {
        GlobalValues.loadScripts()

        while (run) {
            for (i in queue) {
                ruby.evalScriptlet(i)
            }
            queue = listOf()
        }
    }
}