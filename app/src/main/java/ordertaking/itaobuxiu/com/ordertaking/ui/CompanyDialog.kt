package ordertaking.itaobuxiu.com.ordertaking.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.company_dialog.*
import ordertaking.itaobuxiu.com.ordertaking.R
import org.jetbrains.anko.dip

/**
 * Created by dev on 2017/12/25.
 */
class CompanyDialog(val name: String?, val lavel: String?, context: Context?) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_dialog)
        companyName.setText(name)
        setupLevels(levelLayout, this!!.lavel!!)

        ok.setOnClickListener { dismiss() }
    }

    fun setupLevels(layout: LinearLayout, day: String) {
        var levelArray = day.split("-")
        var level = levelArray[0].toInt()
        var levelNum = levelArray[1].toInt()

        for (i in 0..(levelNum-1)) {
            var image: ImageView = ImageView(context)
            var params = LinearLayout.LayoutParams(context.dip(14), context.dip(14))
            image.layoutParams = params

            when (level) {
                1 -> image.setImageResource(R.drawable.ic_level_three)
                2 -> image.setImageResource(R.drawable.ic_level_two)
                3 -> image.setImageResource(R.drawable.ic_level_one)
            }

            layout.addView(image)
        }
    }
}
