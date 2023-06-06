package com.example.letgocloneapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.letgocloneapp.starter.MainActivity

class Menu : AppCompatActivity() {
    lateinit var tv_first_name: TextView
    lateinit var tv_last_name: TextView
    lateinit var tv_email: TextView
    lateinit var sign_out_button:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Hook Text View Objects:
        tv_first_name = findViewById(R.id.menu_first_name)
        tv_last_name = findViewById(R.id.menu_last_name)
        tv_email = findViewById(R.id.menu_email)

        // Get Intent Extra Values:

        // Get Intent Extra Values:
        val first_name = intent.getStringExtra("first_name")
        val last_name = intent.getStringExtra("last_name")
        val email = intent.getStringExtra("email")

        // Set Text View Profile Values:

        // Set Text View Profile Values:
        tv_first_name.text = first_name
        tv_last_name.text = last_name
        tv_email.text = email

        // Hook Sign Out Button:

        // Hook Sign Out Button:
        sign_out_button = findViewById(R.id.sign_out_btn)

        // Set On Click Listener:

        // Set On Click Listener:
        sign_out_button.setOnClickListener(View.OnClickListener { signUserOut() })
    }

    fun signUserOut() {

        // Set Text View Profile Values:
        tv_first_name.text = null
        tv_last_name.text = null
        tv_email.text = null

        // Return User Back To Home:
        val goToHome = Intent(this, MainActivity::class.java)
        startActivity(goToHome)
        finish()
    }
}