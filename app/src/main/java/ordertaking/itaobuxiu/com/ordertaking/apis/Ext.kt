package ordertaking.itaobuxiu.com.ordertaking.apis

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import com.sdsmdg.tastytoast.TastyToast
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.ui.toast

/**
 * Created by dev on 2017/11/22.
 */
fun Activity.configSwipe(swipe: SwipeRefreshLayout) {
    swipe.setColorSchemeColors(resources.getColor(R.color.main_blue))
}

fun Fragment.configSwipe(swipe: SwipeRefreshLayout) {
    swipe.setColorSchemeColors(resources.getColor(R.color.main_blue))
}

fun android.support.v4.app.Fragment.configSwipe(swipe: SwipeRefreshLayout) {
    swipe.setColorSchemeColors(resources.getColor(R.color.main_blue))
}

fun Activity.toastInfo(text: String) {
    toast(this, text, TastyToast.INFO)
}

fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.getResources().getDimensionPixelSize(resourceId)
    }
    return result
}