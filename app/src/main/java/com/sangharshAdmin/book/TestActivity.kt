package com.sangharshAdmin.book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.sangharshAdmin.book.R.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_test)
        val createTestBtn = findViewById<Button>(id.createTestBtn)
        val testTitle = findViewById<EditText>(id.testTitle)
        val testDescription = findViewById<EditText>(id.testDescription)
        createTestBtn.setOnClickListener(View.OnClickListener {
            if(!testTitle.text.isEmpty() && !testDescription.text.isEmpty()) {
                Log.i("TestActivity", "Test not empty")
                val intent = Intent(this@TestActivity, AddQuestionActivity::class.java)
                val title = testTitle.text.toString()
                val testDescription = testDescription.text.toString()
                intent.putExtra("title",title)
                intent.putExtra("description",testDescription)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Please fill the above fields",Toast.LENGTH_SHORT).show()
            }
        })
    }


}