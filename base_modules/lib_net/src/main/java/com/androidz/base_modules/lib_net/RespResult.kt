package cn.mashang.screen.base_moudle.lib_net

/**
 *
 * Created by rentianlong on 2020/6/30
 */
sealed class RespResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : RespResult<T>()
    data class Error(val exception: Exception) : RespResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}