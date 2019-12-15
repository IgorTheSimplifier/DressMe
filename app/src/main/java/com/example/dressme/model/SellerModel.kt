package com.example.dressme.model

data class SellerModel(
    val sellerId:           Long        = 2L,
    val userAccount:        UserModel?  = null,

    val name:               String?     = null,
    val profileImageUri:    String?     = null,
    val location:           String?     = null
)