package com.example.dressme

import com.example.dressme.ui.session.ItemsObjectAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import junit.framework.Assert.assertEquals
import org.junit.Test

class FirestoreUnitTest {
    private val mFirebaseRef = FirebaseFirestore.getInstance()
    private val mStorageRef = FirebaseStorage.getInstance().getReference()
    private val mItemsRef = mFirebaseRef.collection("items")
    private val mSellersRef = mFirebaseRef.collection("sellers")

    @Test
    private fun countMockUsers(adapter: ItemsObjectAdapter) {
        var itemsTask: Task<QuerySnapshot> = mItemsRef.get()
        itemsTask.addOnSuccessListener {
            assertEquals(it.getDocuments().size, 6)
        }

        var sellersTask: Task<QuerySnapshot> = mSellersRef.get()
        sellersTask.addOnSuccessListener {
            assertEquals(2, it.getDocuments())
        }
    }

//    @Test
//    private fun roundTripDatabase() {
//        val outbound = ItemModel(6, 66, null, null, null, null, null, null, null)
//        mItemsRef.add(outbound)
//
//        val inbound = FirebaseFirestore.getInstance().collection("items")
//            .whereEqualTo("sellerId", "66").get()
//        inbound.addOnSuccessListener {
//            val res = it.getDocuments().get(0)
//            assertEquals(666, (res.get("sellerId") as Int)*10 + res.get("usersId") as Int)
//        }
//    }

    @Test
    private fun sellerUnicity() {
        val task = FirebaseFirestore.getInstance().collection("sellers")
            .whereEqualTo("sellerId", "0").get()
        task.addOnSuccessListener {
            assertEquals(1, it.getDocuments().size)
        }
    }
}