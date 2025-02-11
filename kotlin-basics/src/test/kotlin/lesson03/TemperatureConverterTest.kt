package lesson03

import com.motycka.edu.lesson04.TemperatureConverter
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

// By convention the name of the test class should be the name of the class under test + "Test"
class TemperatureConverterTest {

    @BeforeEach
    fun beforeEach() {
        println("This runs before each test");
    }

    @AfterEach
    fun afterEach() {
        println("This runs after each test");
    }

    @Test
    @DisplayName("should convert Celsius to Fahrenheit - 0C = 32F")
    fun testConvertCelsiusToFahrenheit() {
        Assertions.assertEquals(32.0, TemperatureConverter.toFahrenheit(0.0));
    }

    // different style of test name
    @Test
    fun `should convert Fahrenheit to Celsius - 32F = 0C`() {
        Assertions.assertEquals(0.0, TemperatureConverter.toCelsius(32.0));
    }

    companion object {

        @BeforeAll
        @JvmStatic
        fun setUp() {
            println("This runs once before all tests");
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            println("This runs once after all tests");
        }
    }
}
