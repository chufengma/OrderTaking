package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ordertaking.itaobuxiu.com.ordertaking.R

/**
 * Created by chufengma on 2017/11/19.
 */
class SellerFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_seller, null)
    }
}