package com.example.dressme.models

import java.io.Serializable

class OwnerUser(val user_id: String    = "",
                val name: String       = "",
                val profile_image_uri: String? = null) : Serializable