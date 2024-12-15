class PID(
    private var _Kp: Double = 1.0,
    private var _Ki: Double = 0.0,
    private var _Kd: Double = 0.0,
    var setPoint: Double = 0.0,
    var sampleTime: Double = 0.01,
    var outputLimits: Pair<Double?, Double?> = Pair(null, null),
    private var _autoMode: Boolean = true,
    var proportionalOnMeasurement: Boolean = false,
    var differentialOnMeasurement: Boolean = true,
    var errorMap: ((Double) -> Double)? = null,
    var timeFn: (() -> Double)? = null,
    var startingOutput: Double = 0.0
) {
    private var _lastOutput: Double? = null
    private var _lastInput: Double? = null
    private var _lastError: Double? = null
    private var _lastTime: Double = 0.0
    private var _proportional: Double = 0.0
    private var _integral: Double = 0.0
    private var _derivative: Double = 0.0

    var Kp: Double = _Kp
        get() = field
        set(value) {
            if (value < 0) throw IllegalArgumentException("Kp cannot be negative")
            field = value
        }
    var Ki: Double = _Ki
        get() = field
        set(value) {
            if (value < 0) throw IllegalArgumentException("Ki cannot be negative")
            field = value
        }
    var Kd: Double = _Kd
        get() = field
        set(value) {
            if (value < 0) throw IllegalArgumentException("Kd cannot be negative")
            field = value
        }

    private fun _clamp(value: Double, limits: Pair<Double?, Double?>): Double {
        val (lower, upper) = limits
        return when {
            lower != null && value < lower -> lower
            upper != null && value > upper -> upper
            else -> value
        }
    }

    fun call(input_: Double, dt: Double? = null): Double? {
        if (!autoMode) return _lastOutput

        val now = timeFn?.invoke() ?: 0.0
        val actualDt = dt ?: if (now - _lastTime > 0) now - _lastTime else 1e-16
        if (sampleTime != null && actualDt < sampleTime) {
            return _lastOutput
        }

        val error = setPoint - input_
        val dInput = input_ - (_lastInput ?: input_)
        val dError = error - (_lastError ?: error)

        val mappedError = errorMap?.invoke(error) ?: error

        if (!proportionalOnMeasurement) {
            _proportional -= Kp * mappedError
        } else {
            _proportional -= Kp * dInput
        }

        _integral += Ki * mappedError * actualDt
        _integral = _clamp(_integral, outputLimits)

        _derivative = if (differentialOnMeasurement) {
            -Kd * dInput / actualDt
        } else {
            Kd * dError / actualDt
        }

        var output = _proportional + _integral + _derivative
        output = _clamp(output, outputLimits)

        _lastOutput = output
        _lastInput = input_
        _lastError = mappedError
        _lastTime = now

        return output
    }

    fun reset() {
        _proportional = 0.0
        _integral = 0.0
        _derivative = 0.0
        _lastTime = timeFn?.invoke() ?: System.currentTimeMillis().toDouble()
        _lastOutput = null
        _lastInput = null
        _lastError = null
    }

    var autoMode: Boolean
        get() = _autoMode
        set(enabled) {
            setAutoModeImpl(enabled)
        }

    private fun setAutoModeImpl(enabled: Boolean) {
        if (enabled && !autoMode) {
            reset()
            _integral = _clamp(startingOutput, outputLimits) ?: 0.0
        }
        _autoMode = enabled
    }

    init {
        println("PID Controller initialized: Kp=$Kp, Ki=$Ki , Kd=$Kd")
    }
}

fun main() {
    println("Hello World")
}
