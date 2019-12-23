package com.romaincharpentier.topquiz

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.io.InputStream
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.*
import com.romaincharpentier.topquiz.model.Question
import com.romaincharpentier.topquiz.model.QuestionBank
import java.lang.Integer.min
import kotlin.collections.ArrayList

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private var mQuestionBank: QuestionBank = QuestionBank(ArrayList())
    private var mCurrentQuestion: Question? = null

    private var mScore: Int = 0
    private var mNumberOfQuestions: Int = 0

    private var mEnableTouchEvents: Boolean = true

    private val resultAnswers = ArrayList<String>()

    val QUESTIONS_NUMBER = 5

    companion object {
        // Variables statiques
        val BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE"
        val BUNDLE_STATE_SCORE = "currentScore"
        val BUNDLE_STATE_QUESTION = "currentQuestion"
    }

    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val context = this
        rv_animal_list.apply {
            // Creates a vertical Layout Manager
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            // Access the RecyclerView Adapter and load the data into it
            adapter = ScoreListAdapter(resultAnswers, context)
        }


        mQuestionBank = generateQuestions()

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE)
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION)
        } else {
            mScore = 0
            mNumberOfQuestions = min(QUESTIONS_NUMBER, mQuestionBank.mQuestionList.size)
        }

        mEnableTouchEvents = true

        answer1_btn.tag = 0
        answer2_btn.tag = 1
        answer3_btn.tag = 2
        answer4_btn.tag = 3

        answer1_btn.setOnClickListener(this)
        answer2_btn.setOnClickListener(this)
        answer3_btn.setOnClickListener(this)
        answer4_btn.setOnClickListener(this)

        mCurrentQuestion = mQuestionBank.question
        displayQuestion(mCurrentQuestion!!)
    }

    override fun onClick(v: View?) {
        val responseIndex = v?.tag

        if (responseIndex == mCurrentQuestion!!.answerIndex) {
            // Bonne réponse
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show()
            mScore++
            resultAnswers.add("V")
        } else {
            // Mauvaise réponse
            Toast.makeText(this, "Mauvaise réponse !", Toast.LENGTH_SHORT).show()
            resultAnswers.add("F")
        }

        mEnableTouchEvents = false

        Handler().postDelayed({
            // actualize score list
            rv_animal_list.apply {
                adapter = ScoreListAdapter(resultAnswers, context)
            }
            mEnableTouchEvents = true

            // S'il n'y a plus de questions, le jeu se termine, sinon on charge la question suivante
            if (--mNumberOfQuestions === 0) {
                endGame()
            } else {
                mCurrentQuestion = mQuestionBank.question
                displayQuestion(mCurrentQuestion!!)
            }
        }, 300) // Petit délai entre le changement des questions
    }


    private fun endGame() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Bien joué!")
            .setMessage("Ton score est $mScore")
            .setPositiveButton("OK") { _, _ ->
                // End the activity
                val intent = Intent()
                intent.putExtra(BUNDLE_EXTRA_SCORE, mScore)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun displayQuestion(question: Question) {
        question_txt.text = question.question
        answer1_btn.text = question.choiceList[0]
        answer2_btn.text = question.choiceList[1]
        answer3_btn.text = question.choiceList[2]
        answer4_btn.text = question.choiceList[3]
    }

    private fun generateQuestions(): QuestionBank {
        val inputStream:InputStream = assets!!.open("data.json")
        val jsonFile = inputStream.bufferedReader().use{it.readText()}

        val json = Json(JsonConfiguration.Stable)
        val questionList = json.parse(Question.serializer().list, jsonFile)

        return QuestionBank(questionList)
    }
}
