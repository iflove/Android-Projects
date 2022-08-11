package com.androidz.base_modules.lib_room_db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * 作用描述: 基础的实体, 目前用于自增覆写参照下面demo
 * 组件描述: Room 三个主要组件：数据实体，用于表示应用的数据库中的表。
 * 创建人 rentl
 * 创建日期 2022/5/11
 * 修改日期 2022/5/11
 * 版权 mashang
 */
abstract class BaseEntity {
    abstract val uid: Long
}

/**
 * 演示实体
 */
@Entity
@Parcelize
data class DemoEntity(
    @PrimaryKey(autoGenerate = true)
    override val uid: Long = 0, //自增
) : Parcelable, BaseEntity() {
    //自定义属性
}