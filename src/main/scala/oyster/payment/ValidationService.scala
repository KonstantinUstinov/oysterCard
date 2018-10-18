package oyster.payment

import oyster.card.Card
import oyster.journey.{Direction, Journey, Transport}
import oyster.zone.StationStorage

class ValidationService {

  def validateBalance(card: Card, transport: Transport.Value) : Either[ValidationError, Unit] = {

    val result = Map(Transport.Tube -> StationStorage.MAX_COST, Transport.Bus -> StationStorage.BUS_COST)
        .get(transport).filter(f => card.balance >= f)

    Either.cond(result.isDefined, (), ValidationError("Do not have enough money to travel."))

  }

  def verifyTripIsValid(current: Journey, previous: Journey): Either[ValidationError, Unit] = {
    
      if ((current.direction == Direction.OUT) && (previous.direction == Direction.OUT))
        Left(ValidationError("Complicated Journey"))
      else if ((current.direction == Direction.IN) && (previous.direction == Direction.IN))
        Left(ValidationError("Complicated Journey"))
      else Right((): Unit)
  }

}

object ValidationService {

  def apply(): ValidationService = new ValidationService()
}

case class  ValidationError(msg: String)