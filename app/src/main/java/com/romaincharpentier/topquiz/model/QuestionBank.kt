package com.romaincharpentier.topquiz.model

import java.util.Collections

class QuestionBank(val mQuestionList: List<Question>) {
    private var mNextQuestionIndex: Int = 0

    val question: Question
        get() {
            if (mNextQuestionIndex == mQuestionList.size) {
                mNextQuestionIndex = 0
            }
            return mQuestionList[mNextQuestionIndex++]
        }

    init {
        // Shuffle the question list
        mQuestionList.shuffled()

        mNextQuestionIndex = 0
    }
}