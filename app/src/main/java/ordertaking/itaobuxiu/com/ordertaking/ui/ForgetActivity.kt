package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_forget.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.UserApiService
import ordertaking.itaobuxiu.com.ordertaking.apis.networkWrap
import ordertaking.itaobuxiu.com.ordertaking.apis.toastInfo
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

class ForgetActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)
        useNormalBack()


        if (System.currentTimeMillis() - lastGetTime <= 1000 * 60) {
            var countDown = object: CountDownTimer(1000 * 60, 1000) {
                override fun onFinish() {
                    codeBtn.isEnabled = true
                }

                override fun onTick(millisUntilFinished: Long) {
                    codeBtn.text = "剩余${(lastGetTime + 1000 * 60 - System.currentTimeMillis())/1000}秒"
                    if ((lastGetTime + 1000 * 60 - System.currentTimeMillis()/1000) <= 0) {
                        codeBtn.isEnabled = true
                    }
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
            var mobile = mobile.text.toString()
            if (mobile.isNullOrBlank()) {
                toastInfo("请输入手机号")
                return@setOnClickListener
            }
            networkWrap(Network.create(UserApiService::class.java)?.getSMSCode(mobile))?.subscribe({ result ->
                toastInfo("获取验证码成功")
                hideLoading()

                codeBtn.isEnabled = false
                lastGetTime = System.currentTimeMillis()
                var countDown = object: CountDownTimer(1000 * 60, 1000) {
                    override fun onFinish() {
                        codeBtn.isEnabled = true
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        codeBtn.text = "剩余${(lastGetTime + 1000 * 60 - System.currentTimeMillis())/1000}秒"
                    }
                }
                countDown.start()

            }, { error ->
                hideLoading()
                toastInfo("获取验证码失败：" + error.message)
            })
        }

        modify.setOnClickListener {
            var mobileStr = mobile.text.toString()
            var passwordStr = password.text.toString()
            var codeStr = code.text.toString()

            if (mobileStr.isNullOrBlank() || passwordStr.isNullOrBlank() || codeStr.isNullOrBlank()) {
                toastInfo("请输入完整信息")
                return@setOnClickListener
            }

            showLoading()
            networkWrap(Network.create(UserApiService::class.java)?.forgetPassword(mobileStr,
                    passwordStr,
                    codeStr))?.subscribe({ result ->
                toastInfo("修改成功")
                hideLoading()
                finish()
            }, { error ->
                hideLoading()
                toastInfo("修改失败：" + error.message)
            })
        }
    }

    companion object {
        var lastGetTime: Long = 0
    }
}
