package com.androidz.base_modules.baseAndroid.utils

/**
 * 作用描述: Collection 工具类
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/6/13
 * 修改日期 2022/6/13
 * 版权 pub
 */

fun <E> Collection<E>?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

fun <E> Collection<E>?.isNotEmpty(): Boolean = (this != null) && !isEmpty()