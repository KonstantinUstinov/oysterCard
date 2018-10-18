package oyster.card

import scala.collection.mutable.Map

class CardStorage {

  private val cards = Map.empty[Int, Card]

  def addCard(balance: Double) : Card = {
    val number = cards.size + 1
    val result = Card(number, balance)
    cards.put(number, result)
    result
  }

  def getCard(number: Int) : Option[Card] = cards.get(number)

  def updateBalance(number: Int, balance: Double) : Unit = {
    cards.get(number).map(_ => cards.put(number,  Card(number, balance)))
  }

}

object CardStorage {
  def apply(): CardStorage = new CardStorage()
}
