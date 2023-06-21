package com.example.letgocloneapp.starter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

class SignUp : AppCompatActivity() {
    lateinit var password: EditText
    lateinit var email: EditText
    lateinit var name:EditText
    lateinit var surname:EditText
    lateinit var confirm:EditText
    lateinit var phoneNumber :EditText
    lateinit var signupButton:Button
    lateinit var backButton:ImageButton
    private val BASE_URL = "http://10.0.2.2:9080/api/v1/"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .baseUrl(BASE_URL)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        bindViews()
        signupButton.setOnClickListener {
            signUp()
        }
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    //Bind Views
    fun bindViews(){
        password = findViewById(R.id.password)
        email = findViewById(R.id.email)
        name = findViewById(R.id.name)
        surname = findViewById(R.id.surname)
        confirm = findViewById(R.id.confirm)
        signupButton = findViewById(R.id.sign_up)
        backButton = findViewById(R.id.backButton)
        phoneNumber = findViewById(R.id.email2)
    }

    //Sign Up
    fun signUp(){
        //validate texts
        if (!validateFirstName() or !validateLastName() or !validateEmail() or !validatePasswordAndConfirm() ) {
            return
        }

        val service = retrofit.create(LoginRegisterApiService::class.java)
        val call = service.registerUser(User(-1,name.text.toString(),surname.text.toString(),email.text.toString(), password.text.toString(),phoneNumber.text.toString()))
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    Log.d("Response IS SUCCESS","+")
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("Response IS FAILURE", t.message.toString())
                Toast.makeText(this@SignUp, "Register Successfully complete",Toast.LENGTH_LONG).show()
            }
        })
    }

    // End Of Process Form Fields Method.
    fun validateFirstName(): Boolean {
        val firstName: String = name.getText().toString()
        // Check If First Name Is Empty:
        return if (firstName.isEmpty()) {
            name.setError("First name cannot be empty!")
            false
        } else {
            name.setError(null)
            true
        } // Check If First Name Is Empty.
    }
    // End Of Validate First Name Field.


    // End Of Validate First Name Field.
    fun validateLastName(): Boolean {
        val lastName: String = surname.getText().toString()
        // Check If Last Name Is Empty:
        return if (lastName.isEmpty()) {
            surname.setError("Last name cannot be empty!")
            false
        } else {
            surname.setError(null)
            true
        } // Check If Last Name Is Empty.
    }
    // End Of Validate Last Name Field.

    // End Of Validate Last Name Field.
    fun validateEmail(): Boolean {
        val email_e = email.text.toString()
        // Check If Email Is Empty:
        return if (email_e.isEmpty()) {
            email.error = "Email cannot be empty!"
            false
        } else if (!StringHelper.regexEmailValidationPattern(email_e)) {
            email.error = "Please enter a valid email"
            false
        } else {
            email.error = null
            true
        } // Check If Email Is Empty.
    }
    // End Of Validate Email Field.

    // End Of Validate Email Field.
    fun validatePasswordAndConfirm(): Boolean {
        val password_p = password.text.toString()
        val confirm_p = confirm.text.toString()

        // Check If Password and Confirm Field Is Empty:
        return if (password_p.isEmpty()) {
            password.error = "Password cannot be empty!"
            false
        } else if (password_p != confirm_p) {
            password.error = "Passwords do not match!"
            false
        } else if (confirm_p.isEmpty()) {
            confirm.error = "Confirm field cannot be empty!"
            false
        } else {
            password.error = null
            confirm.error = null
            true
        } // Check Password and Confirm Field Is Empty.
    }
    //set sign up button






}