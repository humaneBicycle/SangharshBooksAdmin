package com.sangharshAdmin.book

import android.content.DialogInterface
import android.icu.text.CaseMap.Title
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.sangharshAdmin.book.model.ShortTest
import com.sangharshAdmin.book.model.Test

class EditTestActivity : AppCompatActivity() {

    //Todo: Add Ques, Delete Ques, Edit ques image, title and options

    private lateinit var shortTest: ShortTest
    private lateinit var test: Test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_test)

        initialiseVariables()
        setUpInitialUI()
        downloadFullTest()
    }

    private fun initialiseVariables(){
        shortTest = Gson().fromJson(intent.getStringExtra("Test"), ShortTest::class.java);
    }

    private fun setUpInitialUI(){
        //Todo show the data available in short test model and show the progress bar to download
    }

    private fun downloadFullTest(){
        FirebaseFirestore.getInstance()
            .collection("directory")
            .document(shortTest.id)
            .get()
            .addOnCompleteListener {
                hideProgressBar()
                if (it.isSuccessful){
                    test = it.getResult().toObject(Test::class.java)!!
                    showEditQuestionButton()
                } else {
                    showError(it.exception?.message);
                }
            }
    }

    private fun showError(message: String?) {
     AlertDialog.Builder(this)
         .setTitle("Error on Downloading")
         .setMessage(message)
            .setPositiveButton("Cancel") { dialog, which ->
                this@EditTestActivity.finish() }
            .show()
    }

    private fun showEditQuestionButton() {
        TODO("Not yet implemented")
    }

    private fun hideProgressBar(){

    }

    //Function to save test to server
    private fun saveTest(){
        disableButtons()
        showProgressBar()

        FirebaseFirestore.getInstance()
            .collection("directory")
            .document(test.id)
            .set(test)
            .addOnCompleteListener {
                hideProgressBar()
                enableButtons()
                if (it.isSuccessful){
                    Toast.makeText(this@EditTestActivity, "Test was editted successfuly!", Toast.LENGTH_LONG).show()
                    this@EditTestActivity.finish()
                } else {
                    showError("While upload: " + it.exception!!.message);
                }
            }

            //Todo Edit short test on firebase also
    }

    private fun showProgressBar() {
        //Todo show Progress bar
    }

    private fun showProgressBar(message: String){
        //Todo make this
    }

    private fun disableButtons(){
        //Todo disable all buttons
    }

    private fun enableButtons(){
        //Todo enable all buttons
    }

    private fun editTestTitle(newTitle: String){
        shortTest.title = newTitle
        test.testTitle = newTitle
    }

    private fun editDescription(newDes: String){
        shortTest.description = newDes
        test.testDescription = newDes
    }

    private fun editTime(time: Int){
        shortTest.time = time.toString()
        test.timeAllowed = time
    }

    private fun editTestBanner(banner: String){
        shortTest.imageUrl = banner
        test.testBannerUrl = banner
    }

    private fun uploadBanner(image: Uri){
        showProgressBar("Uploading banner image")
        FirebaseStorage.getInstance()
            .reference
            .child(test.id)
            .child("banner.png")
            .putFile(image)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    FirebaseStorage.getInstance()
                        .reference
                        .child(test.id)
                        .child("banner.png")
                        .downloadUrl
                        .addOnCompleteListener {
                            hideProgressBar()
                            if (it.isSuccessful){
                                editTestBanner(it.result.toString())
                            } else showError("while getting banner url: " + it.exception!!.message)
                        }
                } else {
                    showError("while uploading banner: "+ it.exception!!.message)
                }
            }

    }

}