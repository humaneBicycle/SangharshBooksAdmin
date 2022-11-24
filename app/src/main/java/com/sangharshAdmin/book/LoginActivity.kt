package com.sangharshAdmin.book

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {
    var emailET: EditText? = null
    var passwordET:EditText? = null
    var next: TextView? = null
    var mAuth: FirebaseAuth? = null
    var emailAlreadyExist = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailET = findViewById(R.id.email_login_activity)
        passwordET = findViewById<EditText>(R.id.password_login)
        next = findViewById(R.id.img_email_login_next)
        mAuth = FirebaseAuth.getInstance()
        next!!.setOnClickListener(View.OnClickListener {
            val email = emailET!!.getText().toString()
            val password: String = passwordET!!.getText().toString()
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@LoginActivity,
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(
                                this@LoginActivity,
                                "Sign in successfull",
                                Toast.LENGTH_SHORT
                            ).show()

                            loginHandler(email, true)
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("sba: ", "login error: "+task.exception )
                            //updateUI(null);
                        }
                    })
        })
    }

    private fun loginHandler(email: String, isLoginSuccessful: Boolean) {
        if (isLoginSuccessful) {
            doesUserWithThisMailExist(email)
            if (emailAlreadyExist) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    ).putExtra("email", email)
                )
            }
        }
    }

    private fun doesUserWithThisMailExist(mail: String): Boolean {
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", mail).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                emailAlreadyExist = if (queryDocumentSnapshots.isEmpty) {
                    false
                    //new PreferenceGetter(LoginActivity.this).putBoolean(PreferenceGetter.IS_REGISTERED,false);
                    //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                } else {
                    true
                    //new PreferenceGetter(LoginActivity.this).putBoolean(PreferenceGetter.IS_REGISTERED,true);
                    //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@LoginActivity,
                    "Task Failed. Please Try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        return emailAlreadyExist
    }
}