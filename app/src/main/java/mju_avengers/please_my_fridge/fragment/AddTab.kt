package mju_avengers.please_my_fridge.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_add.*
import mju_avengers.please_my_fridge.FridgeActivity
import mju_avengers.please_my_fridge.R
import org.jetbrains.anko.support.v4.toast

class AddTab : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_add, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addFragment(WriteTab())

        btn_add_frigde.setOnClickListener {
            val intent = Intent(context, FridgeActivity::class.java)
            intent.putExtra("msg", "냉장고로 테스트 메시지 보내기")
            startActivity(intent)
        }
        add_write_btn.setOnClickListener {
            replaceFragment(WriteTab())
        }
        add_carmera_btn.setOnClickListener {
            toast("카메라 작동 시키기")
        }


    }

    fun addFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.add_frame, fragment)
        transaction.commit()
    }

    fun replaceFragment(fragment: Fragment){
        val transition = childFragmentManager.beginTransaction()
        transition.replace(R.id.add_frame, fragment)
        transition.commit()
    }
}