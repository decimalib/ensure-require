package decima.exceptions

class ExceptionWithExpectedFormat(message: String) : Exception(message)
class ExceptionWithUnusedConstructor(message: String) : Exception("Invalid email address")
class ExceptionWithDefaultConstructor : Exception("Invalid username")
class ExceptionWithPrivateConstructor private constructor(message: String) : Exception(message)
class ExceptionWithSecondaryConstructor(id: Int, message: String) : Exception("Invalid id: $id") {
    constructor(message: String) : this(-1, message)
}
