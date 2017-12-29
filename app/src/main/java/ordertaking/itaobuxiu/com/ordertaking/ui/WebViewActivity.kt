package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_web_view.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R

class WebViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        titleView.text = intent.getStringExtra("title")
        var url = intent.getStringExtra("url")
        webView.loadUrl(url)
    }
}
