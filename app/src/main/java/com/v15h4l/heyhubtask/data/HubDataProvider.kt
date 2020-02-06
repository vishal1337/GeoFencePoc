package com.v15h4l.heyhubtask.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.v15h4l.heyhubtask.ui.home.model.HubData

object HubDataProvider {

    fun getHubData(context: Context, fileName: String): HubData {
        context.assets.open(fileName).use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val hubDataType = object : TypeToken<HubData>() {}.type
                return Gson().fromJson(jsonReader, hubDataType)
            }
        }
    }

}