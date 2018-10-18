package oyster.journey


object Direction extends Enumeration {
  type Direction = Value
  val IN, OUT = Value
}

object Transport extends Enumeration {
  type Transport = Value
  val Bus, Tube = Value
}

case class Journey(stationName: String, transport: Transport.Value, direction: Direction.Value, cost: Double)
