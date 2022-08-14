package com.quangpv.punk.model.dto

import com.google.gson.annotations.SerializedName

class BeerDTO(
    val id: Long? = null,
    val name: String? = null,

    @SerializedName("tagline")
    val tagLine: String? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null,
    val description: String? = null,
)