package ordertaking.itaobuxiu.com.ordertaking

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.jetbrains.anko.find

/**
 * Created by chufengma on 2017/11/19.
 */
open class BaseActivity : AppCompatActivity() {

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

}