package com.sangharshAdmin.book

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.sangharshAdmin.book.R.*
import com.squareup.picasso.Picasso

class TestActivity : AppCompatActivity() {
    lateinit var chooseTestBannerBtn:Button
    lateinit var testBannerIV:ImageView
    var  testBannerImg : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_test)
        val createTestBtn = findViewById<Button>(id.createTestBtn)
        val testTitle = findViewById<EditText>(id.testTitle)
        val testDescription = findViewById<EditText>(id.testDescription)
        val timeAllowed = findViewById<EditText>(id.timeAllowed)

        chooseTestBannerBtn = findViewById(R.id.chooseTestBannerBtn)
        chooseTestBannerBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                0
            )
        })

        createTestBtn.setOnClickListener(View.OnClickListener {
            if(!testTitle.text.isEmpty() && !testDescription.text.isEmpty()) {
                Log.i("TestActivity", "Test not empty")
                val intent = Intent(this@TestActivity, AddQuestionActivity::class.java)
                val title = testTitle.text.toString()
                val testDescription = testDescription.text.toString()
                val timeAllowed = timeAllowed.text.toString()
                intent.putExtra("title",title)
                intent.putExtra("description",testDescription)
                intent.putExtra("timer",timeAllowed)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Please fill the above fields",Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== RESULT_OK && data!=null){
            testBannerImg = data.data!!
            testBannerIV = findViewById(R.id.testBannerIV)
            Picasso.get()
                .load(data.data)
                .into(testBannerIV)


        }
    }


}