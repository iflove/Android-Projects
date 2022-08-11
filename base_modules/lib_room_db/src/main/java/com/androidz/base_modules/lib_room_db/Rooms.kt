package com.androidz.base_modules.lib_room_db

import android.content.Context
import androidx.multidex.BuildConfig
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import com.androidz.base_modules.lib_logger.Logg

/**
 * 作用描述: 创建一个数据库。为一个持久数据库建设者。一旦建立一个数据库,你应该保持一个参考和重用它。
 * 作用描述: Creates a RoomDatabase.Builder for a persistent database. Once a database is built, you should keep a reference to it and re-use it.
 * 组件描述: Room 三个主要组件：数据库类，用于保存数据库并作为应用持久性数据底层连接的主要访问点。
 * 组件描述:
 * 创建人 rentl
 * 创建日期  2020/7/2
 * 修改日期 2022/5/5
 * 版权 mashang
 */

object RoomsFactory { //Factory
    /**
     * RoomDatabase
     */
    inline fun <reified T : RoomDatabase> build(
        context: Context,
        name: String, //数据名称或者 文件path
        journalMode: RoomDatabase.JournalMode = RoomDatabase.JournalMode.AUTOMATIC,
        callback: RoomDatabase.Callback? = null,
        body: RoomDatabase.Builder<T>.() -> Unit
    ): T {
        val build = Room.databaseBuilder(context.applicationContext, T::class.java, name)
        return build.run {
            setJournalMode(journalMode)
            callback?.let { addCallback(callback) }
            body()
            this
        }.build()
    }

    fun SimpleSQLiteQuery.debug(): SimpleSQLiteQuery {
        if (!BuildConfig.DEBUG) {
            return this
        }

        Logg.d(
            "SimpleSQLiteQueryDebug",
            "sql: ${this.sql} bindArgs: ${this.argCount}"
        )  //有需要反射获取绑定参数
        return this
    }
}