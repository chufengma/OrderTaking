package ordertaking.itaobuxiu.com.ordertaking

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * Created by chufengma on 2017/11/19.
 */

class TestLayout2 : LinearLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("TouchTest", "TestLayout2 onInterceptTouchEvent:" + ev?.action)
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("TouchTest", "TestLayout2 dispatchTouchEvent:" + ev?.action)
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("TouchTest", "TestLayout2 onTouchEvent:" + event?.action)
        return super.onTouchEvent(event)
    }

}
