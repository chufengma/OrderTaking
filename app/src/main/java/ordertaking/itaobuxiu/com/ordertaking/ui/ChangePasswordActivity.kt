package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_change_password.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.UserApiService
import ordertaking.itaobuxiu.com.ordertaking.apis.networkWrap
import ordertaking.itaobuxiu.com.ordertaking.apis.toastInfo
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

class ChangePasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        useNormalBack()

        modify.setOnClickListener {
            var oldPasswordStr = oldPassword.text.toString()
            var newPasswordStr = newPassword.text.toString()
            var newPasswordConfirmStr = newConfirm.text.toString()

            if (oldPasswordStr.isNullOrBlank() || newPasswordStr.isNullOrBlank() || newPasswordConfirmStr.isNullOrBlank()) {
                toastInfo("请输入完整信息")
                return@setOnClickListener
            }

            if (!TextUtils.equals(newPasswordStr, newPasswordConfirmStr)) {
                toastInfo("新密码前后不一致")
                return@setOnClickListener
            }

            showLoading()
            networkWrap(Network.create(UserApiService::class.java)?.changePassword(oldPasswordStr,
                    newPasswordStr,
                    newPasswordConfirmStr))?.subscribe({ result ->
                toastInfo("修改成功")
                hideLoading()
                gotoMainActivity()
            }, { error ->
                hideLoading()
                toastInfo("修改失败：" + error.message)
            })
        }
    }
}
