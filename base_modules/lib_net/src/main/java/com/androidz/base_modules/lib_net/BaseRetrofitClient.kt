package cn.mashang.screen.base_moudle.lib_net

import com.androidz.base_modules.lib_net.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * Created by rentianlong on 2020/6/28
 */
abstract class BaseRetrofitClient() {

    companion object {
        private const val TIME_OUT = 10L
    }

    val okClient: OkHttpClient by lazy { createOkClient({}, {}) }
    val retrofitPool = mutableMapOf<String, Retrofit>()

    val logging: HttpLoggingInterceptor by lazy { HttpLoggingInterceptor(logger) }

    open val logger: HttpLoggingInterceptor.Logger
        get() = HttpLoggingInterceptor.Logger.DEFAULT

    init {
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    open fun createOkClient(
        head: OkHttpClient.Builder.() -> Unit,
        tail: OkHttpClient.Builder.() -> Unit
    ): OkHttpClient = OkHttpClient.Builder().apply(head).addInterceptor(logging)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS).apply(tail).build()

    inline fun <reified T> createClientServer(
        baseUrl: String,
        retrofit: Retrofit = retrofit(baseUrl)
    ): T {
        return retrofit.newBuilder()
            .baseUrl(baseUrl)
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build().create(T::class.java)
    }

    inline fun <reified T> createClientServer(
        baseUrl: String,
        client: OkHttpClient,
        retrofit: Retrofit = retrofit(baseUrl)
    ): T {
        return retrofit.newBuilder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(T::class.java)
    }


    fun retrofit(baseUrl: String): Retrofit =
        retrofitPool[baseUrl] ?: Retrofit.Builder().baseUrl(baseUrl).build()
            .apply { retrofitPool[baseUrl] = this }

    fun onCleared() {
        retrofitPool.clear()
    }

}