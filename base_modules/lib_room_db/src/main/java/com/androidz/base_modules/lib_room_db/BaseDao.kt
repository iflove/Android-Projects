package com.androidz.base_modules.lib_room_db

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.androidz.base_modules.lib_logger.Logg.Companion.log
import com.androidz.base_modules.lib_room_db.RoomsFactory.debug
import java.lang.reflect.ParameterizedType

/**
 * 作用描述: 基础DAO 的抽象实例  Dao组件默认策略是ABORT：XXX OR ABORT, BaseDao组件默认策略是REPLACE: XXX OR REPLACE INTO，可以覆写方法改变策略
 * 组件描述: Room 三个主要组件：数据访问对象 (DAO)，提供您的应用可用于查询、更新、插入和删除数据库中的数据的方法。
 * 创建人 rentl
 * 创建日期 2022/5/11
 * 修改日期 2022/5/11
 * 版权 mashang
 */
abstract class BaseDao<T : BaseEntity>(val onConflict: Int = OnConflictStrategy.REPLACE) {

    //默认获取泛型T类名作为表名
    val tableName: String
        get() = defTableName()

    /**
     * 获取泛型T类名作为表名, 可覆写
     */
    protected open fun defTableName(): String {
        val clazz =
            (javaClass.superclass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        return clazz.simpleName
    }

    /**
     * 插入数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: T): Long

    /**
     * 插入数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg entities: T): LongArray

    /**
     * 插入数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entities: List<T>): List<Long>

    /**
     * 删除数据
     * 根据对象中的主键删除（主键是自动增长的，无需手动赋值）
     */
    @Delete
    abstract fun delete(entity: T)

    /**
     * 删除数据
     * 根据对象中的主键删除（主键是自动增长的，无需手动赋值）
     */
    @Delete
    abstract fun delete(vararg entities: T)

    /**
     * 删除数据
     * 根据对象中的主键删除（主键是自动增长的，无需手动赋值）
     */
    @Delete
    abstract fun delete(entities: List<T>)

    /**
     * 更新数据
     * 根据对象中的主键更新（主键是自动增长的，无需手动赋值）
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(entity: T): Int

    /**
     * 更新数据
     * 根据对象中的主键更新（主键是自动增长的，无需手动赋值）
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(vararg entities: T): Int

    /**
     * 更新数据
     * 根据对象中的主键更新（主键是自动增长的，无需手动赋值）
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(entities: List<T>): Int

    //Usage: SQLite ,
    /**
     * 删除所有
     */
    fun deleteAll(): Int {
        val query = SimpleSQLiteQuery("DELETE FROM $tableName").debug()
        return getQueryResult(query)
    }

    /**
     * [name] 列名
     * [value] 列的值
     */
    fun delete(name: String, value: String): Int {
        val query = SimpleSQLiteQuery("DELETE FROM $tableName WHERE $name='${value}'").debug()
        return getQueryResult(query)
    }

    fun getCountInt(): Int {
        val query = SimpleSQLiteQuery("SELECT count(*) FROM $tableName").debug()
        return getQueryResult(query)
    }

    fun getCountLong(): Long {
        val query = SimpleSQLiteQuery("SELECT count(*) FROM $tableName").debug()
        return getQueryResultLong(query)
    }

    fun getSumInt(name: String): Int {
        return getSumInt(name = name, whereSQL = "")
    }

    fun getSumInt(name: String, whereSQL: String = ""): Int {
        var sql = "SELECT SUM($name) FROM $tableName"
        if (whereSQL.isNotBlank()) {
            sql += " $whereSQL"
        }
        val query = SimpleSQLiteQuery(sql).debug()
        return getQueryResult(query)
    }

    fun getSumLong(name: String): Long {
        return getSumLong(name = name, whereSQL = "")
    }

    fun getSumLong(name: String, whereSQL: String = ""): Long {
        var sql = "SELECT SUM($name) FROM $tableName"
        if (whereSQL.isNotBlank()) {
            sql += " $whereSQL"
        }
        val query = SimpleSQLiteQuery(sql).debug()
        return getQueryResultLong(query)
    }

    fun getEntities(): List<T> {
        val query = SimpleSQLiteQuery("SELECT * FROM $tableName").debug()
        return getEntities(query)
    }

    /**
     * id：uid
     */
    fun getEntity(id: Long): T? {
        val query =
            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE UID = ?", arrayOf<Any>(id)).debug()
        return getEntity(query)
    }

    /** (未完善)
     * 分页查询，支持传入多个字段，但必须要按照顺序传入
     * key = value，key = value 的形式，一一对应（可以使用 stringbuilder 去构造一下，这里就不演示了）
     */
    fun getEntities(vararg string: String, limit: Int = 20, offset: Int = 0): List<T> {
        val query =
            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE ${string[0]} = '${string[1]}' LIMIT $limit OFFSET $offset")
        query.debug()
        return getEntities(query)
    }

    /**
     * 分页查询(默认升序)
     */
    fun getEntitiesByOrder(
        vararg columnNamesArg: String,
        limit: Int = 20,
        offset: Int = 0,
        sort: String = "ASC"
    ): List<T> {
        val columnNames = columnNamesArg.joinToString()
        log.d(tableName, "column_names:$columnNames")
        val query =
            SimpleSQLiteQuery("SELECT * FROM $tableName ORDER BY $columnNames $sort LIMIT '${limit}' OFFSET '${offset}'")
        query.debug()
        return getEntities(query)
    }

    /**
     * 分页查询(默认升序)
     */
    fun getEntitiesByOrderDesc(
        vararg columnNamesArg: String,
        limit: Int = 20,
        offset: Int = 0
    ): List<T> {
        return getEntitiesByOrder(
            columnNamesArg = columnNamesArg,
            limit = limit,
            offset = offset,
            sort = "DESC"
        )
    }

    // Usage of RawDao
    @RawQuery
    protected abstract fun getEntity(query: SupportSQLiteQuery): T?

    @RawQuery
    protected abstract fun getEntities(query: SupportSQLiteQuery): List<T>

    /**
     * RawQuery 都是执行 无论删除还是插入、查询 androidx.room.util.DBUtil.query
     */
    @RawQuery
    protected abstract fun getQueryResult(query: SupportSQLiteQuery): Int

    /**
     * RawQuery 都是执行 无论删除还是插入、查询 androidx.room.util.DBUtil.query
     */
    @RawQuery
    protected abstract fun getQueryResultLong(query: SupportSQLiteQuery): Long
}