package com.motycka.edu.lesson06

fun main() {

    val lightSide = starWarsCharacters.filter { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }
    val darkSide = starWarsCharacters.filterNot { it.fraction in setOf(Fraction.JEDI, Fraction.REBEL) }

    val pairs = lightSide.zip(darkSide)
    val (ls, ds) = pairs.unzip()

    pairs.zipWithNext { current, next ->
        listOf(
            current.first to next.first,
            current.second to next.second
        )
    }

    val results = pairs.matchWithFlatResults()

//    val resultsByFraction = results.unzip { (character, score) -> character.fraction to score }

    val scoreboardByCharacter = results
        .groupBy { it.first.name }
        .map { (_, results) ->
            results.reduce { acc, (character, score) ->
                character to score + acc.second
            }

        }

    scoreboardByCharacter.forEach { println(it)  }

    // using fold
    val scoreboardByFraction = results
        .fold(mutableMapOf<Fraction, Int>()) { acc, (character, score) ->
            acc[character.fraction] = acc.getOrDefault(character.fraction, 0) + score
            acc
        }

    scoreboardByFraction.forEach { println(it) }

//        .associate { it.first.fraction to it.second }


//        .map { (fraction, scores) ->
//            scores
//                .reduce { acc, (character, score) ->
//                StarWarsCharacter(character.fraction.name, character.fraction) to acc.second + scores
//            }
//        }


//    val scoreboard = results.groupBy { it.first }
//        .map { (character, scores) ->
//            character to scores.reduce { acc, (_, score) -> score }
//        }
//    }

//    val summary = results.fold(mutableMapOf<StarWarsCharacter, Int>()) { acc, (character, score) ->
//        acc[character] = acc.getOrDefault(character, 0) + score
//        acc
//    }

//    summary.forEach { t, u ->
//        println("${t.name} - $u")
//    }



//    val zip = characters.zipWithNext { current, next ->
//        println("${current.first} vs ${next.first}")
//    }
//
//    val xxx = characters.reduce { acc, (name, fraction) ->
//        println("$acc, $name")
//        acc
//    }

}

fun Int.power(n: Int): Int {
    return (1..n).fold(1) { acc, _ ->
        acc * this
    }
}
