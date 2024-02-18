package decima

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.jvm.Throws
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
        throw createException<T>(lazyMessage)
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
        throw createException<T>(lazyMessage)
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
 * Helper function to create an instance of [Throwable] using reflection.
 */
inline fun <reified T : Throwable> createException(lazyMessage: () -> Any): Throwable {
    val constructor = T::class.primaryConstructor
    requireNotNull(constructor) { "Exception class ${T::class.simpleName} does not have a primary constructor." }
    try {
        return constructor.call(lazyMessage())
    } catch (e: Exception) {
        throw IllegalArgumentException("Exception class ${T::class.simpleName} does not have a primary constructor that takes a single argument of type String or the constructor is not accessible.", e)
    }
}