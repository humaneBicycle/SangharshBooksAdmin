package com.sangharshAdmin.book

import android.app.Application
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sangharshAdmin.book.model.Directory
import com.sangharshAdmin.book.model.Question
import com.sangharshAdmin.book.model.Test
import com.squareup.picasso.Picasso
import org.checkerframework.checker.units.qual.Length


class AddQuestionActivity : AppCompatActivity() {
    lateinit var questionImage  :ImageView
    lateinit var  optionAImage :ImageView
    lateinit var  optionBImage :ImageView
    lateinit var  optionCImage :ImageView
    lateinit var   optionDImage:ImageView
    var  questionPath :Uri? = null
    var  optionApath :Uri? = null
    var  optionBpath :Uri? = null
    var  optionCpath :Uri? = null
    var  optionDpath :Uri? = null
    val PICK_IMAGE = 1
    var test = Test();
    var addedQuestionsCount = 0
    lateinit var pd: ProgressDialog
    var isQuesImgSelected = false
    var isOptionAImageSelected = false
    var isOptionBImageSelected = false
    var isOptionCImageSelected = false
    var isOptionDImageSelected = false
    var url:String = ""

    var currentQestion = Question();


    lateinit var sangharshBooks : Application

    var pendingImagUploads = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        sangharshBooks = application as SangharshBooks


        val testId = FirebaseFirestore.getInstance()
            .collection("directory")
            .document()
            .id;
        test.id = testId;
        test.questions = ArrayList<Question>();


        val titleTV = findViewById<TextView>(R.id.titleTV)
        val title = intent.getStringExtra("title")
        val testDescription =intent.getStringExtra("description")
        titleTV.text = title
        val addQuestion = findViewById<Button>(R.id.addQuestionBtn)
         question = findViewById<EditText>(R.id.question)
         optionA = findViewById<EditText>(R.id.optionA)
         optionB = findViewById<EditText>(R.id.optionB)
         optionC = findViewById<EditText>(R.id.optionC)
         optionD = findViewById<EditText>(R.id.optionD)
         correctOption = findViewById<EditText>(R.id.correctOption)
         addedQuestion = findViewById<TextView>(R.id.tv_addedQuestions)
        val  questionImageBtn = findViewById<Button>(R.id.questionImageBtn)
        val  optionAImageBtn = findViewById<Button>(R.id.optionAImageBtn)
        val  optionBBImageBtn = findViewById<Button>(R.id.optionBImageBtn)
        val  optionCImageBtn = findViewById<Button>(R.id.optionCImageBtn)
        val  optionDImageBtn = findViewById<Button>(R.id.optionDImageBtn)
        questionImage  = findViewById<ImageView>(R.id.questionImage)
        optionAImage = findViewById<ImageView>(R.id.optionAImage)
        optionBImage = findViewById<ImageView>(R.id.optionBImage)
        optionCImage = findViewById<ImageView>(R.id.optionCImage)
        optionDImage= findViewById<ImageView>(R.id.optionDImage)



        addQuestion.setOnClickListener(View.OnClickListener {

            currentQestion = Question();

            if((question.text.isNotEmpty() || questionPath != null)
                && (optionA.text.isNotEmpty() || optionApath != null)
                && (optionB.text.isNotEmpty() || optionBpath != null)
                && (optionC.text.isNotEmpty() || optionCpath != null)
                && (optionD.text.isNotEmpty() || optionDpath != null)
            ){

                val questionTxt = question.text.toString()
                test.testTitle = title
                test.testDescription = testDescription

                currentQestion.question = questionTxt;

                if (!optionA.text.toString().isEmpty())
                    currentQestion.option1 = optionA.text.toString();

                if (!optionB.text.toString().isEmpty())
                    currentQestion.option2 = optionB.text.toString();

                if (!optionC.text.toString().isEmpty())
                    currentQestion.option3 = optionC.text.toString();

                if (!optionD.text.toString().isEmpty())
                    currentQestion.option4 = optionD.text.toString();

                if(questionPath!=null){
                    uploadImage(questionPath!!, "question", OnSuccessListener {
                        currentQestion.quesImgUrl = it.toString();
                        Picasso.get()
                            .load(R.drawable.imageplace)
                            .into(questionImage);
                        hideProgress()
                        isQuesImgSelected = false
                    })
                }


                if(optionApath!=null){
                    uploadImage(optionApath!!, "optionA", OnSuccessListener {
                        url = it.toString();
                        currentQestion.option1ImgUrl = it.toString();
                        Picasso.get()
                            .load(R.drawable.imageplace)
                            .into(optionAImage);
                        hideProgress()
                        isOptionAImageSelected = false
                    })
                }

                if(optionBpath!=null){
                    uploadImage(optionBpath!!, "optionB", OnSuccessListener {
                        currentQestion.option2ImgUrl = it.toString();

                        Picasso.get()
                            .load(R.drawable.imageplace)
                            .into(optionBImage);
                        hideProgress()
                        isOptionBImageSelected = false
                    })



                }

                if(optionCpath!=null){
                    uploadImage(optionCpath!!, "optionC", OnSuccessListener {
                        Log.i("optCUrl", url)
                        currentQestion.option3ImgUrl = it.toString();

                        Picasso.get()
                            .load(R.drawable.imageplace)
                            .into(optionCImage);
                        hideProgress()
                        isOptionCImageSelected = false
                    })


                }

                if(optionDpath!=null){
                    uploadImage(optionDpath!!, "optionD", OnSuccessListener {
                        Log.i("optDUrl", url)
                        currentQestion.option4ImgUrl = it.toString();
                        Picasso.get()
                            .load(R.drawable.imageplace)
                            .into(optionDImage);
                        hideProgress()
                        isOptionDImageSelected = false
                    })
                }

                currentQestion.correctOption = correctOption.text.toString()
                if (pendingImagUploads == 0){
                    saveCurrentQuestion()
                }
            }
            else{
                Toast.makeText(this,"Fill all the values and try again!",Toast.LENGTH_SHORT).show()
            }






        })

