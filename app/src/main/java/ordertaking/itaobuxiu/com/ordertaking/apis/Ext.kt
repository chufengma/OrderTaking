package ordertaking.itaobuxiu.com.ordertaking.apis

import android.app.Activity
import android.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import ordertaking.itaobuxiu.com.ordertaking.R

/**
 * Created by dev on 2017/11/22.
 */
fun Activity.configSwipe(swipe: SwipeRefreshLayout) {
    swipe.setColorSchemeColors(resources.getColor(R.color.main_blue))
}

fun Fragment.configSwipe(swipe: SwipeRefreshLayout) {
    swipe.setColorSchemeColors(resources.getColor(R.color.main_blue))
}