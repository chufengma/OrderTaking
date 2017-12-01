package ordertaking.itaobuxiu.com.ordertaking

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kaopiz.kprogresshud.KProgressHUD
import ordertaking.itaobuxiu.com.ordertaking.engine.MainApplication
import ordertaking.itaobuxiu.com.ordertaking.ui.LoginActivity
import org.jetbrains.anko.find


/**
 * Created by chufengma on 2017/11/19.
 */
open class BaseActivity : AppCompatActivity() {

    var ks : KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 系统 6.0 以上 状态栏白底黑字的实现方法
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    fun useNormalBack() {
        find<View>(R.id.backArrow)?.setOnClickListener {
            onBackPressed()
        }
    }

    fun showLoading() {
        if (ks == null) {
            ks = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show()
        } else {
            ks?.show()
        }
    }

    fun gotoLoginActivity() {
        var intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun gotoHome() {
        var intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    fun hideLoading() {
        ks?.dismiss()
    }

    fun showLoginDialog() {
        AlertDialog.Builder(this)
                .setMessage("您还没有登陆，请先登陆")
                .setPositiveButton("去登陆") { dialog, which ->
//                    startActivity(Intent(this, LoginActivity::class.java))
                    gotoLoginActivity()
                    gotoLoginActivity()
                    gotoLoginActivity()
                    gotoLoginActivity()

                }
                .setNegativeButton("取消") { dialog, which -> }.show()
    }

    public override fun onResume() {
        super.onResume()
        MainApplication.instance()?.enterActivity(this)
    }
}