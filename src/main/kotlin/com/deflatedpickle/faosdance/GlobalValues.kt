package com.deflatedpickle.faosdance

import com.deflatedpickle.faosdance.settings.SettingsDialog
import java.awt.*
import javax.swing.*
import kotlin.math.roundToInt

object GlobalValues {
    init {
        Thread(RubyThread(), "Ruby").start()
    }

    val maxSize = 10.0

    var sheet: SpriteSheet? = null
    var currentAction = ""

    var opacity = 1.0

    var animFrame = 0

    var play = true

    var isReflectionVisible = true
    var reflectionPadding = 0.0

    var fadeHeight = 0.65f
    var fadeOpacity = 0.25f

    var xPosition = 0
    var yPosition = 0

    var xMultiplier = 0.5
    var yMultiplier = 0.5

    // var xRotation = 0
    // var yRotation = 0
    var zRotation = 0

    var fps = 8
    var rewind = false

    var isVisible = true
    var isSolid = true
    var isTopLevel = true

    var currentPath = System.getProperty("user.home")

    var timer: Timer? = null

    var frame: JFrame? = null

    var animationControls: Triple<JComponent, JSlider, JSpinner>? = null

    var oldWidth = 0
    var oldHeight = 0

    var effectiveSize: Rectangle? = null

    // The current settings window
    var settingsDialog: SettingsDialog? = null

    fun initPositions() {
        effectiveSize = getEffectiveScreenSize(frame!!)
        xPosition = effectiveSize!!.width / 2
        yPosition = effectiveSize!!.height / 2
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
    ): Triple<JComponent, JSlider, JSpinner> {
        return addComponentSliderSpinner<Double>(parent, gridBagLayout, JLabel(name), defaultValue, maxNumber, minNumber)
    }

    fun addLabelSliderSpinnerInteger(
        parent: Container,
        gridBagLayout: GridBagLayout,
        name: String,
        defaultValue: Int,
        maxNumber: Int,
        minNumber: Int
    ): Triple<JComponent, JSlider, JSpinner> {
        return addComponentSliderSpinner<Int>(parent, gridBagLayout, JLabel(name), defaultValue, maxNumber, minNumber)
    }

    inline fun <reified T : Number> addComponentSliderSpinner(
        parent: Container,
        gridBagLayout: GridBagLayout,
        component: JComponent,
        defaultValue: Number,
        maxNumber: Number,
        minNumber: Number
    ): Triple<JComponent, JSlider, JSpinner> {
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

        parent.add(component)
        parent.add(slider)
        parent.add(spinner)

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
            gridwidth = GridBagConstraints.REMAINDER
        })

        return Triple(component, slider, spinner)
    }
}