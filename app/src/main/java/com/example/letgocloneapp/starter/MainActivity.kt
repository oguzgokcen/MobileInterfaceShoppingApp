package com.example.letgocloneapp.starter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letgocloneapp.ItemListScreen
import com.example.letgocloneapp.Models.Login
import com.example.letgocloneapp.Models.User
import com.example.letgocloneapp.R
import com.example.letgocloneapp.helpers.StringHelper
import com.example.letgocloneapp.requestUrl.LoginRegisterApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var password: EditText
    lateinit var email: EditText
    lateinit var loginButton: Button
    lateinit var sign_up: TextView
    private val BASE_URL = "http://10.0.2.2:9080/api/v1/"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        setSignUp()
        loginButton.setOnClickListener {
            authenticateUser()
        }
    }

    //Bind Views
    fun bindViews() {
        password = findViewById(R.id.password)
        email = findViewById(R.id.email)
        loginButton = findViewById(R.id.loginbutton)
        sign_up = findViewById(R.id.sign_up_main)
    }

    //Set Sign Up button
    fun setSignUp() {
        sign_up.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }
    //authentication
    fun authenticateUser(){
        // Check For Errors:
        if (!validateEmail() || !validatePassword()) {
            return
        }
        // Inisiate retrofit request
        val service = retrofit.create(LoginRegisterApiService::class.java)
        val call = service.loginUser(Login (email.text.toString(), password.text.toString()))
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user =response.body()
                    if (user != null) {
                        Toast.makeText(this@MainActivity, "Welcome ${user.first_name}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, ItemListScreen::class.java)
                        // Pass Values To Profile Activity:
                        /*intent.putExtra("first_name", user.first_name)
                        intent.putExtra("last_name", user.last_name)*/
                        intent.putExtra("user",user as User?)
                        startActivity(intent)
                        finish()
                    }
                }
                else{
                    Toast.makeText(this@MainActivity, "Yanlış şifre veya mail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }



    fun validateEmail(): Boolean {
        val mail = email.getText().toString()
        // Check If Email Is Empty:
        return if (mail.isEmpty()) {
            email.setError("Email cannot be empty!")
            false
        } else if (!StringHelper.regexEmailValidationPattern(mail)) {
            email.setError("Please enter a valid email")
            false
        } else {
            email.setError(null)
            true
        } // Check If Email Is Empty.
    }
    // End Of Validate Email Field.

    fun validatePassword():Boolean
    {
        val pass: String = password.getText().toString()

        // Check If Password and Confirm Field Is Empty:

        // Check If Password and Confirm Field Is Empty:
        return if (pass.isEmpty()) {
            password.setError("Password cannot be empty!")
            false
        } else {
            password.setError(null)
            true
        } // Check Password and Confirm Field Is Empty.


    }
}