package com.example.dressme.models

class Item(val name: String            =  "",
           val desc_text: String       =  "",
           val item_image_uri: String  ?= null,
           val owner_user: OwnerUser   ?= null)