class PIDController(
    var kp: Double = 1.0,
    var ki: Double = 0.0,
    var kd: Double = 0.0,
    var setpoint: Double = 0.0,
    var sampleTime: Double = 0.01,
    outputLimits: Pair<Double?, Double?> = Pair(null, null),
    var autoMode: Boolean = true,
    var proportionalOnMeasurement: Boolean = false,
    var differentialOnMeasurement: Boolean = true,
    var errorMap: ((Double) -> Double)? = null,
    private val timeFn: () -> Double = { System.nanoTime() / 1e9 }
) {
    private var _proportional = 0.0
    private var _integral = 0.0
    private var _derivative = 0.0

    internal var _lastTime: Double? = null
    private var _lastOutput: Double? = null
    private var _lastError: Double? = null
    private var _lastInput: Double? = null

    var output_limits: Pair<Double?, Double?> = outputLimits
        set(value) {
            field = value
            _integral = clamp(_integral, value)
        }

    init {
        reset()
    }

    fun call(input: Double, dt: Double? = null): Double? {
        if (!autoMode) return _lastOutput

        val now = timeFn()
        val actualDt = dt ?: (_lastTime?.let { now - it } ?: 1e-16)

        if (actualDt <= 0) return _lastOutput

        if (sampleTime != null && actualDt < sampleTime && _lastOutput != null) {
            return _lastOutput
        }

        val error = setpoint - input
        val mappedError = errorMap?.invoke(error) ?: error

        val dInput = if (_lastInput != null) input - _lastInput!! else 0.0
        val dError = if (_lastError != null) mappedError - _lastError!! else 0.0

        _proportional = if (proportionalOnMeasurement) {
            _proportional - kp * dInput
        } else {
            kp * mappedError
        }

        _integral += ki * mappedError * actualDt
        _integral = clamp(_integral, output_limits)

        _derivative = if (differentialOnMeasurement) {
            if (actualDt > 0) -kd * dInput / actualDt else 0.0
        } else {
            if (actualDt > 0) kd * dError / actualDt else 0.0
        }

        val output = _proportional + _integral + _derivative
        val clampedOutput = clamp(output, output_limits)

        _lastOutput = clampedOutput
        _lastInput = input
        _lastError = mappedError
        _lastTime = now

        return clampedOutput
    }

    fun reset() {
        _lastTime = null
        _lastOutput = null
        _lastError = null
        _lastInput = null
        _proportional = 0.0
        _integral = 0.0
        _derivative = 0.0
    }

    private fun clamp(value: Double, limits: Pair<Double?, Double?>): Double {
        val (lower, upper) = limits
        return when {
            upper != null && value > upper -> upper
            lower != null && value < lower -> lower
            else -> value
        }
    }
}
