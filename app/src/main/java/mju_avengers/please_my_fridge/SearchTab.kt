package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SearchTab : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addFragment(MainSearchTab())


    }

    fun addFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.search_frame, fragment)
        transaction.commit()
    }
}