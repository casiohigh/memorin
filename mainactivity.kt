package com.example.memorina

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.weight = 1.toFloat()

        val fronts = arrayOf(
            R.drawable.first, R.drawable.sec, R.drawable.thr, R.drawable.four,
            R.drawable.fi, R.drawable.six, R.drawable.sev, R.drawable.sev, R.drawable.eig
        )

        var firstCard: View = View(this)
        var openCardsCount = 0
        var guessedRightCount = 0

        val catViews = ArrayList<ImageView>()
        fronts.shuffle()

        val colorListener = View.OnClickListener() {
            when (openCardsCount) {
                0 -> {
                    firstCard = it;
                    openCardsCount++;
                    GlobalScope.launch(Dispatchers.Main) {
                        flipOver(
                            firstCard as ImageView,
                            fronts[it.tag.toString().toInt()]
                        )
                    }
                }
                1 -> {
                    if (it.tag == firstCard.tag) {
                        GlobalScope.launch(Dispatchers.Main) {
                            openCardsCount++;
                            flipOver(it as ImageView, fronts[it.tag.toString().toInt()])
                            openCardsCount = 0
                            guessedRightCount += 1
                        }
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            openCardsCount++;
                            flipOver(it as ImageView, fronts[it.tag.toString().toInt()])
                            delay(1000)
                            flipOver(firstCard as ImageView, R.drawable.squarecat)
                            flipOver(it as ImageView, R.drawable.squarecat)
                            openCardsCount = 0
                        }
                    }
                }
            }
        }

        for (i in 1..16) {
            catViews.add(
                ImageView(applicationContext).apply {
                    setImageResource(R.drawable.squarecat)
                    tag = (i - 1) / 2
                    layoutParams = params
                    setOnClickListener(colorListener)
                })
        }
        catViews.shuffle()

        val rows = Array(4, { LinearLayout(applicationContext) })
        var count = 0

        for (view in catViews) {
            val row: Int = count / 4
            rows[row].addView(view)
            count++
        }

        for (row in rows) {
            row.apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = params
            }
            layout.addView(row)
        }

        setContentView(layout)
    }

    suspend fun flipOver(v: ImageView, r: Int) {
        v.setImageResource(r)
        v.isClickable = !v.isClickable
    }
}
