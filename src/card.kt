import properties.AnimalProperty

abstract class Card

class SinglePropertyCard(val property: AnimalProperty)

class DoublePropertyCard(val property1: AnimalProperty, val property2: AnimalProperty)
