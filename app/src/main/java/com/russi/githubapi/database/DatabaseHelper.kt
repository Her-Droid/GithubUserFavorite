package com.russi.githubapi.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USER_TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE, null, DATABASE_VERSION){

    companion object{
        const val DATABASE = "databaseGithub"
        const val DATABASE_VERSION = 1
        const val SQL_TABLE = "CREATE TABLE $USER_TABLE_NAME" +
                " (${GithubDatabase.FavoriteTable.USERNAME} TEXT PRIMARY KEY  NOT NULL," +
                " ${GithubDatabase.FavoriteTable.NAME} TEXT NOT NULL," +
                " ${GithubDatabase.FavoriteTable.AVATAR} TEXT NOT NULL," +
                " ${GithubDatabase.FavoriteTable.COMPANY} TEXT NOT NULL," +
                " ${GithubDatabase.FavoriteTable.LOCATION} TEXT NOT NULL," +
                " ${GithubDatabase.FavoriteTable.FOLLOWERS} INTEGER NOT NULL," +
                " ${GithubDatabase.FavoriteTable.FOLLOWING} INTEGER NOT NULL," +
                " ${GithubDatabase.FavoriteTable.FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(SQL_TABLE)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
        onCreate(database)
    }
}