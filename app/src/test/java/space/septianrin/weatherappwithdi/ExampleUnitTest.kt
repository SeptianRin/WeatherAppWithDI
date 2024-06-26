package space.septianrin.weatherappwithdi

import android.content.Context
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import space.septianrin.weatherappwithdi.module.homescreen.viewmodel.WeatherViewModel

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}