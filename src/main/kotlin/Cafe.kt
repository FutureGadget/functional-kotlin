class Cafe {
    fun buyCoffee(cc: CreditCard): Pair<Coffee, Charge> {
        val cup = Coffee()
        return cup to Charge(cc, cup.price)
    }

    fun buyCoffees(
        cc: CreditCard,
        n: Int
    ): Pair<List<Coffee>, Charge> {
        val purchases = List(n) { buyCoffee(cc) }
        val (coffees, charges) = purchases.unzip()
        return coffees to charges.reduce { acc, c -> acc.combine(c) }
    }
}

interface CreditCard
class Coffee(
    val price: Float = 5000f
)

fun List<Charge>.coalesce(): List<Charge> {
    return groupBy { it.cc }.values.map {
        it.reduce { a, b -> a.combine(b) }
    }
}

data class Charge(val cc: CreditCard, val amount: Float) {
    fun combine(other: Charge): Charge {
        return if (cc == other.cc) {
            Charge(cc, this.amount + other.amount)
        } else {
            throw IllegalArgumentException("Cannot combine charges to different cards")
        }
    }
}
