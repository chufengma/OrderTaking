package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_forget.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.toastInfo

class ForgetActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)
        useNormalBack()


        if (System.currentTimeMillis() - lastGetTime <= 1000 * 60) {
            var countDown = object: CountDownTimer(lastGetTime + 1000 * 60, 1000) {
                override fun onFinish() {
                    codeBtn.isEnabled = true
                }

                override fun onTick(millisUntilFinished: Long) {
                    codeBtn.text = "剩余${(System.currentTimeMillis() + 1000 * 60 - lastGetTime)/1000}秒"
                }
            }

            codeBtn.isEnabled = false
            countDown.start()
        } else {
            codeBtn.isEnabled = true
        }

        codeBtn.setOnClickListener {
            if (System.currentTimeMillis() - lastGetTime <= 1000 * 60) {
                return@setOnClickListener
            }
            codeBtn.isEnabled = false
            lastGetTime = System.currentTimeMillis()
            var countDown = object: CountDownTimer(lastGetTime + 1000 * 60, 1000) {
                override fun onFinish() {
                    codeBtn.isEnabled = true
                }

                override fun onTick(millisUntilFinished: Long) {
                    codeBtn.text = "剩余${(System.currentTimeMillis() + 1000 * 60 - lastGetTime)/1000}秒"
                }
            }
            countDown.start()
        }

        modify.setOnClickListener {
            var mobileStr = mobile.text.toString()
            var passwordStr = password.text.toString()
            var codeStr = code.text.toString()

            if (mobileStr.isNullOrBlank() || passwordStr.isNullOrBlank() || codeStr.isNullOrBlank()) {
                toastInfo("请输入完整信息")
                return@setOnClickListener
            }

//            if (!TextUtils.equals(newPasswordStr, newPasswordConfirmStr)) {
//                toastInfo("新密码前后不一致")
//                return@setOnClickListener
//            }
//
//            showLoading()
//            networkWrap(Network.create(UserApiService::class.java)?.changePassword(oldPasswordStr,
//                    newPasswordStr,
//                    newPasswordConfirmStr))?.subscribe({ result ->
//                toastInfo("修改成功")
//                hideLoading()
//                finish()
//            }, { error ->
//                hideLoading()
//                toastInfo("修改失败：" + error.message)
//            })
        }
    }

    companion object {
        var lastGetTime: Long = 0
    }
}
