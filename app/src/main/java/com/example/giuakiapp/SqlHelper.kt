package com.example.giuakiapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.security.MessageDigest
import java.util.*

class SqlHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Tạo bảng User với các trường id, name, email, password
        db?.execSQL("CREATE TABLE User (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Xóa bảng User nếu đã tồn tại và tạo lại
        db?.execSQL("DROP TABLE IF EXISTS User")
        onCreate(db)
    }

    fun registerUser(name: String, email: String, password: String, confirmPassword: String): Boolean {
        // Kiểm tra email đã tồn tại chưa
        val cursor = readableDatabase.rawQuery("SELECT * FROM User WHERE email = ?", arrayOf(email))
        if (cursor.count > 0) {
            cursor.close()
            return false
        }
        cursor.close()

        // Kiểm tra mật khẩu nhập lại
        if (password != confirmPassword) {
            return false
        }

        // Hash mật khẩu
        val passwordHash = hashPassword(password)

        // Thêm user vào database
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", passwordHash)
        }
        writableDatabase.insert("User", null, values)

        return true
    }
    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(password.toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}