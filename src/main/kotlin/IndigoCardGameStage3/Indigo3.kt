package IndigoCardGameStage3

import IndigoCardGameStage3.Indigo3.PlayerUtils.Companion.cannotDealFurther
import IndigoCardGameStage3.Indigo3.PlayerUtils.Companion.needsRefill
import IndigoCardGameStage3.Indigo3.PlayerUtils.Companion.shuffleDeck
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.IllegalArgumentException
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.system.exitProcess


/** If computer plays first deal cards to and randomly pick a card
 * ***/

class Indigo3 {

     class PlayerUtils{
        companion object{
            //Cards on table we'll keep stacking on.
            var cardsOnTable: Stack<Card>? = null

            //variable declared for determine when player plays; First or not.
             var playfirst: Boolean? = null

            //Playing Cards Dealt to Player.
             var dealtCards: MutableList<Card?>?= null

            //Playing Cards Dealt to Computer
             var computerCards: MutableList<Card?>? = null

            //Deck as reference to what a full Deck looks like.
             val deckInstance = Deck().createdDeck

            //Actual game Deck , Take cards from ,Shuffle and Reset.
            val gameDeck = object: ArrayList<Card?>(){init{addAll(deckInstance.array)}}


            fun MutableList<Card?>?.needsRefill(): Boolean{ return this!!.size == 0 }

            fun refillingCardsPlayer(){
                //Refilling the players , and setting the next card to null. And will be removed. Next 6 cards.
                (0..5).forEach { num -> dealtCards?.add(num, gameDeck.get(num)); gameDeck[num] = null }
                gameDeck.removeIf { it == null }

            }

            fun refillingCardsComputer(){
                //Refilling the players , and setting the next card to null. And will be removed. Next 6 cards.
                (0..5).forEach { num -> computerCards?.add(num, gameDeck.get(num)); gameDeck[num] = null }
                gameDeck.removeIf { it == null }

            }

            //Initial shuffle of Deck
            fun ArrayList<Card?>.shuffleDeck() = apply{this.shuffle();}

            fun ArrayList<Card?>?.cannotDealFurther(): Boolean { return this!!.size == 0 }

        }
    }

    companion object : Game(){
        //Keeping track whose turn it is.
        var player_1turn: Boolean? = null

        @JvmStatic
        fun main(args: Array<String>) {

            println(name)
            setPlayerTurns()

            PlayerUtils.gameDeck.shuffleDeck()
            initialiseCards()

            if(PlayerUtils.playfirst!!){ dealInitialCardsToPlayer(); player_1turn = true}

            else if(PlayerUtils.playfirst!!.not()){ dealInitCardsToComputer();player_1turn = false }

            //takeTurnsPlaying()

//            while(true){
//                while(true) {
//                    try{
//                        computerPlaying2()
//                        break
//
//                    }catch(e: Exception) {
//                        println("error")
//                        break
//                    }
//                }
//                takeTurnsPlaying()
//            }

            while(true){
                takeTurnsPlaying()
            }

        }

        //Set-up who plays first : computer or player.
        private fun setPlayerTurns() {

            var notPlaying: Boolean = true
            val no_match: Regex = "no".toRegex()
            val yeah_match: Regex = "yes".toRegex()
            //println("Indigo Card Game")

            while(notPlaying) {
                try{
                    println("Play first?")

                    val choice = readLine()!!.toString()

                    if (choice.matches(no_match)) { PlayerUtils.playfirst = false;notPlaying = !notPlaying }

                    else if (choice.matches(yeah_match)) { PlayerUtils.playfirst = true;notPlaying = !notPlaying }

                    else { throw IllegalArgumentException() }

                }catch(e: IllegalArgumentException){ println() }
            }

        }

        private fun takeTurnsPlaying() {

            while(true){
                try {
                    while (player_1turn!!) { playing1() }

                    while (player_1turn!!.not()) { computerPlaying() }

                }catch(e: ArrayIndexOutOfBoundsException){println("Error aiob"); break }

                catch (e: IndexOutOfBoundsException){ println("Error iob");break }

                catch(e: Exception){println("Error ex") ; break}
            }

        }
    }
}

/**
 * @Game class serves as a utility class to the companion object of
 * class @Indigo3 which eventually inherits the methods of this.
 * ***/
