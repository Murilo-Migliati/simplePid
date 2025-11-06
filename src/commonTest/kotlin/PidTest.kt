import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PidTest {

    class MockClock(var currentTime: Double = 0.0) {

        fun timeProvider(): Double = currentTime

        fun advance(seconds: Double) {
            currentTime += seconds
        }
    }

    @Test
    fun testKp(){

        val clock  = MockClock(0.0)

        val pid = PIDController(
            kp = 2.0,
            setpoint = 100.0,
            timeFn = clock::timeProvider
        )

        var output = pid.call(input = 0.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(200.0, output, 0.001, "Teste P - Error  100")

        clock.advance(1.0)

        output = pid.call(input = 75.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(50.0, output, 0.001, "Teste P - Error 25")
    }

    @Test
    fun testKi() {
        val clock  = MockClock(0.0)
        val pid = PIDController(
            kp = 0.0,
            ki = 2.0,
            setpoint = 100.0,
            timeFn = clock::timeProvider
        )

        var output = pid.call(input = 50.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(0.0, output, 0.001, "Test I - First call")

        clock.advance(1.0)
        output = pid.call(input = 50.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(100.0, output, 0.001, "Test I - Accumulation 1 (dt=1.0s)")


        clock.advance(0.5)
        output = pid.call(input = 50.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(150.0, output, 0.001, "Test I - Accumulation 2 (dt=0.5s)")
    }

    @Test
    fun testZeroAssert(){
        val clock  = MockClock(0.0)
        val pid = PIDController(
            kp = 1.0,
            ki = 1.0,
            kd = 1.0,
            setpoint = 0.0
        )

        var output = pid.call(input = 0.0)

        assertNotNull(output,"Not null")
        assertEquals(0.0,output, 0.001, "Teste 0 - Returns 0")


    }

    @Test
    fun testKpNegativeSetPoint(){
        val clock  = MockClock(0.0)
        val pid = PIDController(
            kp = 1.0,
            setpoint = -10.0,
            timeFn = clock::timeProvider
        )

        var output = pid.call(input = 0.0)
        clock.advance(1.0)

        assertNotNull(output,"Not null")
        assertEquals(-10.0,output, 0.001, "Test P- - error -10")

        output = pid.call(input = 5.0)
        clock.advance(1.0)

        assertNotNull(output,"Not null")
        assertEquals(-15.0,output, 0.001, "Test P- - error -15")

        output = pid.call(input = -5.0)
        clock.advance(1.0)

        assertNotNull(output,"Not null")
        assertEquals(-5.0,output, 0.001, "Test P- - error -5")

        output = pid.call(input = -15.0)
        clock.advance(1.0)

        assertNotNull(output,"Not null")
        assertEquals(5.0,output, 0.001, "Test P- - error 5")


    }

    @Test
    fun testKiNegativeSetPoint(){
        val clock  = MockClock(0.0)
        val pid = PIDController(
            kp = 0.0,
            ki = 1.0,
            setpoint = -10.0,
            sampleTime = 0.1,
            timeFn = clock::timeProvider
        )


        var output = pid.call(input = 0.0)
        assertNotNull(output,"Not null")
        assertEquals(0.0, output, 0.001, "Teste I- - Chamada Inicial")

        clock.advance(0.1)
        output = pid.call(input = 0.0)


        assertNotNull(output,"Not null")
        assertEquals(-1.0, output, 0.001, "Teste I- - Accumulation 1 (should be -1.0)")


        clock.advance(0.1)
        output = pid.call(input = 0.0)


        assertNotNull(output,"Not null")
        assertEquals(-2.0, output, 0.001, "Teste I- - Accumulation 2 (should be -2.0)")
    }

    @Test
    fun testKd() {
        val clock = MockClock(0.0)
        val pid = PIDController(
            kp = 0.0,
            ki = 0.0,
            kd = 0.1,
            setpoint = 10.0,
            timeFn = clock::timeProvider
        )

        var output = pid.call(input = 0.0)
        assertNotNull(output, "Output must not be null.")
        assertEquals(0.0, output, 0.001, "Teste D - First call")

        clock.advance(0.1)
        output = pid.call(input = 0.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(0.0, output, 0.001, "Teste D - dInput é 0")

        clock.advance(0.1)
        output = pid.call(input = 5.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(-5.0, output, 0.001, "Teste D - dInput = 5")

        clock.advance(0.1)
        output = pid.call(input = 15.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(-10.0, output, 0.001, "Teste D - dInput = 10")
    }

    @Test
    fun testKdNegativeSetpoint() {
        val clock = MockClock(0.0)
        val pid = PIDController(
            kp = 0.0,
            ki = 0.0,
            kd = 0.1,
            setpoint = -10.0,
            timeFn = clock::timeProvider
        )

        var output = pid.call(input = 0.0)
        clock.advance(0.1)
        output = pid.call(input = 0.0)
        assertNotNull(output, "Output must not be null.")
        assertEquals(0.0, output, 0.001, "Test D - dInput é 0")

        clock.advance(0.1)
        output = pid.call(input = 5.0)
        assertNotNull(output, "Output must not be null.")
        assertEquals(-5.0, output, 0.001, "Test D - dInput = 5")

        clock.advance(0.1)
        output = pid.call(input = -5.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(10.0, output, 0.001, "Test D - dInput = -10")

        clock.advance(0.1)
        output = pid.call(input = -15.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(10.0, output, 0.001, "Test D - dInput = -10 (repeated)")
    }

    @Test
    fun testStateTarge(){
        val pid = PIDController(
            kp = 0.0,
            ki = 1.0,
            kd = 0.1,
            setpoint = 10.0,
        )
          var output =  pid.call(input = 10.0)

        assertNotNull(output, "Output must not be null.")
        assertEquals(0.0, output, 0.001, "Teste State - Not returns 0")

    }

    @Test
    fun testOutputLimits(){
        val clock = MockClock(0.0)
        val pid = PIDController(
            kp = 100.0,
            ki = 20.0,
            kd = 40.0,
            setpoint = 10.0,
            outputLimits = Pair(0.0,100.0  ),
        )
        clock.advance(0.1)
        var output: Double? = pid.call(input = 0.0)

        assertNotNull(output, "Output must not be null.")
        var estaNoIntervalo = output in 0.0..100.0
        assertTrue(estaNoIntervalo, "The output was outside the range 0..100 (actual value: $output).")

        clock.advance(0.1)
        output = pid.call(input = -100.0)

        assertNotNull(output, "Output must not be null.Output must not be null.")
        estaNoIntervalo = output in 0.0..100.0
        assertTrue(estaNoIntervalo, "The output was outside the range 0..100 (actual value: $output).")
    }

    @Test
    fun testSampleTime(){
        val pid = PIDController(setpoint = 10.0, sampleTime = 10.0)
        var output = pid.call(input = 0.0)
        var output2 = pid.call(input = 100.0)

        assertNotNull(output, "Output must not be null.")
        var equals = output == output2
        assertTrue(equals, "output hasn't changed")

    }

    @Test
    fun testCustomTimeFn() {

        var customTime = 0.0
        val customTimeProvider = {
            customTime += 1.0
            customTime
        }
        val pid = PIDController(
            kp = 1.0,
            setpoint = 100.0,
            timeFn = customTimeProvider
        )

        pid.call(input = 0.0)

        var output = pid.call(input = 0.0)
        assertNotNull(output, "Output must not be null.")
        assertEquals(100.0, output, 0.001, "Teste time_fn - Chamada 2")

        output = pid.call(input = 0.0)
        assertNotNull(output, "Output must not be null.")
        assertEquals(100.0, output, 0.001, "Teste time_fn - Chamada 3")
    }

    @Test
    fun testCustomTimeFnDirect() {
        var i = 0.0
        val pid = PIDController(timeFn = { i += 1.0; i })

        pid.call(input = 0.0)
        assertEquals(1.0, pid._lastTime)

        pid.call(input = 0.0)
        assertEquals(2.0, pid._lastTime)

        pid.call(input = 0.0)
        assertEquals(3.0, pid._lastTime)
    }


    @Test
    fun testAutoMode() {
        val clock = MockClock(0.0)
        val pid = PIDController(
            kp = 1.0,
            ki = 0.0,
            kd = 0.0,
            setpoint = 10.0,
            sampleTime = 0.0,
            timeFn = clock::timeProvider
        )

        clock.advance(0.1)
        var output = pid.call(input = 0.0)
        assertNotNull(output, "Output must not be null.")
        assertEquals(10.0, output, 0.001)

        clock.advance(0.1)
        output = pid.call(input = 5.0)
        assertNotNull(output, "Output must not be null.")
        assertEquals(5.0, output, 0.001)

        pid.autoMode = false
        val lastOutput = output

        clock.advance(0.1)
        output = pid.call(input = 1.0)
        assertNotNull(output, "Output must not be null.")
        assertEquals(lastOutput, output)

        clock.advance(0.1)
        output = pid.call(input = 7.0)
        assertNotNull(output, "Output must not be null.")
        assertEquals(lastOutput, output)

        pid.autoMode = true

        clock.advance(0.1)
        output = pid.call(input = 8.0)

        assertNotNull(output)
        assertEquals(2.0, output, 0.001)
    }

}
