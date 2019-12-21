package com.romaincharpentier.topquiz

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_rank.*

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import com.romaincharpentier.topquiz.model.User

class RankActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

        // retour à l'activité précédente dans la barre de navigation
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val json = Json(JsonConfiguration.Stable)

        val rankJson: String? = preferences.getString(MainActivity.PREF_KEY_RANK, "")
        var rankList: List<User> = ArrayList()

        // Lecture du Json
        if (rankJson!=null && rankJson.isNotEmpty()){
            rankList = json.parse(User.serializer().list, rankJson)
        }

        // Ajout de la liste à la vue
        val arrayAdapter = ListAdapter(this, rankList)
        rank_list.adapter = arrayAdapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
