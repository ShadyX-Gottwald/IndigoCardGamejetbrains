import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class Indigo2{

    companion object {
        //Commands
        private const val GET: String = "get"
        private const val RESET: String = "reset"
        private const val EXIT: String = "exit"
        private const val SHUFFLE: String = "shuffle"

        //Deck as reference to what a full Deck looks like.
        private val deckInstance = Deck().createdDeck

        //Actual game Deck , Take cards from ,Shuffle and Reset
        private val gameDeck = object: ArrayList<Card?>(){init{addAll(deckInstance.array)}}

        //Playing Cards Dealt to Player
        private var dealtCards: MutableList<Card?>?= null

        @JvmStatic
        fun main(args: Array<String>) {
            //Set-up game variables

            do {
                try {
                    println("Choose an action (reset, shuffle, get, exit):")
                    val input = readLine()!!.toString()
                    menuPrompt(input = input) }
                catch(e: NumberFormatException){ println("Invalid number of cards.") }
                catch (e: IllegalArgumentException) { println("Wrong action. ") }
                catch(e: InSufficientCardsToDrawFrom){println(e.message)}
            }while(true)
        }
        private fun menuPrompt(input: String){
            when{
                input.equals(EXIT)-> {println("Bye");exitProcess(0)}
                input.equals(RESET) -> {gameDeck.resetDeck()}
                input.equals(GET) -> {get()}
                input.equals(SHUFFLE) -> {gameDeck.shuffleDeck()}
                else -> {throw IllegalArgumentException()}
            }
        }

        /**Logic to Reset an Established Deck to original size**/
        private fun ArrayList<Card?>.resetDeck(){
            dealtCards = null
            this.apply{clear();addAll(deckInstance.array)}
            println("Card deck is reset.")
        }

        /**Logic to shuffle this gameDeck*/
        private fun ArrayList<Card?>.shuffleDeck() = apply{this.shuffle(); println("Card deck is shuffled.")}

        /**Logic of Dealt cards **/
        private fun dealCards(cards: Int): MutableList<Card?> {
            val cards_ImDealt = MutableList<Card?>(cards){null}
            var count: Int = 0

            //Gimme playing Cards!! Init list of cards , when all null then create @cards_ImDealt
            //Else try Resizing array
            while (count < cards) {
                if (gameDeck[count] != null) {
                    cards_ImDealt[count] = gameDeck[count]
                    gameDeck[count] = null
                }
                count++
            }
            return cards_ImDealt
        }

        private fun get(){
            println("Number of cards:")
            when(val numCardsToGet: Int = readLine()!!.toInt()){
                in 1..52 -> { checkNumberOfCardsToBeDealt(numCardsToGet) }
                !in 1..52 -> {throw NumberFormatException()}
            }
        }


        private fun checkNumberOfCardsToBeDealt(cards: Int){
            //Size of dealtCards
            val SIZE = dealtCards?.size

            //Filter-out nulls to get the next non-null card to Deal
            val deckWithNoNulls: MutableList<Card> = gameDeck.filterNotNull()
                .toList()
                .toMutableList()

            //Make sure our dealtCards are not Null
            if(dealtCards == null){ dealtCards =  dealCards(cards = cards) }
            else {
                var count: Int = 0
                while(count < cards){
                    if(cards > deckWithNoNulls.size){
                        throw InSufficientCardsToDrawFrom("The remaining cards are insufficient to meet the request.")
                    }
                    dealtCards!!.add(SIZE ?: 0,deckWithNoNulls[count])
                    count++
                }
            }
            dealtCards!!.forEach{print("$it ")}
            println()
        }
    }
}

class InSufficientCardsToDrawFrom(override val message: String): Exception(message)

enum class Suites(val symbol: String){
    Diamond("♦"),
    Heart("♥"),
    Club("♣"),
    Spade("♠")
}

enum class Ranks(val char: String){
    //Rank 2 - 10 ; A J Q K
    Ace("A"), TWO("2"), THREE("3"),
    FOUR("4"), FIVE("5"), SIX("6") ,
    SEVEN("7") , EIGHT("8"), NINE("9"),
    TEN("10"), Jack("J"), Queen("Q"),
    King("K"),

}
class Card(private val rank: Ranks, private val suites: Suites){
    override fun toString(): String {
        return rank.char+suites.symbol
    }
}
class Deck {

    val createdDeck =  Cards

    object Cards{ val array  = arrayListOf<Card>() }
    init{ createAllCards() }

    private fun createAllCards(): ArrayList<Card>{
        for(card in Ranks.values().indices){
            Cards.array.add(Card(Ranks.values()[card],Suites.Spade))
        }
        for(card in Ranks.values().indices){
            Cards.array.add(Card(Ranks.values()[card],Suites.Heart))
        }
        for(card in Ranks.values().indices){
            Cards.array.add(Card(Ranks.values()[card],Suites.Diamond))
        }
        for(card in Ranks.values().indices){
            Cards.array.add(Card(Ranks.values()[card],Suites.Club))
        }
        return Cards.array
    }

}
