package com.thk.contactsapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

fun getDataSet(context: Context): List<Contact> {

    try {
        val jsonString = context.assets
            .open("jsons/contacts.json")
            .bufferedReader()
            .use { it.readText() }

        val contactsJsonArray = JSONObject(jsonString).getJSONArray("contacts")
        val contactsJsonObjectList = List(contactsJsonArray.length()) { contactsJsonArray.getJSONObject(it) }

        return contactsJsonObjectList.map { jsonObject ->
            val profilePic = jsonObject.getString("profilePic")
            val name = jsonObject.getString("name")
            val phoneNum = jsonObject.getString("phoneNum")

            Contact(profilePic, name, phoneNum)
        }

    } catch (e: Exception) {
        Log.d("testlog", "error: ${e.message}")
        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
    }

    return emptyList()
}