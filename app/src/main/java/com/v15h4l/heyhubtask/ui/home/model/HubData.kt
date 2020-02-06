package com.v15h4l.heyhubtask.ui.home.model

import Hub
import com.google.gson.annotations.SerializedName

data class HubData(
    @SerializedName("results") val hub : Hub,
    @SerializedName("success") val success : String,
    @SerializedName("message") val message : String
)