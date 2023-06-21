package com.example.letgocloneapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.letgocloneapp.Models.ItemData

class ItemDetailActivity : AppCompatActivity() {
    lateinit var item: ItemData
    lateinit var detailImage: ImageView
    lateinit var detailCaption: TextView
    lateinit var detailPrice: TextView
    lateinit var detailDescription: TextView
    lateinit var detailSellerName: TextView
    lateinit var detailSellerAdress: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        bindViews()
        intent.getSerializableExtra("item")?.let {
            item = it as ItemData
            Glide.with(this).load(item.imageUrl).into(detailImage)
            detailCaption.text = item.caption
            val price = item.price.toString() + " TL"
            val phone = Uri.parse("tel:"+item.sellerPhone)
            detailPrice.text = price
            detailDescription.text = item.description
            detailSellerName.text = item.sellerName
            detailSellerAdress.text = item.adress
            detailSellerName.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.setData(phone)
                startActivity(intent)
            }

        }

    }
    fun bindViews(){
        detailImage=findViewById(R.id.detailImage)
        detailCaption=findViewById(R.id.detailCaption)
        detailPrice=findViewById(R.id.detailPrice)
        detailDescription=findViewById(R.id.detailDescription)
        detailSellerName=findViewById(R.id.detailUser)
        detailSellerAdress=findViewById(R.id.detailAdress)
    }
}