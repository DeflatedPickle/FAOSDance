package com.deflatedpickle.faosdance

import org.jruby.Ruby
import org.jruby.RubyObject
import org.jruby.embed.IsolatedScriptingContainer
import org.jruby.embed.LocalVariableBehavior
import org.jruby.embed.ScriptingContainer

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
        while (run) {
            for (i in queue) {
                ruby.evalScriptlet(i)
            }
            queue = listOf()
        }
    }
}