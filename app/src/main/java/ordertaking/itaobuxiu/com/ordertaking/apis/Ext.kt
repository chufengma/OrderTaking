package ordertaking.itaobuxiu.com.ordertaking.apis

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import com.philjay.circledisplay.CircleDisplay
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


fun android.support.v4.app.Fragment.configCircle(cd: CircleDisplay) {
    cd.setColor(resources.getColor(R.color.main_blue))
    cd.setUnit("%")
    cd.setFormatDigits(2)
    cd.setValueWidthPercent(20f)
    cd.setAnimDuration(300)
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