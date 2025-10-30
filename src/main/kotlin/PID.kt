class PID(
    var Kp: Double = 1.0,
    var Ki: Double = 0.0,
    var Kd: Double = 0.0,
    var setpoint: Double = 0.0,
    var sampleTime: Double = 0.01,
    output_limits: Pair<Double?, Double?> = Pair(null, null),
    var auto_mode: Boolean = true,
    var proportional_on_measurement: Boolean = false,
    var differential_on_measurement: Boolean = true,
    var error_map: ((Double) -> Double)? = null,
    private val time_fn: () -> Double = { System.nanoTime() / 1e9 }
) {
    private var _proportional = 0.0
    private var _integral = 0.0
    private var _derivative = 0.0

    internal var _last_time: Double? = null
    private var _last_output: Double? = null
    private var _last_error: Double? = null
    private var _last_input: Double? = null

    var output_limits: Pair<Double?, Double?> = output_limits
        set(value) {
            field = value
            _integral = clamp(_integral, value)
        }

    init {
        reset()
    }

    fun call(input: Double, dt: Double? = null): Double? {
        if (!auto_mode) return _last_output

        val now = time_fn()
        val actualDt = dt ?: (_last_time?.let { now - it } ?: 1e-16)

        if (actualDt <= 0) return _last_output

        if (sampleTime != null && actualDt < sampleTime && _last_output != null) {
            return _last_output
        }

        val error = setpoint - input
        val mappedError = error_map?.invoke(error) ?: error

        val dInput = if (_last_input != null) input - _last_input!! else 0.0
        val dError = if (_last_error != null) mappedError - _last_error!! else 0.0

        _proportional = if (proportional_on_measurement) {
            _proportional - Kp * dInput
        } else {
            Kp * mappedError
        }

        _integral += Ki * mappedError * actualDt
        _integral = clamp(_integral, output_limits)

        _derivative = if (differential_on_measurement) {
            if (actualDt > 0) -Kd * dInput / actualDt else 0.0
        } else {
            if (actualDt > 0) Kd * dError / actualDt else 0.0
        }

        val output = _proportional + _integral + _derivative
        val clampedOutput = clamp(output, output_limits)

        _last_output = clampedOutput
        _last_input = input
        _last_error = mappedError
        _last_time = now

        return clampedOutput
    }

    fun reset() {
        _last_time = null
        _last_output = null
        _last_error = null
        _last_input = null
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
