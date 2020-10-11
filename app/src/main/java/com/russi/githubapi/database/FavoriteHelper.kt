package com.russi.githubapi.database
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USERNAME
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USER_TABLE_NAME
import java.sql.SQLException

class FavoriteHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private var database: SQLiteDatabase = databaseHelper.writableDatabase

    companion object {
        private const val DATABASE_TABLE = USER_TABLE_NAME
        var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAllUserFavorite(): Cursor {
        return database.query(DATABASE_TABLE, null, null, null, null, null, "$USERNAME DESC")
    }

    fun queryByIdUserFavorite(id: String): Cursor {
        return database.query(
            DATABASE_TABLE, null, "$USERNAME = ?", arrayOf(id), null, null, null, null)
    }

    fun insertUserFavorite(values: ContentValues) : Long{
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByIdUserFavorite(id: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
    }

    fun updateUseFavorite(id: String, values: ContentValues?) : Int{
        return database.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(id))
    }

}