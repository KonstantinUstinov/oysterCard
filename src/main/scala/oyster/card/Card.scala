package oyster.card

case class Card(number: Int, balance: Double) {
  def updateBalance(amount: Double) : Card = copy(balance = balance - amount)
}
