package mju_avengers.please_my_fridge

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UseFirebaseDatabase() {
    companion object {
        private var instance: UseFirebaseDatabase? = null
        fun getInstence(): UseFirebaseDatabase {
            if (instance == null) {
                instance = UseFirebaseDatabase()
            }
            return instance!!
        }
    }

    fun readFBData(childID: String, listener: OnGetDataListener) {
        listener.onStart()
        val mFBDatabase = FirebaseDatabase.getInstance().reference
        mFBDatabase.child(childID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(errorData: DatabaseError?) {
                listener.onFailed(errorData!!)
            }

            override fun onDataChange(data: DataSnapshot?) {
                listener.onSuccess(data!!)
            }
        })
    }
}
