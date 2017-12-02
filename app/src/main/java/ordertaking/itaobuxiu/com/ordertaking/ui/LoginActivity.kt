package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import com.orhanobut.hawk.Hawk
import com.sdsmdg.tastytoast.TastyToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

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
                        gotoHome()
                    },
                    {
                        error ->
                        TastyToast.makeText(this, "登陆失败：" + error.message, TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                        hideLoading()
                    })
        }


    }
}
