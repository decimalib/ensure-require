### Ensure - Kotlin Require Alternative

The `ensure` function is a Kotlin extension function that allows you to throw a custom exception when a condition is not met. It is similar to the regular [require](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/require.html) function, but it allows you to throw a custom exception instead of the default `IllegalArgumentException`. It is also type-safe, meaning that the exception type is inferred from the exception class passed as the first parameter.

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

### Advantages and disadvantages

- ✅ Ability to throw custom exceptions but with the same syntax as the regular `require` function.
- ✅ It can be upper bounded to a specific exception type, allowing for more specific error handling as you can decide which exceptions can be thrown.

- ❌ Requires the use of reflection, which can take a toll on performance.
- ❌ The exception type to be thrown must have a primary constructor that takes a single `String` parameter, which can be a limitation in some cases.
- ❌ Must opt in to the experimental contracts feature.

### Requirements

If using Gradle, add the following to your `build.gradle.kts` in the `dependencies` block:

```kotlin
implementation(kotlin("reflect"))
```