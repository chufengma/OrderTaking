package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_seller_info.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import org.jetbrains.anko.dip

class SellerInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_info)
        var huoyueStr = intent.getStringExtra("huoyueStr")
        var companyNameStr = intent.getStringExtra("companyNameStr")
        var chengStr = intent.getStringExtra("chengStr")
        var baoStr = intent.getStringExtra("baoStr")
        var contactStr = intent.getStringExtra("contactStr")
        var contactTelStr = intent.getStringExtra("contactTelStr")
        var proInfoStr = intent.getStringExtra("proInfoStr")
        var placeStr = intent.getStringExtra("placeStr")

        place.text = placeStr
        proInfo.text = proInfoStr
        contactTel.text = contactTelStr
        contact.text = contactStr
        place.text = placeStr
        companyName.text = companyNameStr

        bao.visibility = if (baoStr == "1") View.VISIBLE else View.GONE
        cheng.visibility = if (chengStr == "1") View.VISIBLE else View.GONE
        noRenzheng.visibility = if (baoStr == "1" || chengStr == "1") View.GONE else View.VISIBLE

        setupLevels(huoyueLayout, huoyueStr)

        contactTel.setOnClickListener {
            showCall(contactTelStr, contactStr)
        }

    }


    fun setupLevels(layout: LinearLayout, day: String) {
        layout.removeAllViews()
        var levelArray = day.split("-")
        var level = levelArray[0].toInt()
        var levelNum = levelArray[1].toInt()

        for (i in 0..(levelNum-1)) {
            var image: ImageView = ImageView(this)
            var params = LinearLayout.LayoutParams(dip(12), dip(12))
            image.layoutParams = params

            when (level) {
                1 -> image.setImageResource(R.drawable.ic_level_one)
                2 -> image.setImageResource(R.drawable.ic_level_two)
                3 -> image.setImageResource(R.drawable.ic_level_three)
            }

            layout.addView(image)
        }
    }
}
