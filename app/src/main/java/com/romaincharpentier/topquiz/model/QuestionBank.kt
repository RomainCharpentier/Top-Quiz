package com.romaincharpentier.topquiz.model

import java.util.Collections

class QuestionBank(val mQuestionList: List<Question>) {
    private var mNextQuestionIndex: Int = 0

    // Ensure we loop over the questions
    // Please note the post-incrementation
    val question: Question
        get() {
            if (mNextQuestionIndex == mQuestionList.size) {
                mNextQuestionIndex = 0
            }
            return mQuestionList[mNextQuestionIndex++]
        }

    init {
        // Shuffle the question list
        Collections.shuffle(mQuestionList)

        mNextQuestionIndex = 0
    }
}