package com.romaincharpentier.topquiz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.romaincharpentier.topquiz.model.User
import java.lang.Exception
import kotlin.math.roundToInt


class ListAdapter(context: Context, items: List<User>) :
    ArrayAdapter<User>(context, R.layout.itemlist_rank, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.itemlist_rank, parent, false)
        val user: User? = getItem(position)

        val idView: TextView = rowView.findViewById(R.id.id)
        val imageView: ImageView = rowView.findViewById(R.id.image)
        val nameView: TextView = rowView.findViewById(R.id.name)
        val scoreView: TextView = rowView.findViewById(R.id.score)

        nameView.text = user!!.firstname
        scoreView.text = user.score.toString()

        if (position in 0..2) {
            when (position) {
                0 -> {
                    imageView.setBackgroundResource(R.drawable.trophy_gold)
//                    idView.setTextColor(Color.rgb(218,165,32))
                }
                1 -> {
                    imageView.setBackgroundResource(R.drawable.trophy_silver)
//                    idView.setTextColor(Color.rgb(192,192,192))
                }
                2 -> {
                    imageView.setBackgroundResource(R.drawable.trophy_bronze)
//                    idView.setTextColor(Color.rgb(205,127,50))
                }
            }
            // Remove ranking, it is symbolized by trophies
            (idView.parent as ViewGroup).removeView(idView)
        } else {
            idView.text = Integer.valueOf(position + 1).toString()
            // We remove the image, there is no need because the rank is too low
            (imageView.parent as ViewGroup).removeView(imageView)
        }
        return rowView
    }

    private fun scaleImage(view: ImageView) { // Get bitmap from the the ImageView.
        val bitmap: Bitmap?
        bitmap = try {
            val drawing = view.drawable
            (drawing as BitmapDrawable).bitmap
        } catch (e: Exception) {
            return
        }
        // Get current dimensions AND the desired bounding box
        var width: Int
        width = try {
            bitmap!!.width
        } catch (e: NullPointerException) {
            throw NoSuchElementException("Can't find bitmap on given view/drawable")
        }
        var height = bitmap.height
        val bounding = dpToPx(250)
        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        val xScale = bounding.toFloat() / width
        val yScale = bounding.toFloat() / height
        val scale = if (xScale <= yScale) xScale else yScale
        // Create a matrix for the scaling and add the scaling data
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        // Create a new bitmap and convert it to a format understood by the ImageView
        val scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        width = scaledBitmap.width // re-use
        height = scaledBitmap.height // re-use
        val result = BitmapDrawable(scaledBitmap)
        // Apply the scaled bitmap
        view.setImageDrawable(result)
        // Now change ImageView's dimensions to match the scaled image
        val params = view.layoutParams as LinearLayout.LayoutParams
        params.width = width
        params.height = height
        view.layoutParams = params
    }

    private fun dpToPx(dp: Int): Int {
        val density: Float = context.resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }
}