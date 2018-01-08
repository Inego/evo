package properties

sealed class AnimalProperty(val name: String) {

}

abstract class IndividualProperty(name: String) : AnimalProperty(name)

abstract class PairedProperty(name: String) : AnimalProperty(name)