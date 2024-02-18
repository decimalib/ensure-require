package decima

import decima.exceptions.ExceptionWithDefaultConstructor
import decima.exceptions.ExceptionWithExpectedFormat
import decima.exceptions.ExceptionWithPrivateConstructor
import decima.exceptions.ExceptionWithSecondaryConstructor
import decima.exceptions.ExceptionWithUnusedConstructor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EnsureTests {

    @Test
    fun `check if ensure throws a given exception with the default message`() {
        val ex = assertFailsWith<ExceptionWithExpectedFormat> {
            ensure<ExceptionWithExpectedFormat>(false)
        }
        assertEquals("Failed requirement because the required value was false", ex.message)
    }

    @Test
    fun `check if ensure throws a given exception with a message`() {
        val message = "Should not be false"
        val ex = assertFailsWith<ExceptionWithExpectedFormat> {
            ensure<ExceptionWithExpectedFormat>(false) { message }
        }
        assertEquals(message, ex.message)
    }

    @Test
    fun `check if ensure implies smart cast upon success`() {
        data class ExceptionTest(val msg: String)

        var message: String? = "hello"
        // Cannot do: ExceptionTest(message)
        ensure<ExceptionWithUnusedConstructor>(message != null)
        // Should smart cast, assuming thread safety
        ExceptionTest(message)
        // Should lose smart cast
        message = null
        // Cannot do: ExceptionTest(message)
    }

    @Test
    fun `check if ensureNotNull throws a given exception with a default message`() {
        val ex = assertFailsWith<ExceptionWithExpectedFormat> {
            ensureNotNull<ExceptionWithExpectedFormat>(null)
        }
        assertEquals("Failed requirement because the required value was null", ex.message)
    }

    @Test
    fun `check if ensureNotNull throws a given exception with a message`() {
        val message = "Should not be null"
        val ex = assertFailsWith<ExceptionWithExpectedFormat> {
            ensureNotNull<ExceptionWithExpectedFormat>(null) { message }
        }
        assertEquals(message, ex.message)
    }

    @Test
    fun `check if ensureNotNull implies smart cast upon success`() {
        data class ExceptionTest(val msg: String)

        var message: String? = "hello"
        // Cannot do: ExceptionTest(message)
        ensureNotNull<ExceptionWithUnusedConstructor>(message)
        // Should smart cast, assuming thread safety
        ExceptionTest(message)
        // Should lose smart cast
        message = null
        // Cannot do: ExceptionTest(message)
    }

    @Test
    fun `check if an exception with a non-accessible primary constructor throws an exception`() {
        val ex = assertFailsWith<IllegalArgumentException> {
            ensure<ExceptionWithPrivateConstructor>(false)
        }
        assertEquals(
            "Exception class <${ExceptionWithPrivateConstructor::class.java.simpleName}> constructor is not accessible.",
            ex.message
        )
    }

    @Test
    fun `check if an exception without a primary constructor that takes a single String parameter throws an exception`() {
        val ex = assertFailsWith<IllegalArgumentException> {
            ensure<ExceptionWithDefaultConstructor>(false)
        }
        assertEquals(
            "Exception class <${ExceptionWithDefaultConstructor::class.java.simpleName}> does not have a primary constructor that takes a single argument of type String.",
            ex.message
        )
    }

    @Test
    fun `check if an exception with a secondary constructor that takes a single String parameter throws an exception`() {
        val ex = assertFailsWith<IllegalArgumentException> {
            ensure<ExceptionWithSecondaryConstructor>(false) {
                "Failed requirement"
            }
        }
        assertEquals(
            "Exception class <${ExceptionWithSecondaryConstructor::class.java.simpleName}> does not have a primary constructor that takes a single argument of type String.",
            ex.message
        )
    }
}
