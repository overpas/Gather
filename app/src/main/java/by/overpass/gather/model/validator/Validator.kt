package by.overpass.gather.model.validator

interface Validator<T> {
    fun isValid(arg: T): Boolean
}