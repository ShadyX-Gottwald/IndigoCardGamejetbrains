import TestPad.Utils.Companion.noNumbers

/**Objectives  -> setup who plays First. Cards on table formatted, Cards for player and computer
 *  **/
//fun main (){
//
//    val m = "(yes|no|maybe)".toRegex()
//
//    val s = "maybe"
//
//    println(m.matches(s))
//
//    val stack: Stack<Int> = Stack()
//    stack.push(5)
//    stack.push(7)
//    stack.push(8)
//
//    println(stack.peek())
//
//
//    stack.forEach{ print("$it ")}
//    println()
//
//    var optional: Optional<String>  = Optional.of("Hello")
//   // optional.orElseThrow()
//  //  println( optional.orElseThrow())
//
//
//
//}

class TestPad{
    class Utils {
        companion object{
            val NUMBER = 12

            fun getNumber(): Int{
                return NUMBER
            }

            fun MutableList<Int>.noNumbers(): Boolean{ return this.size == 0 }
        }

    }
//    companion object : Game() {
//        @JvmStatic
//        fun main(args: Array<String>){
//            println( Utils.getNumber())
//
//            val list = mutableListOf<Int>()
//
//            println(list.noNumbers())
//            println(X)
//            println(Players.value)
//        }
    //}
}

//open class Game : Util(){
//    companion object{
//        val X = "Game"
//    }
//}
//
//open class Util{
//    object Players{
//        const val value = 34
//    }
//}

