package com.romaincharpentier.topquiz

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.romaincharpentier.topquiz.model.TypeScore
import kotlinx.android.synthetic.main.itemlist_score.view.*

class ScoreListAdapter(private val items: Array<TypeScore>, private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemlist_score, parent, false))
    }

    // Gives a color corresponding to the answer (true or wrong)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = when (items[position]) {
            TypeScore.TRUE_ANSWER -> R.color.rightAnswer
            TypeScore.WRONG_ANSWER -> R.color.wrongAnswer
            TypeScore.EMPTY_ANSWER -> R.color.emptyAnswer
        }
        (holder.scoreList as GradientDrawable).setColor(ContextCompat.getColor(context, color))
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the view
    val scoreList: Drawable = view.score.background
}