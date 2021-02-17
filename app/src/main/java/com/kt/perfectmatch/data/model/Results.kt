package com.kt.perfectmatch.data.model

import com.kt.perfectmatch.data.model.Dob
import com.kt.perfectmatch.data.model.Location
import com.kt.perfectmatch.data.model.Name
import com.kt.perfectmatch.data.model.Picture

data class Results(
    val email: String?,
    val name: Name?,
    val location: Location?,
    val picture: Picture?,
    val dob: Dob?
)
