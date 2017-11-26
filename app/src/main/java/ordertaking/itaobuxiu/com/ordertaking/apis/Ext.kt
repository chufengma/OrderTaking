package ordertaking.itaobuxiu.com.ordertaking.apis

import android.app.Activity
import android.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.ui.toast
import org.jetbrains.anko.contentView

/**
 * Created by dev on 2017/11/22.
 */
fun Activity.configSwipe(swipe: SwipeRefreshLayout) {
    swipe.setColorSchemeColors(resources.getColor(R.color.main_blue))
}

fun Fragment.configSwipe(swipe: SwipeRefreshLayout) {
    swipe.setColorSchemeColors(resources.getColor(R.color.main_blue))
}

fun Activity.toastInfo(text: String) {
    toast(this, text, TastyToast.INFO)
}