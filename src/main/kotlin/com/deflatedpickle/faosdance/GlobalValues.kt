package com.deflatedpickle.faosdance

import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.jidesoft.swing.RangeSlider
import org.apache.commons.lang3.SystemUtils
import org.jruby.RubyBoolean
import org.jruby.RubyObject
import org.jruby.ir.Tuple
import java.awt.*
import java.awt.image.BufferedImage
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.zip.ZipInputStream
import javax.swing.*
import kotlin.math.abs
import kotlin.math.roundToInt

object GlobalValues {
    const val version = "v0.33.8-alpha"

    @JvmStatic
    val homePath = when {
        SystemUtils.IS_OS_WINDOWS -> File("${System.getenv("APPDATA")}/FAOSDance/")
        SystemUtils.IS_OS_LINUX -> File("${System.getProperty("user.home")}/.config/FAOSDance/")
        else -> null
    }

    lateinit var configFile: File
    lateinit var langProperties: File
    val scriptsFolder = if (ClassLoader.getSystemResource("icon.png").protocol == "jar") {
        File(homePath, "scripts")
    } else {
        File(ClassLoader.getSystemResource("scripts").path)
    }
    val configFolder = if (ClassLoader.getSystemResource("icon.png").protocol == "jar") {
        File(homePath, "config")
    } else {
        File(ClassLoader.getSystemResource("config").path)
    }

    init {
        createEnviromentFiles()
        Thread(RubyThread(), "Ruby").start()
    }

    fun createEnviromentFiles() {
        homePath?.mkdir()

        // It's a loop in case I decide to add more user folders, or move the lang folder out of the program
        for (i in listOf(scriptsFolder, configFolder)) {
            if (!i.isDirectory) {
                i.mkdir()
            }

            val fileList = i.listFiles().map { it.name }

            val zipInputStream =
                ZipInputStream(GlobalValues::class.java.protectionDomain.codeSource.location.openStream())

            while (true) {
                try {
                    val entry = zipInputStream.nextEntry ?: break

                    if (entry.name.startsWith("${i.name}/")) {
                        if (entry.name != "${i.name}/") {
                            val bufferedInputStream = BufferedInputStream(zipInputStream)
                            val text = bufferedInputStream.reader().readText().toByteArray()

                            val name = entry.name.split("/").last()
                            if (!fileList.contains(name)) {
                                File(i, name).apply {
                                    this.createNewFile()
                                    Files.write(this.toPath(), text)
                                }
                            }
                        }
                    }
                } catch (e: IOException) {
                    break
                }
            }
        }

        configFile = File(homePath, "config/config.toml")
        langProperties = File(homePath, "config/lang.properties")
    }

    fun loadScripts() {
        val scripts = mutableListOf<String>()
        // Makes sure the core class is always loaded first
        scripts.add(File(scriptsFolder, "dance_extension.rb").readText())
        for (i in scriptsFolder.listFiles()) {
            val text = i.readText()
            if (i.name != "dance_extension.rb" && text.contains("< DanceExtension")) {
                scripts.add(text)
            }
        }
        RubyThread.queue = scripts
    }

    // For extensions to use
    @JvmStatic
    val optionsMap = NestedHashMap<String, Any>()

    var rootMap = optionsMap
    fun parseOption(option: String): Tuple<NestedHashMap<String, Any>?, String?> {
        val items = option.split(".")
        for (i in items) {
            if (i == items.last()) {
                return Tuple(rootMap, items.last())
            }
            else {
                if (rootMap[i] is NestedHashMap<*, *>) {
                    rootMap = rootMap[i] as NestedHashMap<String, Any>
                    parseOption(items.subList(items.indexOf(i), items.size).joinToString("."))
                }
            }
        }

        return Tuple(null, null)
    }

    @JvmStatic
    fun getOption(option: String): Any? {
        rootMap = optionsMap
        val parse = parseOption(option)
        return parse.a!![parse.b!!]
    }

    @JvmStatic
    fun setOption(option: String, value: Any) {
        rootMap = optionsMap
        val parse = parseOption(option)
        parse.a!![parse.b!!] = value
    }

