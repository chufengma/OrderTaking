package ordertaking.itaobuxiu.com.ordertaking.engine

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by dev on 2017/11/20.
 */
object Network {

    var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                        GsonConverterFactory.create())
                .baseUrl("http://jiedan8.cn/")
                .build()

    }

    fun <T> create(service: Class<T>):T? {
        return retrofit?.create(service)
    }

}