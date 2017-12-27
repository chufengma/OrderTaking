package ordertaking.itaobuxiu.com.ordertaking

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
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

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        useNormalBack()
    }

    fun useNormalBack() {
        if (findViewById(R.id.backArrow) == null) {
            return
        }
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

    fun gotoMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("loginOut", true)
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

    var currentCallNumTmp: String? = ""

    fun showCall(num: String?) {
        AlertDialog.Builder(this).setMessage(num)
                .setNegativeButton("取消", null)
                .setPositiveButton("呼叫", {dialog, which ->
                    doCall(num)
                    dialog.dismiss()
                }).show()
    }

    fun showCall(num: String?, text: String?) {
        AlertDialog.Builder(this).setMessage("${text}: ${num}")
                .setNegativeButton("取消", null)
                .setPositiveButton("呼叫", {dialog, which ->
                    doCall(num)
                    dialog.dismiss()
                }).show()
    }

    fun doCall(num: String?) {
        currentCallNumTmp = num
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num))
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            MainApplication.instance()?.getCurrentActivity()?.startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1000)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doCall(currentCallNumTmp)
        }
    }
}