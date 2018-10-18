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

  def validateJourney(current: Journey, previous: Journey, card: Card): Either[ValidationError, Unit] = {
    validationService.validateBalance(card, current.transport).
      flatMap(_ => validationService.verifyTripIsValid(current, previous))
  }

  def doTubeJourney(journey: Journey, card: Card) : Either[ValidationError, (Card, Journey)] = {
    journeyStorage.getLastTube(card.number) match {
      case Some(previous) =>
        validateJourney(journey, previous, card)
          .right.map( _ => calculationService.updateTubeCost(journey, Some(previous)))
          .right.map(j => paymentService.payment(j, card))
      case None =>
        validationService.validateBalance(card, journey.transport)
          .right.map( _ => calculationService.updateTubeCost(journey, None))
          .right.map(j => paymentService.payment(j, card))
    }
  }

  def doJourney(journey: Journey, card: Card) : Either[ValidationError, (Card, Journey)] = {
    journey.transport match {
      case Transport.Bus => doBusJourney(journey, card)
      case Transport.Tube => doTubeJourney(journey, card)
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