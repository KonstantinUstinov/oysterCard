package oyster.payment

import oyster.card.{Card, CardStorage}
import oyster.journey.{Journey, JourneyStorage}


class PaymentService(cardStorage: CardStorage, journeyStorage: JourneyStorage) {

  def payment(journey: Journey, card: Card) : (Card, Journey) = {

    val newCard  =  card.updateBalance(journey.cost)
    journeyStorage.addJourney(newCard.number, journey)
    cardStorage.updateBalance(newCard.number, newCard.balance)

    (newCard, journey)
  }

}

object PaymentService {
  def apply(cardStorage: CardStorage, journeyStorage: JourneyStorage): PaymentService =
    new PaymentService(cardStorage, journeyStorage)
}
