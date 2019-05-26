package com.deflatedpickle.faosdance

import com.deflatedpickle.faosdance.settings.SettingsDialog
import org.jruby.RubyBoolean
import org.jruby.RubyObject
import java.awt.*
import javax.swing.*
import kotlin.math.roundToInt

object GlobalValues {
    init {
        Thread(RubyThread(), "Ruby").start()
    }

    @JvmStatic
    val icon = ImageIcon(ClassLoader.getSystemResource("icon.png"), "FAOSDance")

    @JvmStatic
    val maxSize = 10.0

    @JvmStatic
    var sheet: SpriteSheet? = null
    @JvmStatic
    var currentAction = ""

    @JvmStatic
    var opacity = 1.0

    @JvmStatic
    var animFrame = 0

    @JvmStatic
    var play = true

    @JvmStatic
    var isReflectionVisible = true
    @JvmStatic
    var reflectionPadding = 0.0

    @JvmStatic
    var fadeHeight = 0.65f
    @JvmStatic
    var fadeOpacity = 0.25f

    @JvmStatic
    var xPosition = 0
    @JvmStatic
    var yPosition = 0

    @JvmStatic
    var xMultiplier = 0.5
    @JvmStatic
    var yMultiplier = 0.5

    // var xRotation = 0
    // var yRotation = 0
    @JvmStatic
    var zRotation = 0

    @JvmStatic
    var fps = 8
    @JvmStatic
    var rewind = false

    @JvmStatic
    var isVisible = true
    @JvmStatic
    var isSolid = true
    @JvmStatic
    var isTopLevel = true
    @JvmStatic
    var isToggleHeld = true

    var enabledExtensions = mutableListOf<String>()
        @JvmStatic
        get

    @JvmStatic
    var scalingType: ScalingType = ScalingType.BILINEAR

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

    var extensionCheckBoxList: MutableList<JCheckBox>? = null
    val extensionPanelMap = mutableMapOf<String, JPanel>()

    fun initPositions() {
        effectiveSize = getEffectiveScreenSize(frame!!)
        xPosition = effectiveSize!!.width / 2
        yPosition = effectiveSize!!.height / 2
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
        val width = ((((sheet!!.spriteWidth * xMultiplier) * 2) * 100) / 100).toInt()
        val height = ((((sheet!!.spriteHeight * yMultiplier) * if (isReflectionVisible) 2 else 1) * 100) / 100).toInt()

        frame!!.minimumSize = Dimension(width, height)
        frame!!.setSize(width, height)

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

    fun configureSpriteSheet(sheet: SpriteSheet) {
        GlobalValues.sheet = sheet
        currentAction = GlobalValues.sheet!!.spriteMap.keys.first()
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
            Double::class -> JDoubleSlider(
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

        parent.add(component)
        parent.add(slider)
        parent.add(spinner)
        parent.add(revertButton)

        gridBagLayout.setConstraints(component, GridBagConstraints().apply {
            anchor = GridBagConstraints.EAST
        })

        gridBagLayout.setConstraints(slider, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
        })

        gridBagLayout.setConstraints(spinner, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            anchor = GridBagConstraints.EAST
        })

        gridBagLayout.setConstraints(revertButton, GridBagConstraints().apply {
            gridwidth = GridBagConstraints.REMAINDER
        })

        return Triple(revertButton, slider, spinner)
    }
}