    @JvmStatic
    val icon = ImageIcon(ClassLoader.getSystemResource("icon.png"), "FAOSDance")

    @JvmStatic
    val maxSize = 10.0

    @JvmStatic
    var sheet: SpriteSheet? = null
    @JvmStatic
    var mutableSprite: BufferedImage? = null

    var currentPath = System.getProperty("user.home")
        @JvmStatic
        get

    var timer: Timer? = null

    var frame: JFrame? = null
        @JvmStatic
        get

    var animationControls: Triple<JComponent, JSlider, JSpinner>? = null

    var oldWidth = 0
    var oldHeight = 0

    var effectiveSize: Rectangle? = null
        @JvmStatic
        get

    // The current settings window
    var settingsDialog: SettingsDialog? = null

    var widgetList: MutableList<JComponent>? = null
    val extensionPanelMap = mutableMapOf<String, JPanel>()

    fun initPositions() {
        effectiveSize = getEffectiveScreenSize(frame!!)
        optionsMap.getMap("window")!!.getMap("location")!!["x"] = effectiveSize!!.width / 2
        optionsMap.getMap("window")!!.getMap("location")!!["y"] = effectiveSize!!.height / 2
    }

    fun <E : Enum<E>> enumToReadableNames(enum: Class<E>): Array<String> {
        return enum.enumConstants.map { enumItem -> sanatizeEnumValue(enumItem.name) }.toTypedArray()
    }

    fun sanatizeEnumValue(enumItem: String): String {
        return enumItem
            .replace("_", " ")
            .toLowerCase()
            .split(" ")
            .joinToString(" ") { subStr ->
                subStr.capitalize()
            }
    }

    fun unsanatizeEnumValue(enumItem: String): String {
        return enumItem.replace(" ", "_").toUpperCase()
    }

    fun updateScripts(name: String, value: Any) {
        for (i in RubyThread.extensions) {
            if (isEnabled(i)) {
                RubyThread.rubyContainer.callMethod(i, "update_value", name, value)
            }
        }
    }

    fun resize(direction: Direction? = null) {
        if (sheet != null) {
            val width =
                ((((sheet!!.spriteWidth * optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("width")!!) * 2) * 100) / 100).toInt()
            val height =
                ((((sheet!!.spriteHeight * optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("height")!!) * if (optionsMap.getMap(
                        "reflection"
                    )!!.getOption<Boolean>("visible")!!
                ) 2 else 1) * 100) / 100).toInt()

            frame!!.minimumSize = Dimension(abs(width), abs(height))
            frame!!.setSize(abs(width), abs(height))

            // TODO: Clean this up
            when (direction) {
                Direction.HORIZONTAL -> {
                    frame!!.setLocation(frame!!.x - ((frame!!.width - oldWidth) / 2), frame!!.y)
                    oldWidth = frame!!.width
                }
                Direction.VERTICAL -> {
                    frame!!.setLocation(frame!!.x, frame!!.y - ((frame!!.height - oldHeight) / 2))
                    oldHeight = frame!!.height
                }
                Direction.BOTH -> {
                    frame!!.setLocation(frame!!.x - ((frame!!.width - oldWidth) / 2), frame!!.y)
                    oldWidth = frame!!.width
                    frame!!.setLocation(frame!!.x, frame!!.y - ((frame!!.height - oldHeight) / 2))
                    oldHeight = frame!!.height
                }
            }

            effectiveSize = getEffectiveScreenSize(frame!!)
        }
    }

    fun configureSpriteSheet(sheet: SpriteSheet) {
        this.sheet = sheet
        optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("action", this.sheet!!.spriteMap.keys.first())
        resize()
    }

    fun isEnabled(rubyObject: RubyObject): Boolean {
        return rubyObject.getInstanceVariable("@enabled") as RubyBoolean == RubyThread.ruby.`true`
    }

