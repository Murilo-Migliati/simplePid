import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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

        val pid = PID(
            Kp = 2.0,
            setpoint = 100.0,
            time_fn = clock::timeProvider
        )

        var output = pid.call(input = 0.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(200.0, output, 0.001, "Teste P - Erro de 100")

        clock.advance(1.0)

        output = pid.call(input = 75.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(50.0, output, 0.001, "Teste P - Erro de 25")
    }

    @Test
    fun testKi() {
        val clock  = MockClock(0.0)
        val pid = PID(
            Kp = 0.0,
            Ki = 2.0,
            setpoint = 100.0,
            time_fn = clock::timeProvider
        )

        var output = pid.call(input = 50.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(0.0, output, 0.001, "Teste I - Chamada inicial")

        clock.advance(1.0)
        output = pid.call(input = 50.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(100.0, output, 0.001, "Teste I - Acumulação 1 (dt=1.0s)")


        clock.advance(0.5)
        output = pid.call(input = 50.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(150.0, output, 0.001, "Teste I - Acumulação 2 (dt=0.5s)")
    }

    @Test
    fun testZeroAssert(){
        val clock  = MockClock(0.0)
        val pid = PID(
            Kp = 1.0,
            Ki = 1.0,
            Kd = 1.0,
            setpoint = 0.0
        )

        var output = pid.call(input = 0.0)

        assertNotNull(output,"Not null")
        assertEquals(0.0,output, 0.001, "Teste 0 - Not Returns 0")


    }

    @Test
    fun testKpNegativeSetPoint(){
        val clock  = MockClock(0.0)
        val pid = PID(
            Kp = 1.0,
            setpoint = -10.0,
            time_fn = clock::timeProvider
        )

        var output = pid.call(input = 0.0)
        clock.advance(1.0)

        assertNotNull(output,"Not null")
        assertEquals(-10.0,output, 0.001, "Teste P- - erro de -10")

        output = pid.call(input = 5.0)
        clock.advance(1.0)

        assertNotNull(output,"Not null")
        assertEquals(-15.0,output, 0.001, "Teste P- - erro de -15")

        output = pid.call(input = -5.0)
        clock.advance(1.0)

        assertNotNull(output,"Not null")
        assertEquals(-5.0,output, 0.001, "Teste P- - erro de -5")

        output = pid.call(input = -15.0)
        clock.advance(1.0)

        assertNotNull(output,"Not null")
        assertEquals(5.0,output, 0.001, "Teste P- - erro de 5")


    }

    @Test
    fun testKiNegativeSetPoint(){
        val clock  = MockClock(0.0)
        val pid = PID(
            Kp = 0.0,
            Ki = 1.0,
            setpoint = -10.0,
            sampleTime = 0.1,
            time_fn = clock::timeProvider
        )


        var output = pid.call(input = 0.0)
        assertNotNull(output,"Not null")
        assertEquals(0.0, output, 0.001, "Teste I- - Chamada Inicial")

        clock.advance(0.1)
        output = pid.call(input = 0.0)


        assertNotNull(output,"Not null")
        assertEquals(-1.0, output, 0.001, "Teste I- - Acumulação 1 (deve ser -1.0)")


        clock.advance(0.1)
        output = pid.call(input = 0.0)


        assertNotNull(output,"Not null")
        assertEquals(-2.0, output, 0.001, "Teste I- - Acumulação 2 (deve ser -2.0)")
    }

    @Test
    fun testKd() {
        val clock = MockClock(0.0)
        val pid = PID(
            Kp = 0.0,
            Ki = 0.0,
            Kd = 0.1,
            setpoint = 10.0,
            time_fn = clock::timeProvider
        )

        var output = pid.call(input = 0.0)
        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(0.0, output, 0.001, "Teste D - Primeira chamada")

        clock.advance(0.1)
        output = pid.call(input = 0.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(0.0, output, 0.001, "Teste D - dInput é 0")

        clock.advance(0.1)
        output = pid.call(input = 5.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(-5.0, output, 0.001, "Teste D - dInput = 5")

        clock.advance(0.1)
        output = pid.call(input = 15.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(-10.0, output, 0.001, "Teste D - dInput = 10")
    }

    @Test
    fun testKdNegativeSetpoint() {
        val clock = MockClock(0.0)
        val pid = PID(
            Kp = 0.0,
            Ki = 0.0,
            Kd = 0.1,
            setpoint = -10.0,
            time_fn = clock::timeProvider
        )

        var output = pid.call(input = 0.0)
        clock.advance(0.1)
        output = pid.call(input = 0.0)
        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(0.0, output, 0.001, "Teste D - dInput é 0")

        clock.advance(0.1)
        output = pid.call(input = 5.0)
        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(-5.0, output, 0.001, "Teste D - dInput = 5")

        clock.advance(0.1)
        output = pid.call(input = -5.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(10.0, output, 0.001, "Teste D - dInput = -10")

        clock.advance(0.1)
        output = pid.call(input = -15.0)

        assertNotNull(output, "Output não deve ser nulo")
        assertEquals(10.0, output, 0.001, "Teste D - dInput = -10 (repetido)")
    }

}
