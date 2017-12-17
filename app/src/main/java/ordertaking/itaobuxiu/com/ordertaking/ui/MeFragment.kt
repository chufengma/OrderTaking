package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_me.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import org.jetbrains.anko.dip

/**
 * Created by chufengma on 2017/11/19.
 */
class MeFragment: Fragment() {

    var isBuyer: Boolean = false
    var user: UserInfo? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_me, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        user = Hawk.get(LOGIN_USER)
        me_header.setBackgroundResource(R.drawable.iron_buy_detail_doing_bg)

        updateViews()
    }

    fun updateViews() {
       if (isLogin()) {
           unLoginLayout.visibility = View.GONE
           loginLayout.visibility = View.VISIBLE

           companyName.text = user?.buserInfo?.companyName
           bao.visibility = if (user?.buserInfo?.isGuaranteeUser == "1") View.VISIBLE else View.GONE
           cheng.visibility = if (user?.buserInfo?.isFaithUser == "1") View.VISIBLE else View.GONE

           salesMan.text = user?.sellManTel

           salesMan.setOnClickListener {
               (context as BaseActivity).showCall(user?.sellManTel)
           }

           switchBuyer.setOnClickListener {
               isBuyer = !isBuyer
               updateBuyerStatus()
           }
           updateBuyerStatus()

           networkWrap(Network.create(UserApiService::class.java)?.getUserLevel(""))
                   ?.subscribe({ result->
                       setupLevels(sellerHuoyueLayout, result.data.level!!)
                       setupLevels(buyerHuoyueLayout, result.data.day!!)
                   }, { error ->
                       // doNothing
                   })
       } else {
           unLoginLayout.visibility = View.VISIBLE
           loginLayout.visibility = View.GONE
       }

        youHui.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
        }

        fanWei.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
        }

        data.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
        }

        dataBuyer.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
        }

        rightDesc.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
        }

        myAsset.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
        }
    }

    fun updateBuyerStatus() {
        renzhen.visibility = if (isBuyer) View.GONE else View.VISIBLE
        sellerHuoyue.visibility = if (isBuyer) View.GONE else View.VISIBLE
        buyerHuoyue.visibility = if (isBuyer) View.VISIBLE else View.GONE
        me_header.setBackgroundResource(if (isBuyer) R.drawable.iron_buy_detail_doing_bg else R.drawable.iron_buy_detail_done_bg)
        sellerActions.visibility = if (isBuyer) View.GONE else View.VISIBLE
        dataBuyer.visibility = if (isBuyer) View.VISIBLE else View.GONE
        switchBuyer.text =  if (isBuyer) "切换到卖家" else "切换到买家"
    }

    fun setupLevels(layout: LinearLayout, day: String) {
        var levelArray = day.split("-")
        var level = levelArray[0].toInt()
        var levelNum = levelArray[1].toInt()

        for (i in 0..levelNum) {
            var image: ImageView = ImageView(context)
            var params = LinearLayout.LayoutParams(context.dip(12), context.dip(12))
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