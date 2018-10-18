package oyster

import oyster.card.{Card, CardStorage}
import oyster.journey.{Journey, JourneyStorage, Transport}
import oyster.payment.{PaymentService, ValidationError, ValidationService}
import oyster.zone.CalculationService

class Service(cardStorage: CardStorage, journeyStorage: JourneyStorage, calculationService: CalculationService, validationService: ValidationService, paymentService: PaymentService) {

  def createCard(amount: Double): Card = cardStorage.addCard( amount)

  def journeyHistory(card: Card) = journeyStorage.getHistory(card.number)

  def doBusJourney(journey: Journey, card: Card) : Either[ValidationError, (Card, Journey)] = {
    validationService.validateBalance(card, journey.transport)
      .right.map( _ => calculationService.updateBusCost(journey))
      .right.map(j => paymentService.payment(j, card))

  }

  def doTubeJourney(journey: Journey, card: Card) : Either[ValidationError, (Card, Journey)] = {
    journeyStorage.getLastTube(card.number) match {
      case Some(previous) =>
      case None =>
        validationService.validateBalance(card, journey.transport)
          .right.map( _ => calculationService.updateTubeCost(journey, None))
          .right.map(j => paymentService.payment(j, card))
    }
  }

  def doJourney(journey: Journey, card: Card) : Either[ValidationError, (Card, Journey)] = {
    journey.transport match {
      case Transport.Bus => doBusJourney(journey, card)
      case Transport.Tube => Left(ValidationError(""))
    }
  }

}

object Service {
  def apply(cardStorage: CardStorage,
            journeyStorage: JourneyStorage,
            calculationService: CalculationService,
            validationService: ValidationService,
            paymentService: PaymentService): Service =
    new Service(cardStorage, journeyStorage, calculationService, validationService, paymentService)
}