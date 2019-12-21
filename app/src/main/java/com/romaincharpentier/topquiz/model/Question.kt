package com.romaincharpentier.topquiz.model

import kotlinx.serialization.Serializable

@Serializable
class Question(val question: String, val choiceList: List<String>, val answerIndex: Int) {

    override fun toString(): String {
        return "Question{" +
                "mQuestion='" + question + '\''.toString() +
                ", mChoiceList=" + choiceList +
                ", mAnswerIndex=" + answerIndex +
                '}'.toString()
    }
}