open class Game  {
    companion object {
        const val name = "Indigo Card Game"
        const val EXIT = "exit"
        const val Finished_Game_Msg = "Game Over"

        /**
         * @playing1() human player plays and going on to Computer playing .
         *
         * */
        fun playing1(){

            //println("\n")

            //Check if player needs refill, while we have cards to deal to players.
            if(Indigo3.PlayerUtils.dealtCards!!.needsRefill() ){ Indigo3.PlayerUtils.refillingCardsPlayer()}
            println("${Indigo3.PlayerUtils.cardsOnTable?.size} cards on the table, and the top card is ${Indigo3.PlayerUtils.cardsOnTable?.peek()}")

            print("Cards in hand: ")
            Indigo3.PlayerUtils.dealtCards!!.forEachIndexed { index, card -> print(" ${index+1})$card") }.also{println()}

            println("Choose a card to play (1-${Indigo3.PlayerUtils.dealtCards?.size}):")

//            //Check if player needs refill, while we have cards to deal to players.
//            if(Indigo3.PlayerUtils.dealtCards?.needsRefill() == true){ Indigo3.PlayerUtils.refillingCardsPlayer()}

            val chosen = readLine()!!.toString()
            //when we cannot draw cards anymore from gameDeck then exit.
//            if(Indigo3.PlayerUtils.gameDeck.cannotDealFurther() && Indigo3.PlayerUtils.cardsOnTable?.size == 51 ){
//                Indigo3.PlayerUtils.cardsOnTable!!.push(Indigo3.PlayerUtils.dealtCards!![chosen.toInt()]).also{Indigo3.PlayerUtils.computerCards?.removeAt(chosen.toInt()-1)}
//                println("${Indigo3.PlayerUtils.cardsOnTable?.size} cards on the table, and the top card is ${Indigo3.PlayerUtils.cardsOnTable?.peek()}")
//                println(Finished_Game_Msg + ".")
//                exitProcess(0)
//            }

            if(chosen.toRegex().matches(EXIT)) {println(Finished_Game_Msg);exitProcess(0) }

            Indigo3.PlayerUtils.dealtCards?.apply{ Indigo3.PlayerUtils.cardsOnTable?.push(this[chosen.toInt()-1]);this.removeAt(chosen.toInt()-1); }

            if(Indigo3.PlayerUtils.cardsOnTable?.size == 52){
                println("${Indigo3.PlayerUtils.cardsOnTable?.size} cards on the table, and the top card is ${Indigo3.PlayerUtils.cardsOnTable?.peek()}")
                println(Finished_Game_Msg + ".")
                exitProcess(0)
            }

            Indigo3.player_1turn = false

            println("\n")
             computerPlaying()
        }

         fun computerPlaying() {

            if(Indigo3.PlayerUtils.computerCards?.needsRefill() == true){ Indigo3.PlayerUtils.refillingCardsComputer()}


             //Indigo3.PlayerUtils.computerCards?.forEach {  card: Card? -> println(card?.toString())  }

            //Let computer choose a random card in range.

             var rand_cardNum: Int = 0
             if(Indigo3.PlayerUtils.computerCards!!.size > 1)
                 rand_cardNum = Random.nextInt(0, Indigo3.PlayerUtils.computerCards!!.size)
             else if(Indigo3.PlayerUtils.computerCards!!.size ==1)
                 rand_cardNum = 0

            val card: Card? = Indigo3.PlayerUtils.computerCards?.get(rand_cardNum)

            // println("\n ${Indigo3.PlayerUtils.computerCards?.size}")
             println("${Indigo3.PlayerUtils.cardsOnTable?.size} cards on the table, and the top card is ${Indigo3.PlayerUtils.cardsOnTable?.peek()}")


             println("Computer plays ${card.toString()}")

             //when we cannot draw cards anymore from gameDeck then exit.
//             if(Indigo3.PlayerUtils.gameDeck.cannotDealFurther() && Indigo3.PlayerUtils.cardsOnTable?.size == 51){
//                 Indigo3.PlayerUtils.cardsOnTable!!.push(card).also{Indigo3.PlayerUtils.computerCards?.removeAt(rand_cardNum)}
//                 println("${Indigo3.PlayerUtils.cardsOnTable?.size} cards on the table, and the top card is ${Indigo3.PlayerUtils.cardsOnTable?.peek()}")
//                 println(Finished_Game_Msg +" ..")
//                 exitProcess(0)
//             }


            Indigo3.PlayerUtils.cardsOnTable!!.push(card).also{Indigo3.PlayerUtils.computerCards?.removeAt(rand_cardNum)}

             if(Indigo3.PlayerUtils.cardsOnTable?.size == 52){
                 println("${Indigo3.PlayerUtils.cardsOnTable?.size} cards on the table, and the top card is ${Indigo3.PlayerUtils.cardsOnTable?.peek()}")
                 println(Finished_Game_Msg + "..")
                 exitProcess(0)
             }

             Indigo3.player_1turn = false

            // Indigo3.PlayerUtils.computerCards?.forEach { print(" ${it?.toString()} ")  }.a

            playing1()
        }

        fun dealInitCardsToComputer(){
            //Initial Dealing to players and filter out nulls again.
            Indigo3.PlayerUtils.computerCards = mutableListOf<Card?>()
            (0 until 6).forEach { num -> Indigo3.PlayerUtils.computerCards?.add(num, Indigo3.PlayerUtils.gameDeck.get(num)); Indigo3.PlayerUtils.gameDeck[num] = null }
            Indigo3.PlayerUtils.gameDeck.removeIf { card -> card == null }

            Indigo3.PlayerUtils.computerCards!!.forEachIndexed { index, card -> print(" ${index+1})$card") }.also{println()}

            // And then deal to Player

            Indigo3.PlayerUtils.dealtCards = mutableListOf<Card?>()
            (0 until 6).forEach { num -> Indigo3.PlayerUtils.dealtCards?.add(num, Indigo3.PlayerUtils.gameDeck.get(num)); Indigo3.PlayerUtils.gameDeck[num] = null }
            Indigo3.PlayerUtils.gameDeck.removeIf { card -> card == null }

        }

         fun dealInitialCardsToPlayer() {

            //Initial Dealing to players and filter out nulls again.
            Indigo3.PlayerUtils.dealtCards = mutableListOf<Card?>()
            (0 until 6).forEach { num -> Indigo3.PlayerUtils.dealtCards?.add(num, Indigo3.PlayerUtils.gameDeck.get(num));Indigo3.PlayerUtils.gameDeck[num] = null }
            Indigo3.PlayerUtils.gameDeck.removeIf { card -> card == null }

           // Indigo3.PlayerUtils.dealtCards!!.forEachIndexed { index, card -> print(" ${index+1})$card") }.also{println()}

            //And then deal to computer.
            Indigo3.PlayerUtils.computerCards = mutableListOf<Card?>()
            (0 until 6).forEach { num -> Indigo3.PlayerUtils.computerCards?.add(num, Indigo3.PlayerUtils.gameDeck.get(num)); Indigo3.PlayerUtils.gameDeck[num] = null }
            Indigo3.PlayerUtils.gameDeck.removeIf { card -> card == null }

        }

        //Set cards initially on the table.
        fun initialiseCards(){

            //Initialise cards on table stack.
            Indigo3.PlayerUtils.cardsOnTable = Stack()
            var index: Int = 0
            while(index < 4){
                Indigo3.PlayerUtils.cardsOnTable?.push(Indigo3.PlayerUtils.gameDeck[index])
                Indigo3.PlayerUtils.gameDeck[index] = null
                index++
            }

            //Filter out the null values in Deck so that we can get to the next non-null value to deal.
            Indigo3.PlayerUtils.gameDeck.removeIf { card -> card == null }

            print("Initial cards on the table: ")
            Indigo3.PlayerUtils.cardsOnTable!!.forEach{ card: Card? -> print("${card.toString()} ") }.also{println("\n")}
        }
    }

}

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



/** fun computerPlaying2(){
if(Indigo3.PlayerUtils.computerCards?.needsRefill() == true){ Indigo3.PlayerUtils.refillingCardsComputer()}

Indigo3.PlayerUtils.computerCards?.forEach {  card: Card? -> println(card?.toString())  }

println("\n")
println("${Indigo3.PlayerUtils.cardsOnTable?.size} cards on the table, and the top card is ${Indigo3.PlayerUtils.cardsOnTable?.peek()}")

print("\nCards in hand computer: ")
Indigo3.PlayerUtils.computerCards!!.forEachIndexed { index, card -> print(" ${index+1})$card") }.also{println()}

println("Choose a card to play (1-${Indigo3.PlayerUtils.computerCards?.size}):")

val chosen = readLine()!!.toInt()

Indigo3.PlayerUtils.computerCards?.apply{ Indigo3.PlayerUtils.cardsOnTable?.push(this[chosen-1]);this.removeAt(chosen-1); }
}***/