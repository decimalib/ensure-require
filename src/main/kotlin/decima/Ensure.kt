package decima

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.full.IllegalCallableAccessException
import kotlin.reflect.full.primaryConstructor

/**
 * Throws a [Throwable] with the result of calling [lazyMessage] if the [value] is false.
 */
@OptIn(ExperimentalContracts::class)
inline fun <reified T : Throwable> ensure(value: Boolean, lazyMessage: () -> Any) {
    contract {
        returns() implies value
    }
    if (!value) {
        throw instantiateThrowable<T>(lazyMessage)
    }
}

/**
 * Throws a [Throwable] if the [value] is false.
 */
@OptIn(ExperimentalContracts::class)
inline fun <reified T : Throwable> ensure(value: Boolean) {
    contract {
        returns() implies value
    }
    ensure<T>(value) { "Failed requirement because the required value was false" }
}

/**
 * Throws a [Throwable] with the result of calling [lazyMessage] if the [value] is null.
 */
@OptIn(ExperimentalContracts::class)
inline fun <reified T : Throwable> ensureNotNull(value: Any?, lazyMessage: () -> Any) {
    contract {
        returns() implies (value != null)
    }
    if (value == null) {
        throw instantiateThrowable<T>(lazyMessage)
    }
}

/**
 * Throws a [Throwable] if the [value] is null.
 */
@OptIn(ExperimentalContracts::class)
inline fun <reified T : Throwable> ensureNotNull(value: Any?) {
    contract {
        returns() implies (value != null)
    }
    ensureNotNull<T>(value) { "Failed requirement because the required value was null" }
}

/**
 * Helper function to create an instance of a [Throwable] using reflection.
 * @param lazyMessage A lambda that returns the message for the [Throwable].
 * @return An instance of [Throwable] of type [T].
 * @throws IllegalArgumentException If the [T] class does not have a primary constructor with a single argument of type [String] or the constructor is not accessible.
 */
@Throws(IllegalArgumentException::class)
inline fun <reified T : Throwable> instantiateThrowable(lazyMessage: () -> Any): Throwable {
    val constructor = T::class.primaryConstructor
    requireNotNull(constructor) { "Exception class ${T::class.simpleName} does not have a primary constructor." }
    try {
        return constructor.call(lazyMessage())
    } catch (e: Exception) {
        throw IllegalArgumentException(
            when (e) {
                is IllegalCallableAccessException -> "Exception class <${T::class.simpleName}> constructor is not accessible."
                is IllegalArgumentException -> "Exception class <${T::class.simpleName}> does not have a primary constructor that takes a single argument of type String."
                else -> "Could not create an instance of <${T::class.simpleName}> with the given message, because of: ${e.message}"
            }, e
        )
    }
}
