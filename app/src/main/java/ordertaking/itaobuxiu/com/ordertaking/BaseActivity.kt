package ordertaking.itaobuxiu.com.ordertaking

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * Created by chufengma on 2017/11/19.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 系统 6.0 以上 状态栏白底黑字的实现方法
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

}