        // Adding images
        questionImageBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                0
            )
        })
        optionAImageBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                1
            )
        })
        optionBBImageBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                2
            )
        })
        optionCImageBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                3
            )
        })
        optionDImageBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                4
            )
        })


        findViewById<Button>(R.id.submitBtn).setOnClickListener {
            //TODO add progress dialog
            val newPD = ProgressDialog(this)
            newPD.setTitle("Uploading Test")
            newPD.setMessage("Uploading Test")
            newPD.setCancelable(false)
            FirebaseFirestore.getInstance().collection("directory")
                .whereEqualTo("path", (sangharshBooks as SangharshBooks).path).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if(it.result.toObjects(Directory::class.java).size==1) {
                            val id = it.result.documents[0].id
                            val directory = it.result.toObjects(Directory::class.java)[0]
                            directory.tests.add(test)
                            FirebaseFirestore.getInstance().collection("directory").document(id)
                                .set(directory)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                       FirebaseFirestore.getInstance()
                                           .collection("directory")
                                           .document(test.id)
                                           .set(test)
                                           .addOnCompleteListener{
                                               newPD.dismiss()
                                               if (it.isSuccessful){
                                                   Toast.makeText(this, "Uploaded Successfully!", Toast.LENGTH_LONG).show();
                                               }
                                           }
                                    }
                                }
                        }else{
                            Toast.makeText(this,"DataBaseError",Toast.LENGTH_LONG).show()
                        }

                    }
                }
        }




    }

    lateinit var addedQuestion: TextView
    lateinit var question: EditText
    lateinit var optionA: EditText
    lateinit var optionB: EditText
    lateinit var optionC: EditText
    lateinit var optionD: EditText
    lateinit var correctOption: EditText

    private fun saveCurrentQuestion() {
        test.questions.add(currentQestion);
        currentQestion = Question()
        addedQuestionsCount++
        addedQuestion.text = addedQuestionsCount.toString()
        question.text = null
        optionA.text = null
        optionB.text = null
        optionC.text = null
        optionD.text = null
        correctOption.text = null
    }

    private fun hideProgress() {
        pendingImagUploads--
        pd.setMessage("Uploads Pending: ${pendingImagUploads}")
        if (pendingImagUploads <= 0){
            pd.dismiss();
            saveCurrentQuestion()
        }
    }

    private fun uploadImage(image: Uri, postScript: String, successListener: OnSuccessListener<in Uri>){
        if(pendingImagUploads <=0){
            pd = ProgressDialog(this)
            pd.setTitle("Uploading Images")
            pd.show()
        }
        pendingImagUploads++;
        pd.setMessage("Uploads Pending: $pendingImagUploads")
        val imageRef = FirebaseStorage.getInstance().reference.child("${test.id}/Q${addedQuestionsCount}images/${postScript}.png")
        imageRef.putFile(image)
            .addOnSuccessListener {
                imageRef.downloadUrl
                    .addOnSuccessListener(successListener)
                    .addOnFailureListener{
                        Toast.makeText(this,"Image -${postScript} Download url is not fetched!",Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Image -${postScript} is not uploaded",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null){
            questionPath = data.data!!
            Picasso.get()
                .load(questionPath)
                .into(questionImage)
            isQuesImgSelected = true

        }

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            optionApath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionAImage)
            isOptionAImageSelected = true

        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            optionBpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionBImage)
            isOptionBImageSelected = true
        }
        if (requestCode == 3 && resultCode == RESULT_OK && data != null){
            optionCpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionCImage)
            isOptionCImageSelected = true
        }
        if (requestCode == 4 && resultCode == RESULT_OK && data != null){
            optionDpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionDImage)
            isOptionDImageSelected =true
        }

    }







}