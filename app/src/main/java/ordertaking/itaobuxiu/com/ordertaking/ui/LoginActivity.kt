package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_login.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.Response
import ordertaking.itaobuxiu.com.ordertaking.apis.UserLoginData
import ordertaking.itaobuxiu.com.ordertaking.engine.doLogin

//13921167896,13921167896
//15251698271,123456

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        useNormalBack()

        login.setOnClickListener {
            val mobileText = mobile.text.toString()
            val passwordText = password.text.toString()
            if (mobileText.isNullOrBlank() || passwordText.isNullOrBlank()) {
                TastyToast.makeText(this, "手机号或密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                return@setOnClickListener
            }
            showLoading()
            doLogin(mobileText, passwordText)?.subscribe({ result: Response<UserLoginData> ->
                        hideLoading()
                        finish()
                    },
                    {
                        error ->
                        TastyToast.makeText(this, "登陆失败：" + error.message, TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                        hideLoading()
                    })
        }
    }
}
