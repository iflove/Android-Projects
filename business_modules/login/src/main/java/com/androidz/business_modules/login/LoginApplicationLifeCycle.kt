package com.androidz.business_modules.login

import com.androidz.base_modules.lib_baseAndroid.ApplicationLifeCycle
import com.androidz.base_modules.lib_baseAndroid.BaseApplicationLifeCycle
import com.androidz.base_modules.lib_logger.Logg
import com.didi.drouter.annotation.Service
import com.didi.drouter.api.Extend

/**
 * 作用描述: 组件描述
 * 组件描述: #基础组件 #组件名 （子组件）
 * 组件版本: v1
 * 创建人 rentl
 * 创建日期 2022/8/24 21:50
 * 修改日期 2022/8/24 21:50
 * 版权 pub
 */
@Service(
    function = [ApplicationLifeCycle::class],
    cache = Extend.Cache.SINGLETON,
    priority = 10
)
class LoginApplicationLifeCycle : BaseApplicationLifeCycle() {
    companion object {
        private const val TAG = "LoginApplicationLifeCyc"
    }

    override fun onCreate() {
        Logg.d(TAG, "onCreate")
    }


}