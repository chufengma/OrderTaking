package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Intent
import android.os.Bundle
import cn.jpush.android.api.JPushInterface
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_setting.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.LOCAL_REQUESTS
import ordertaking.itaobuxiu.com.ordertaking.apis.LOGIN_USER
import ordertaking.itaobuxiu.com.ordertaking.apis.USER_LOGIN_INFO
import ordertaking.itaobuxiu.com.ordertaking.apis.gotoWebView

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        useNormalBack()

        quit.setOnClickListener {
            Hawk.delete(USER_LOGIN_INFO)
            Hawk.delete(LOGIN_USER)
            Hawk.delete(LOCAL_REQUESTS)

            JPushInterface.stopPush(this)

            gotoMainActivity()
        }

        baseInfo.setOnClickListener {
            startActivity(Intent(this, BaseUserInfoActivity::class.java))
        }


        account.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        about.setOnClickListener {
            gotoWebView(this, "联系我们", "http@ //jiedan8.cn/apphtml/aboutus.html")

        }

    }
}
