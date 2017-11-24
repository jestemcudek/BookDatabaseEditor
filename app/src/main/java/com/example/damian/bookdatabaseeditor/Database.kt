package com.example.damian.bookdatabaseeditor


import android.widget.Toast

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList

/**
 * Created by Damian on 24.11.2017.
 */

class Database {
    private var conn: Connection? = null
    private var stmt: Statement? = null
    private var rs: ResultSet? = null

    private var choosenIsbn: String? = null
    private var choosenAuthor: String? = null

    fun getBooksByAuthor(author: String): List<Book> {
        val returnList: List<Book>
        choosenAuthor = author
        returnList = getFromSQL(2)
        return returnList
    }

    fun getBooksByISBN(ISBN: String): List<Book> {
        val returnList: List<Book>
        choosenIsbn = ISBN
        returnList = getFromSQL(3)
        return returnList
    }

    fun connect() {
        try {
            var attmept = 1
            while (attmept < 4 && conn == null) {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance()
                conn = DriverManager.getConnection("jdbc:mysql://mysql.agh.edu.pl/dcudek?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                        "dcudek", "q4bVR5YYpX3pqf4a")
                attmept++
            }
            println("Połączono z bazą danych")

        } catch (ex: SQLException) {
            // handle any errors
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getFromSQL(param: Int): List<Book> {
        val returnList = ArrayList<Book>()
        try {
            connect()
            //if(conn!=null){
            stmt = conn!!.createStatement()


            when (param) {
                1 -> rs = stmt!!.executeQuery("SELECT * FROM books")
                2 -> {
                    rs = stmt!!.executeQuery("SELECT * FROM books WHERE isbn = \'$choosenIsbn\';")
                    println(choosenIsbn)
                }
                3 -> rs = stmt!!.executeQuery("SELECT * FROM books WHERE author LIKE %$choosenAuthor%")
            }//wypisuje wszystkich
            //wypisuje po isbn
            //wypisuje po autorze


            while (rs!!.next()) {
                val isbn = rs!!.getString("ISBN")
                val author = rs!!.getString("Author")
                val name = rs!!.getString("Title")
                val year = rs!!.getString("year")
                val addition = Book(isbn, author, name, year)
                returnList.add(addition)
                //}
            }

        } catch (ex: SQLException) {
            // handle any errors

        } finally {
            // zwalniamy zasoby, które nie będą potrzebne
            if (rs != null) {
                try {
                    rs!!.close()
                } catch (sqlEx: SQLException) {
                }
                // ignore
                rs = null
            }

            if (stmt != null) {
                try {
                    stmt!!.close()
                } catch (sqlEx: SQLException) {
                }
                // ignore

                stmt = null
            }
        }
        return returnList
    }
}