    // https://stackoverflow.com/a/29177069
    fun getEffectiveScreenSize(component: Component): Rectangle {
        val rectangle = Rectangle()

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val bounds = Toolkit.getDefaultToolkit().getScreenInsets(component.graphicsConfiguration)

        rectangle.width =
            (screenSize.getWidth() - bounds.left.toDouble() - bounds.right.toDouble()).toInt() - frame!!.width
        rectangle.height =
            (screenSize.getHeight() - bounds.top.toDouble() - bounds.bottom.toDouble()).toInt() - frame!!.height

        rectangle.x = ((screenSize.getHeight() - component.height) / 2.0).toInt()
        rectangle.y = ((screenSize.getWidth() - component.width) / 2.0).toInt()

        return rectangle
    }

    fun addLabelSliderSpinnerDouble(
        parent: Container,
        gridBagLayout: GridBagLayout,
        name: String,
        defaultValue: Double,
        maxNumber: Double,
        minNumber: Double
    ): Triple<JButton, JSlider, JSpinner> {
        return addComponentSliderSpinner<Double>(
            parent,
            gridBagLayout,
            JLabel(name),
            defaultValue,
            maxNumber,
            minNumber
        )
    }

    fun addLabelSliderSpinnerInteger(
        parent: Container,
        gridBagLayout: GridBagLayout,
        name: String,
        defaultValue: Int,
        maxNumber: Int,
        minNumber: Int
    ): Triple<JButton, JSlider, JSpinner> {
        return addComponentSliderSpinner<Int>(parent, gridBagLayout, JLabel(name), defaultValue, maxNumber, minNumber)
    }

