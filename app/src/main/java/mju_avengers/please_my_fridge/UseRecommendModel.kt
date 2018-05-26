package mju_avengers.please_my_fridge

import android.content.Context
import android.content.res.AssetManager

import mju_avengers.please_my_fridge.recipe_model.TensorflowRecommend



class UseRecommendModel() {
//    companion object {
//        private var instance : UseRecommendModel? = null
//        fun getInstance() : UseRecommendModel {
//            if (instance == null){
//                instance = UseRecommendModel()
//            }
//            return instance!!
//        }
//    }
//
//    fun loadModel(ctx :Context) : ArrayList<String>{
//       var mRecommeders = TensorflowRecommend.create(ctx.assets, "Keras",
//                        "opt_recipe.pb", "label.txt", "embedding_1_input", "embedding_2_input",
//                        "merge_1/ExpandDims")
//        var childIds : ArrayList<String> = ArrayList()
//        val id = 0
//        val recipeid : IntArray = IntArray(5101, {i -> i})
//        for (i in recipeid.indices) {
//            val rec = mRecommeders.recognize(id, recipeid[i])
//            childIds.add(String.format("%s: %s, %f\n", mRecommeders.name(), rec.label, rec.conf))
//        }
//        return childIds
//    }
}