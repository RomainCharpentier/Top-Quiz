package com.romaincharpentier.topquiz.model

import kotlinx.serialization.Serializable

@Serializable
class User : Comparable<User> {

    var firstname: String = ""
    var score: Int = 0

    override fun toString(): String {
        return "$firstname : $score"
    }

    override fun compareTo(other: User): Int {
        return score.compareTo(other.score)
    }
}
