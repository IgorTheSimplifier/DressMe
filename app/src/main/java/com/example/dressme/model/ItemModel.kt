package com.example.dressme.model

data class ItemModel(
    val itemId:         Long    = 0L,
    val sellerId:       Long    = 2L,

    val name:           String? = null,
    val brand:          String? = null,
    val description:    String? = null,
    val info:           String? = null,
    val price:          String? = null,
    val rating:         String? = null,

    val imageUri:       String? = null
)