package com.kt.perfectmatch.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
public class Matches : Serializable {
    @PrimaryKey(autoGenerate = true)
    private var id = 0

    @ColumnInfo(name = "full_name")
    private var fullName: String? = null

    @ColumnInfo(name = "age")
    private var age: String? = null

    @ColumnInfo(name = "location")
    private var location: String? = null

    @ColumnInfo(name = "picture")
    private var picture : String? = null

    @ColumnInfo(name = "email")
    private var email : String? = null

    @ColumnInfo(name = "nat")
    private var nat : String? = null

    @ColumnInfo(name = "interested")
    private var interested : String? = null

    /*
    * Getters and Setters
    * */
    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getFullName(): String? {
        return fullName
    }

    fun setFullName(fullName: String?) {
        this.fullName = fullName
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getPicture(): String? {
        return picture
    }

    fun setPicture(picture: String?) {
        this.picture = picture
    }

    fun getAge(): String? {
        return age
    }

    fun setAge(age: String?) {
        this.age = age
    }

    fun getLocation(): String? {
        return location
    }

    fun setLocation(location: String?) {
        this.location = location
    }

    fun getNat(): String? {
        return nat
    }

    fun setNat(nat: String?) {
        this.nat = nat
    }

    fun getInterested(): String? {
        return interested
    }

    fun setInterested(interested: String?) {
        this.interested = interested
    }
}