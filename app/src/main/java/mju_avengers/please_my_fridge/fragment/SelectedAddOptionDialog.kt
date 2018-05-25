package mju_avengers.please_my_fridge.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import mju_avengers.please_my_fridge.GroceryCameraAddActivity
import mju_avengers.please_my_fridge.GroceryTextAddActivity
import org.jetbrains.anko.support.v4.startActivity

class SelectedAddOptionDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("식재료 추가 방식 선택")
                .setItems(arrayOf("텍스트 입력", "영수증 인식"), DialogInterface.OnClickListener {dialog, which ->
                    when (which) {
                        0 -> startActivity<GroceryTextAddActivity>("option" to "write")
                        1 -> startActivity<GroceryCameraAddActivity>("option" to "camera")
                    }
                })
                .setOnCancelListener {
                    dismiss()
                }
//        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
//        val inflater : LayoutInflater = activity!!.layoutInflater
//        builder.setView(inflater.inflate(R.layout.dialog_selected_add_option, null))
//                .setOnCancelListener {
//                    dismiss()
//                }

        return builder.create()
    }

}