    inline fun <reified T : Number> addComponentSliderSpinner(
        parent: Container,
        gridBagLayout: GridBagLayout,
        component: JComponent,
        defaultValue: Number,
        maxNumber: Number,
        minNumber: Number
    ): Triple<JButton, JSlider, JSpinner> {
        val slider = when (T::class) {
            Int::class -> JSlider(minNumber.toInt(), maxNumber.toInt(), defaultValue.toInt())
            Double::class -> DoubleSlider(
                minNumber.toDouble(),
                maxNumber.toDouble(),
                defaultValue.toDouble(),
                100.0
            )
            else -> JSlider()
        }

        val stepSize = when (T::class) {
            Int::class -> 1 as T
            Double::class -> 0.01 as T
            else -> 0 as T
        }

        val spinner = JSpinner(
            SpinnerNumberModel(
                defaultValue.toDouble(),
                minNumber.toDouble(),
                maxNumber.toDouble(),
                stepSize
            )
        )

        slider.value = when (T::class) {
            Int::class -> defaultValue.toInt()
            Double::class -> (defaultValue.toFloat() * 100).toInt()
            else -> 0
        }
        spinner.value = defaultValue

        slider.addChangeListener {
            spinner.value = when (T::class) {
                Int::class -> slider.value
                Double::class -> slider.value.toDouble() / 100
                else -> 0
            }
        }
        spinner.addChangeListener {
            slider.value = when (T::class) {
                Int::class -> when {
                    spinner.value is Int -> spinner.value as Int
                    spinner.value is Double -> (spinner.value as Double).roundToInt()
                    else -> 0
                }
                Double::class -> (spinner.value as Double * 100).toInt()
                else -> 0
            }
        }

        val revertButton = JButton("Revert").apply {
            addActionListener {
                spinner.value = defaultValue.toDouble()
            }
        }

        parent.add(component, GridBagConstraints().apply {
            anchor = GridBagConstraints.EAST
        })
        parent.add(slider, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
        })
        parent.add(spinner, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            anchor = GridBagConstraints.EAST
        })
        parent.add(revertButton, GridBagConstraints().apply {
            gridwidth = GridBagConstraints.REMAINDER
        })

        return Triple(revertButton, slider, spinner)
    }

    inline fun <reified T : Number> addComponentRangeSliderSpinner(
        parent: Container,
        gridBagLayout: GridBagLayout,
        component: JComponent,
        highValue: Number,
        lowValue: Number,
        maxNumber: Number,
        minNumber: Number
    ): Triple<JButton, RangeSlider, Pair<JSpinner, JSpinner>> {
        val slider = when (T::class) {
            Int::class -> RangeSlider(minNumber.toInt(), maxNumber.toInt(), lowValue.toInt(), highValue.toInt())
            Double::class -> DoubleRangeSlider(
                minNumber.toDouble(),
                maxNumber.toDouble(),
                lowValue.toDouble(),
                highValue.toDouble(),
                100.0
            )
            else -> RangeSlider()
        }

        val stepSize = when (T::class) {
            Int::class -> 1 as T
            Double::class -> 0.01 as T
            else -> 0 as T
        }

        val lowSpinner = JSpinner(
            SpinnerNumberModel(
                lowValue.toDouble(),
                minNumber.toDouble(),
                maxNumber.toDouble(),
                stepSize
            )
        )

        val highSpinner = JSpinner(
            SpinnerNumberModel(
                highValue.toDouble(),
                minNumber.toDouble(),
                maxNumber.toDouble(),
                stepSize
            )
        )

        slider.lowValue = when (T::class) {
            Int::class -> lowValue.toInt()
            Double::class -> (lowValue.toFloat() * 100).toInt()
            else -> 0
        }
        lowSpinner.value = lowValue

        slider.addChangeListener {
            lowSpinner.value = when (T::class) {
                Int::class -> slider.lowValue
                Double::class -> slider.lowValue.toDouble() / 100
                else -> 0
            }
            highSpinner.value = when (T::class) {
                Int::class -> slider.highValue
                Double::class -> slider.highValue.toDouble() / 100
                else -> 0
            }
        }
        lowSpinner.addChangeListener {
            slider.lowValue = when (T::class) {
                Int::class -> when {
                    lowSpinner.value is Int -> lowSpinner.value as Int
                    lowSpinner.value is Double -> (lowSpinner.value as Double).roundToInt()
                    else -> 0
                }
                Double::class -> (lowSpinner.value as Double * 100).toInt()
                else -> 0
            }
        }
        highSpinner.addChangeListener {
            slider.highValue = when (T::class) {
                Int::class -> when {
                    highSpinner.value is Int -> highSpinner.value as Int
                    highSpinner.value is Double -> (highSpinner.value as Double).roundToInt()
                    else -> 0
                }
                Double::class -> (highSpinner.value as Double * 100).toInt()
                else -> 0
            }
        }

        val revertButton = JButton("Revert").apply {
            addActionListener {
                lowSpinner.value = lowValue.toDouble()
                highSpinner.value = highValue.toDouble()
            }
        }

        parent.add(component, GridBagConstraints().apply {
            anchor = GridBagConstraints.EAST
        })
        parent.add(lowSpinner, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 0.3
            anchor = GridBagConstraints.EAST
        })
        parent.add(slider, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
        })
        parent.add(highSpinner, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 0.3
            anchor = GridBagConstraints.EAST
        })
        parent.add(revertButton, GridBagConstraints().apply {
            gridwidth = GridBagConstraints.REMAINDER
        })

        return Triple(revertButton, slider, Pair(lowSpinner, highSpinner))
    }

    fun addLabelRangeSliderSpinnerDouble(
        parent: Container,
        gridBagLayout: GridBagLayout,
        name: String,
        highValue: Number,
        lowValue: Number,
        maxNumber: Number,
        minNumber: Number
    ): Triple<JButton, RangeSlider, Pair<JSpinner, JSpinner>> {
        return addComponentRangeSliderSpinner<Double>(
            parent,
            gridBagLayout,
            JLabel(name),
            highValue,
            lowValue,
            maxNumber,
            minNumber
        )
    }

    fun addLabelRangeSliderSpinnerInteger(
        parent: Container,
        gridBagLayout: GridBagLayout,
        name: String,
        highValue: Number,
        lowValue: Number,
        maxNumber: Number,
        minNumber: Number
    ): Triple<JButton, RangeSlider, Pair<JSpinner, JSpinner>> {
        return addComponentRangeSliderSpinner<Int>(
            parent,
            gridBagLayout,
            JLabel(name),
            highValue,
            lowValue,
            maxNumber,
            minNumber
        )
    }
}