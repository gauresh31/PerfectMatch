package com.kt.perfectmatch.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.kt.perfectmatch.data.model.Results

@Keep
class MatchesNestedModel {

    @SerializedName("results")
    var data: List<Results>? = null
}