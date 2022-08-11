package cn.mashang.screen.base_moudle.lib_net

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 *
 * Created by rentianlong on 2020/6/30
 */
class KtResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if (rawType != RespResult::class.java) {
            return null
        }
        return null
    }
}