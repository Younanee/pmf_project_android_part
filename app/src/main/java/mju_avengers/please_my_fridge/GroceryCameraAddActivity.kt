package mju_avengers.please_my_fridge

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.VisionRequest
import com.google.api.services.vision.v1.VisionRequestInitializer
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_grocerty_camera_add.*
import mju_avengers.please_my_fridge.adapter.AddGroceryRecyclerAdapter
import mju_avengers.please_my_fridge.data.GroceryData
import mju_avengers.please_my_fridge.db.DataOpenHelper
import mju_avengers.please_my_fridge.vision_api.PermissionUtils
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.model.*
import mju_avengers.please_my_fridge.dictionary.GroceryDataProcess
import mju_avengers.please_my_fridge.vision_api.PackageManagerUtils
import java.lang.ref.WeakReference

class GroceryCameraAddActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        var idx : Int = add_grocery_item_camera_rv.getChildAdapterPosition(v)
    }

    private val CLOUD_VISION_API_KEY = "12345"
    val FILE_NAME = "temp.jpg"
    private val ANDROID_CERT_HEADER = "X-Android-Cert"
    private val ANDROID_PACKAGE_HEADER = "X-Android-Package"
    private val MAX_LABEL_RESULTS = 10
    private val MAX_DIMENSION = 1200

    private val GALLERY_PERMISSIONS_REQUEST = 0
    private val GALLERY_IMAGE_REQUEST = 1
    val CAMERA_PERMISSIONS_REQUEST = 2
    val CAMERA_IMAGE_REQUEST = 3


    lateinit var groceryDatas : ArrayList<GroceryData>
    lateinit var addGroceryDataAdapter : AddGroceryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocerty_camera_add)
        setGroceryDataAdapter()
        add_grocery_camera_add_btn.setOnClickListener {
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
                            addGroceryDataAdapter.addGrocery(GroceryData(-1, selectedRadioButton.text.toString(), groceryName.text.toString()))

                            //이거추가하면?! 지워도되긴함.
                            addGroceryDataAdapter.notifyDataSetChanged()
                        }
                    }.show()

        }
        add_grocery_camera_finish_btn.setOnClickListener {
            saveGroceryDatas()
            finish()
        }
        //카메라 달기 시작
        grocery_camera_test_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this@GroceryCameraAddActivity)
            builder
                    .setMessage("방식 선택")
                    .setPositiveButton("갤러리에서 선택", { dialog, which -> startGalleryChooser() })
                    .setNegativeButton("사진 찍기", { dialog, which -> startCamera() })
            builder.create().show()
        }
    }
    private fun saveGroceryDatas(){
        DataOpenHelper.getInstance(applicationContext).insertGroceryDatas(addGroceryDataAdapter.getCurrentGroceryDatas())
        //성공!!!
        //Log.e("식료품 데이터들 뭐가 들었나?", addGroceryDataAdapter.getGrocetyDatas().toString())
    }
    private fun setGroceryDataAdapter(){
        groceryDatas = ArrayList()
        addGroceryDataAdapter = AddGroceryRecyclerAdapter(this, groceryDatas)
        addGroceryDataAdapter.setOnItemClickListener(this)

        add_grocery_item_camera_rv.layoutManager = LinearLayoutManager(applicationContext)
        add_grocery_item_camera_rv.adapter = addGroceryDataAdapter

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
                        saveGroceryDatas()
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
    //비전 api 부분 구현 시작
    private fun startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST)
        }
    }

    private fun startCamera() {
        if (PermissionUtils.requestPermission(
                        this,
                        CAMERA_PERMISSIONS_REQUEST,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoUri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", getCameraFile())
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST)
        }
    }

    private fun getCameraFile(): File {
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(dir, FILE_NAME)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.data)
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val photoUri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", getCameraFile())
            uploadImage(photoUri)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSIONS_REQUEST -> if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                startCamera()
            }
            GALLERY_PERMISSIONS_REQUEST -> if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                startGalleryChooser()
            }
        }
    }

    fun uploadImage(uri: Uri?) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                val bitmap = scaleBitmapDown(
                        MediaStore.Images.Media.getBitmap(contentResolver, uri),
                        MAX_DIMENSION)

                callCloudVision(bitmap)
                mMainImage.setImageBitmap(bitmap)

            } catch (e: IOException) {
                Log.d("1", "Image picking failed because " + e.message)
                //Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show()
            }

        } else {
            Log.d("1", "Image picker gave us a null image.")
            //Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show()
        }
    }

    @Throws(IOException::class)
    private fun prepareAnnotationRequest(bitmap: Bitmap): Vision.Images.Annotate {
        val httpTransport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()

        val requestInitializer = object : VisionRequestInitializer(CLOUD_VISION_API_KEY) {
            /**
             * We override this so we can inject important identifying fields into the HTTP
             * headers. This enables use of a restricted cloud platform API key.
             */
            @Throws(IOException::class)
            override fun initializeVisionRequest(visionRequest: VisionRequest<*>?) {
                super.initializeVisionRequest(visionRequest)

                val packageName = packageName
                visionRequest!!.requestHeaders.set(ANDROID_PACKAGE_HEADER, packageName)

                val sig = PackageManagerUtils.getSignature(packageManager, packageName)

                visionRequest.requestHeaders.set(ANDROID_CERT_HEADER, sig)
            }
        }

        val builder = Vision.Builder(httpTransport, jsonFactory, null)
        builder.setVisionRequestInitializer(requestInitializer)

        val vision = builder.build()

        val batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
        batchAnnotateImagesRequest.requests = object : java.util.ArrayList<AnnotateImageRequest>() {
            init {
                val annotateImageRequest = AnnotateImageRequest()

                // Add the image
                val base64EncodedImage = Image()
                // Convert the bitmap to a JPEG
                // Just in case it's a format that Android understands but Cloud Vision
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()

                // Base64 encode the JPEG
                base64EncodedImage.encodeContent(imageBytes)
                annotateImageRequest.image = base64EncodedImage

                // add the features we want
                annotateImageRequest.features = object : java.util.ArrayList<Feature>() {
                    init {
                        val textDetection = Feature()
                        textDetection.type = "TEXT_DETECTION"
                        textDetection.maxResults = 10
                        add(textDetection)
                    }
                }

                // Add the list of one thing to the request
                add(annotateImageRequest)
            }
        }

        val annotateRequest = vision.images().annotate(batchAnnotateImagesRequest)
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.disableGZipContent = true
        Log.d("1", "created Cloud Vision request object, sending request")

        return annotateRequest
    }

    private class LableDetectionTask internal constructor(activity: GroceryCameraAddActivity, private val mRequest: Vision.Images.Annotate) : AsyncTask<Any, Void, String>() {
        private val mActivityWeakReference: WeakReference<GroceryCameraAddActivity> = WeakReference(activity)

        override fun doInBackground(vararg params: Any): String {
            try {
                Log.d("1", "created Cloud Vision request object, sending request")
                val response = mRequest.execute()
                //
                var message = "I found these things:\n\n"

                val labels = response.responses[0].textAnnotations
                if (labels != null) {

                    message += labels[0].description
                } else {
                    message += "nothing"
                }
                //
                return message

            } catch (e: GoogleJsonResponseException) {
                Log.d("2", "failed to make API request because " + e.content)
            } catch (e: IOException) {
                Log.d("3", "failed to make API request because of other IOException " + e.message)
            }

            return "Cloud Vision API request failed. Check logs for details."
        }

        override fun onPostExecute(result: String) {

            val activity = mActivityWeakReference.get()
            val imageDetail = activity!!.findViewById(R.id.mImageDetails) as TextView
            //우웃풋!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            val temp :ArrayList<String> = ArrayList(GroceryDataProcess.getInstence().returnData(result))
            imageDetail.text = temp.toString()
        }
    }

    private fun callCloudVision(bitmap: Bitmap) {
        // Switch text to loading
        mImageDetails.text = "로딩중"

        // Do the real work in an async task, because we need to use the network anyway
        try {
            val labelDetectionTask = LableDetectionTask(this, prepareAnnotationRequest(bitmap))
            labelDetectionTask.execute()
        } catch (e: IOException) {
            Log.d("1", "failed to make API request because of other IOException " + e.message)
        }

    }

    private fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {

        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }

    fun convertResponseToString(response: BatchAnnotateImagesResponse): String {
        var message = "I found these things:\n\n"

        val labels = response.responses[0].textAnnotations
        if (labels != null) {

            message += labels[0].description
        } else {
            message += "nothing"
        }
        return message
    }

}
