package by.overpass.gather.model.data.validator

interface Validator<T> {
    fun isValid(arg: T): Boolean
}