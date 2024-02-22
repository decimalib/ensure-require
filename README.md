## Ensure - Kotlin `require` Alternative

The `ensure` function is a Kotlin extension function that allows you to throw a custom exception when a condition is not
met. It behaves identically to the regular [require](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/require.html)
function,
but it can be parameterized with a custom exception type instead of always throwing an `IllegalArgumentException` upon
condition failure. Such a feature allows for more specific error handling,
as you can decide which exception type you want to throw based on the condition that failed, while maintaining the same syntax.

<table>
<tr>
<td> <code>require</code> </td> <td> <code>ensure</code> </td>
</tr>
<tr>
<td>

```kotlin
// throws IllegalArgumentException
requireNotNull(owner)
// (smart cast to owner)
// throws IllegalArgumentException
require(owner.id > 0) { "Invalid owner id" }
```

</td>
<td>

```kotlin
// throws OwnerNotFoundException
ensureNotNull<OwnerNotFoundException>(owner)
// (smart cast to owner)
// throws InvalidOwnerIdException
ensure<InvalidOwnerIdException>(owner.id > 0) {
    "Invalid owner id"
}
```

</td>
</tr>
</table>

### Advantages

- ✅ Ability to throw custom exceptions but with the same syntax as the regular `require` function;
- ✅ It can be upper bounded to a specific exception type. An example could be a `sealed class` hierarchy where you want
  to ensure that only this hierarchy of exceptions can be thrown.
  ```kotlin
  // example of sealed class hierarchy
  sealed class DomainException(message: String) : Throwable(message)
  class InBoundsExceptionA(message: String) : DomainException(message)
  class InBoundsExceptionB(message: String) : DomainException(message)
  // an exception type that is not part of the sealed class hierarchy 
  class OutOfBoundsException(message: String) : Exception(message)
  ```
  When changing the function(s)
  parameterized generic type from `reified T : Throwable` to `reified T : DomainException`, then:
  ```kotlin
  // no compile-time error
  ensure<InBoundsExceptionA>(true)
  // compile-time error
  ensure<OutOfBoundsException>(true)
  ```

### Disadvantages

- ❌ The exception type to be thrown must have a primary constructor that takes a single `String` parameter, which can be
  a limitation in some cases;
    - Example: `class InvalidOwnerIdException(message: String) : Trowable(message)`
- ❌ When a condition is not met, **reflection** is used to instantiate the exception type.
  It won't have a significant impact on performance in most cases, but it's something to be aware of;
- ❌ The extension functions opt in to
  the [ExperimentalContracts](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.contracts/-experimental-contracts/),
  which, as the name suggests, is an experimental feature.