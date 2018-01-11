package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import cn.jpush.android.api.JPushInterface
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_me.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.MainApplication
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

        me_header.setPadding(0, getStatusBarHeight(context), 0, 0)
        right.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
            startActivity(Intent(context, SettingActivity::class.java))
        }

        unLoginLayout.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).gotoLoginActivity()
            }
        }

        configSwipe(meSwipeRefreshLayout)

        meSwipeRefreshLayout.setOnRefreshListener {
            updateViews()
        }
    }

    fun updateViews() {
       if (isLogin()) {
           unLoginLayout.visibility = View.GONE
           loginLayout.visibility = View.VISIBLE

           companyName.text = user?.buserInfo?.companyName

           var updateRezhen = {
               bao.visibility = if (user?.buserInfo?.isGuaranteeUser == "1") View.VISIBLE else View.GONE
               cheng.visibility = if (user?.buserInfo?.isFaithUser == "1") View.VISIBLE else View.GONE
               noRenzhen.visibility = if (user?.buserInfo?.isGuaranteeUser == "1" || user?.buserInfo?.isFaithUser == "1") View.GONE else View.VISIBLE
           }

           if (user?.sellManTel.isNullOrBlank()) {
               salesMan.text = "0510-81812186"
               salesMan.setOnClickListener {
                   (context as BaseActivity).showCall("0510-81812186", "客服")
               }
           } else {
               salesMan.text = user?.sellManTel
               salesMan.setOnClickListener {
                   (context as BaseActivity).showCall(user?.sellManTel, user?.sellManName)
               }
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
                       meSwipeRefreshLayout.isRefreshing = false
                   }, { error ->
                       // doNothing
                       meSwipeRefreshLayout.isRefreshing = false
                   })

           networkWrap(Network.create(UserApiService::class.java)?.getUserInfo("123"))?.subscribe({ result: Response<UserInfo> ->
               Hawk.put(LOGIN_USER, result.data)
               Hawk.put(LAST_USER_MOBILE, result.data.mobile)

               user = result.data

               updateRezhen()
           }, {})

       } else {
           unLoginLayout.visibility = View.VISIBLE
           loginLayout.visibility = View.GONE
       }

        youHui.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
            startActivity(Intent(context, PostChargeActivity::class.java))
        }

        fanWei.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }

            startActivity(Intent(context, BusinessScopeActivity::class.java))
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
            startActivity(Intent(context, DataActivity::class.java))
        }

        data.setOnClickListener  {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
            startActivity(Intent(context, DataActivity::class.java))
        }

        rightDesc.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
            gotoWebView(context, "权益说明", "http://tbxoss.oss-cn-hangzhou.aliyuncs.com/html/rights.html")
        }

        myAsset.setOnClickListener {
            if (!isLogin()) {
                (context as BaseActivity).showLoginDialog()
                return@setOnClickListener
            }
        }
    }


    override fun onResume() {
        super.onResume()
        meSwipeRefreshLayout.isEnabled = isLogin()
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
        layout.removeAllViews()
        var levelArray = day.split("-")
        var level = levelArray[0].toInt()
        var levelNum = levelArray[1].toInt()

        for (i in 0..(levelNum-1)) {
            var image: ImageView = ImageView(context)
            var params = LinearLayout.LayoutParams(context.dip(12), context.dip(12))
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