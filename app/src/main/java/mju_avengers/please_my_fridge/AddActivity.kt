package mju_avengers.please_my_fridge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_add.*
import mju_avengers.please_my_fridge.adapter.AddGroceryRecyclerAdapter
import mju_avengers.please_my_fridge.data.GroceryCategory
import mju_avengers.please_my_fridge.data.GroceryData
import org.jetbrains.anko.*

class AddActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        var idx : Int = add_grocery_item_rv.getChildAdapterPosition(v)
    }

    lateinit var groceryDatas : ArrayList<GroceryData>
    lateinit var addGroceryDataAdapter : AddGroceryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setGroceryDataAdapter()
        add_grocery_add_btn.setOnClickListener {
            MaterialDialog.Builder(this)
                    .title("식료품 입력하기")
                    .positiveText("추가")
                    .negativeText("취소")
                    .customView(R.layout.dialog_add_grocery, true)
                    .onPositive { dialog, which ->
                        var groceryName : EditText = dialog.findViewById(R.id.dialog_add_grocery_name_et) as EditText
                        var groceryCatagory : RadioGroup = dialog.findViewById(R.id.dialog_add_grocery_catagory_rg) as RadioGroup

                        if(  groceryName.text.isNotEmpty()){
                            var selectedRadioButton : RadioButton = groceryCatagory.findViewById(groceryCatagory.checkedRadioButtonId) as RadioButton
                            addGroceryDataAdapter.addGrocery(GroceryData(groceryName.text.toString(), selectedRadioButton.text.toString()))
                        }
                        if (groceryDatas.size > 0) {
                            add_notice_tv.visibility = View.INVISIBLE
                        }
                    }.show()

        }
    }

    fun setGroceryDataAdapter(){
        groceryDatas = ArrayList()
        addGroceryDataAdapter = AddGroceryRecyclerAdapter(this, groceryDatas)
        addGroceryDataAdapter.setOnItemClickListener(this)

        add_grocery_item_rv.layoutManager = LinearLayoutManager(applicationContext)
        add_grocery_item_rv.adapter = addGroceryDataAdapter

    }

    override fun onBackPressed() {
        if (groceryDatas.size != 0) {
            MaterialDialog.Builder(this)
                    .content("식료품 추가 창을 닫겠습니까?")
                    .positiveText("저장 후 닫기")
                    .positiveColor(resources.getColor(R.color.colorAccent))
                    .neutralText("계속 입력")
                    .negativeText("바로 닫기")
                    .negativeColor(resources.getColor(R.color.colorPrimaryDark))
                    .onPositive { dialog, which ->
                        toast("저장 완료")
                        super.onBackPressed()
                    }
                    .onNegative { dialog, which ->
                        super.onBackPressed()
                    }
                    .onNeutral { dialog, which ->
                        toast("계속")
                    }
                    .show()
        } else {
            super.onBackPressed()
        }

    }
}
