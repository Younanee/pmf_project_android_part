package mju_avengers.please_my_fridge

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

interface OnGetDataListener {
    fun onStart()
    fun onSuccess(data : DataSnapshot)
    fun onFailed(databaseError : DatabaseError)
}
