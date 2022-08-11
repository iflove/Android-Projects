package cn.mashang.screen.base_moudle.lib_net

import com.google.gson.Gson

/**
 *
 * Created by rentianlong on 2020/7/6
 */

val gson = Gson()

fun Any.toGson() = gson.toJson(this)

inline fun <reified T> String.fromJson(): T = gson.fromJson(this, T::class.java)