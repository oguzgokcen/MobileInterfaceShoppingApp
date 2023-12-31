package com.example.letgocloneapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.letgocloneapp.Models.ItemData
import com.example.letgocloneapp.Models.User
import com.example.letgocloneapp.requestUrl.ItemService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.*


class AddItemActivity : AppCompatActivity() {
    lateinit var addItemPrice: EditText
    lateinit var adress : EditText
    lateinit var itemCaption: EditText
    lateinit var itemDescription : EditText
    lateinit var addImage: ImageView
    lateinit var imageUri: Uri
    lateinit var selectedImageBitmap: Bitmap
    lateinit var addItemButton:Button
    lateinit var user : User
    private val BASE_URL = "http://10.0.2.2:9080/api/v1/"
    private val BASE_URL2="http://10.0.2.2:9080/api/v1/addItem"
    private var imageUrl : String = ""
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        bindViews()
        // get user from intent
        user = intent.getSerializableExtra("user")as User
        addImage.setOnClickListener {
            chooseImage()
        }
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Handle the returned Uri
                if (uri != null) {
                    imageUri = uri
                    Log.d("URI",imageUri.toString())
                    // TURN IT TO BITMAP
                    selectedImageBitmap=
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    //SET IMAGE TO IMAGEVIEW
                    addImage.setImageURI(imageUri)
                }
            }
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        addItemButton.setOnClickListener {
            uploadBitMap(BASE_URL2,selectedImageBitmap)
            addItem()
        }
        // add item to database
    }

    fun bindViews() {
        addImage = findViewById(R.id.addImage)
        addItemButton = findViewById(R.id.addItemButton)
        addItemPrice = findViewById(R.id.addItemPrice)
        adress = findViewById(R.id.Adress)
        itemCaption = findViewById(R.id.itemCaption)
        itemDescription = findViewById(R.id.itemDescription)
    }

    // Choose image from gallery
    fun chooseImage() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Handle the returned Uri
                if (uri != null) {
                    imageUri = uri
                    selectedImageBitmap=
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    val path = MediaStore.Images.Media.insertImage(
                        contentResolver,
                        selectedImageBitmap,
                        "Title",
                        null
                    )
                    addImage.setImageURI(Uri.parse(path))
                }
            }
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    // Post ItemData using Retrofit
    fun addItem(){
        val service = retrofit.create(ItemService::class.java)
        val call = service.addItem(ItemData(0,imageUrl,user.first_name.toString(),addItemPrice.text.toString().toInt(),adress.text.toString(),itemCaption.text.toString(),itemDescription.text.toString(),user.phoneNumber))
        call.enqueue(object : retrofit2.Callback<ItemData> {
            override fun onResponse(call: retrofit2.Call<ItemData>, response: retrofit2.Response<ItemData>) {
                if (response.isSuccessful) {
                    Log.d("AddItemActivity", "Item added successfully")
                } else {
                    Log.d("AddItemActivity", "Item add failed")
                }
            }

            override fun onFailure(call: retrofit2.Call<ItemData>, t: Throwable) {
                Log.d("AddItemActivity", "Item add failed")
            }
        })
    }

    // Upload image to server
    fun uploadBitMap(url:String,bitmap: Bitmap){
        // Create a file to store the bitmap
        val filename = itemCaption.text.toString() + "_"+user.user_id+".jpg"
        imageUrl = BASE_URL + filename
        val file = File(cacheDir, filename)
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()

        // Create the request body
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.name,
                RequestBody.create(MediaType.parse("image/jpeg"), file)
            )
            .build()

        // Create the request
        val request= Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        // Create an OkHttpClient instance
        val client = OkHttpClient()

        // Execute the request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("UPLOAD", "Image upload failed")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("UPLOAD", "Image successfully uploaded")
                this@AddItemActivity.runOnUiThread(Runnable {
                    kotlin.run {
                        Toast.makeText(this@AddItemActivity,"Image successfully uploaded",Toast.LENGTH_SHORT).show()
                    }
                })
                finish()
            }
        })
    }



}

