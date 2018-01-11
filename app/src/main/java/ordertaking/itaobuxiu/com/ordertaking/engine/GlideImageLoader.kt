package ordertaking.itaobuxiu.com.ordertaking.engine

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader
import ordertaking.itaobuxiu.com.ordertaking.R

/**
 * Created by dev on 2017/11/20.
 */

object GlideImageLoader : ImageLoader() {

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(context).load(path).placeholder(R.drawable.home_ad_defalut).centerCrop().into(imageView)
    }



}