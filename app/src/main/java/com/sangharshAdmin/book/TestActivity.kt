package com.sangharshAdmin.book

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.sangharshAdmin.book.R.id
import com.sangharshAdmin.book.R.layout
import com.squareup.picasso.Picasso

class TestActivity : AppCompatActivity() {
    lateinit var chooseTestBannerBtn:Button
    lateinit var testBannerIV:ImageView
    lateinit var testTitle:EditText
    lateinit var progressBar:ProgressBar
    lateinit var testDescription:EditText
    lateinit var createTestBtn:Button

    lateinit var timeAllowed:EditText
    var  testBannerImg : Uri? = null
    var testBannerURL:Uri? = null
    lateinit var testTitleTxt:String


    lateinit var isPaidSwitch: Switch
    lateinit var priceEt: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_test)
         createTestBtn = findViewById<Button>(id.createTestBtn)
         testTitle = findViewById<EditText>(id.testTitle)
        testDescription = findViewById<EditText>(id.testDescription)
        timeAllowed = findViewById<EditText>(id.timeAllowed)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        chooseTestBannerBtn = findViewById(R.id.chooseTestBannerBtn)
        isPaidSwitch = findViewById<Switch>(id.isPaidSwitch)
        priceEt = findViewById<EditText>(id.priceET)

        isPaidSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                priceEt.visibility = View.VISIBLE
            } else {
                priceEt.visibility = View.GONE
            }
        }
        chooseTestBannerBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                0
            )
        })

        testTitleTxt = testTitle.text.toString()
        Log.i("banner fo if", testBannerImg.toString())

        createTestBtn.setOnClickListener(View.OnClickListener {
            if (isPaidSwitch.isChecked && priceEt.text.toString().isEmpty()) {
                priceEt.error = "This is required"
                return@OnClickListener
            } else {
                priceEt.error = null
            }
            if(testTitle.text.isNotEmpty() && !testDescription.text.isEmpty()&& !timeAllowed.text.isEmpty() && testBannerImg!= null) {
                uploadTestWithImage()
                Log.i("adi", "uploading test with image")
            }
            else if(!testTitle.text.isEmpty() && !testDescription.text.isEmpty()&& !timeAllowed.text.isEmpty() && testBannerImg==null){
                uploadTest()
                Log.i("adi", "uploading test without image")

            }
            else{
                Toast.makeText(this,"Please fill all the field",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun uploadTest() {
        val intent = Intent(this@TestActivity, AddQuestionActivity::class.java)
        val title = testTitle.text.toString()
        val testDescription = testDescription.text.toString()
        val timeAllowed = timeAllowed.text.toString()
        intent.putExtra("title",title)
        intent.putExtra("description",testDescription)
        intent.putExtra("timer",timeAllowed)
        if (isPaidSwitch.isChecked){
            intent.putExtra("isPaid", isPaidSwitch.isChecked)
            intent.putExtra("price", Integer.valueOf(priceEt.text.toString()))
        }
        startActivity(intent)
    }

    private fun uploadTestWithImage() {
        progressBar.visibility = View.VISIBLE
        createTestBtn.isEnabled = false

        val quesImageRef = FirebaseStorage.getInstance().reference.child("${testTitleTxt}/testbanner.png")
        quesImageRef.putFile(testBannerImg!!).addOnSuccessListener {
            quesImageRef.downloadUrl
                .addOnSuccessListener {
                    testBannerURL = it
                    val intent = Intent(this@TestActivity, AddQuestionActivity::class.java)
                    val title = testTitle.text.toString()
                    val testDescription = testDescription.text.toString()
                    val timeAllowed = timeAllowed.text.toString()
                    intent.putExtra("title",title)
                    intent.putExtra("description",testDescription)
                    intent.putExtra("timer",timeAllowed)
                    intent.putExtra("testBannerUrl",testBannerURL.toString())
                    if (isPaidSwitch.isChecked){
                        intent.putExtra("isPaid", isPaidSwitch.isChecked)
                        intent.putExtra("price", Integer.valueOf(priceEt.text.toString()))
                    }
                    startActivity(intent)
                    progressBar.visibility = View.GONE
                    Log.i("testbanner", "url generated successfully $testBannerURL ")
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed uploading image!",Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                    createTestBtn.isEnabled = true

                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== RESULT_OK && data!=null){
            testBannerImg = data.data!!
            testBannerIV = findViewById(R.id.testBannerIV)
            Picasso.get()
                .load(data.data)
                .into(testBannerIV)
            Log.i("adi", "getting the data $testBannerImg")
        }
        else if(data == null){
            testBannerImg = null
            Log.i("adi", "data is null")

        }
    }


}