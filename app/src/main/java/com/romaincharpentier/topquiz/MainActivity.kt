package com.romaincharpentier.topquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.romaincharpentier.topquiz.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private var mUser: User = User()
    private var mPreferences: SharedPreferences? = null

    val GAME_ACTIVITY_REQUEST_CODE = 42

    companion object {
        val PREF_KEY_RANK = "PREF_KEY_RANK"
    }

    val PREF_KEY_SCORE = "PREF_KEY_SCORE"
    val PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPreferences = getPreferences(Context.MODE_PRIVATE)

        play_btn.isEnabled = false
        greetUser()

        name_input.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                play_btn.isEnabled = s.toString().isNotEmpty()
            }
        })

        play_btn.setOnClickListener {
            val firstname = name_input.text.toString()
            mUser.firstname = firstname

            mPreferences!!.edit().putString(PREF_KEY_FIRSTNAME, mUser.firstname).apply()

            val gameActivity = Intent(this@MainActivity, GameActivity::class.java)
            startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE)
        }

        rank_btn.setOnClickListener {
            val rankActivity = Intent(this@MainActivity, RankActivity::class.java)
            startActivity(rankActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
            // Get the score from GameActivity
            val score: Int = data!!.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0)

            mPreferences!!.edit().putInt(PREF_KEY_SCORE, score).apply()
            greetUser(score)
            greetRankArray()
        }
    }

    private fun greetUser(score: Int = 0) {
        val firstname: String?  = mPreferences!!.getString(PREF_KEY_FIRSTNAME, null)

        if (null != firstname) {
            mUser.firstname = firstname
            mUser.score = score

            val fulltext = ("Bon retour, " + firstname
                    + "!\nTon dernier score était " + score
                    + ", feras-tu mieux cette fois-ci ?")
            greeting_txt.text = fulltext
            name_input.setText(firstname)
            name_input.setSelection(firstname.length)
            play_btn.isEnabled = true
        }
    }

    private fun greetRankArray() {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val json = Json(JsonConfiguration.Stable)

        val rankJson: String? = preferences.getString(PREF_KEY_RANK, "")
        var rankList: MutableList<User> = ArrayList()

        // Read the Json
        if (rankJson!=null && rankJson.isNotEmpty()){
            rankList = json.parse(User.serializer().list, rankJson).toMutableList()
        }

        // Add the new User and sort by score
        rankList.add(mUser)
        rankList.sortDescending()

        // On garde uniquement les 10 premiers et on sauvegarde sous format Json
        val jsonResult = json.stringify(User.serializer().list, rankList.toList().subList(0, min(rankList.size, 10)))
        preferences.edit().putString(PREF_KEY_RANK, jsonResult).commit()
    }
}
