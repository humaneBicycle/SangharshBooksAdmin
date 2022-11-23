package com.sangharshAdmin.book

import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sangharshAdmin.book.model.Directory
import com.sangharshAdmin.book.model.Question
import com.sangharshAdmin.book.model.ShortTest
import com.sangharshAdmin.book.model.Test
import com.squareup.picasso.Picasso


class AddQuestionActivity : AppCompatActivity() {
    lateinit var questionImage  :ImageView
    lateinit var  optionAImage :ImageView
    lateinit var  optionBImage :ImageView
    lateinit var  optionCImage :ImageView
    lateinit var   optionDImage:ImageView
    lateinit var   questionIV:ImageView
    lateinit var   optionAIV:ImageView
    lateinit var   optionBIV:ImageView
    lateinit var   optionCIV:ImageView
    lateinit var   optionDIV:ImageView
    lateinit var   quesDrawerGrid:GridLayout
    lateinit var   quesGridLL:LinearLayout
    lateinit var   updateQuesBtn:TextView
    lateinit var   backGridbtn:TextView
    lateinit var   correctOptionTV:TextView
    lateinit var   questionET:EditText
    lateinit var   optionAET:EditText
    lateinit var   optionBET:EditText
    lateinit var   optionCET:EditText
    lateinit var   optionDET:EditText
    lateinit var   addQuestionLL:LinearLayout
    lateinit var   btnsInGridLL:LinearLayout
    lateinit var   arrayOfIndex:Array<CharSequence>
    lateinit var tv : View
    lateinit var  spinner : Spinner
   var  indexForUpdating : Int =0
    var   correctOption : Int = -2
    var  questionPath :Uri? = null
    var  optionApath :Uri? = null
    var  optionBpath :Uri? = null
    var  optionCpath :Uri? = null
    var  optionDpath :Uri? = null
     var  updateQuestionPath :Uri? = null
    var  updateOptionApath :Uri? = null
    var  updateOptionBpath :Uri? = null
    var  updateOptionCpath :Uri? = null
    var  updateOptionDpath :Uri? = null
    var testBannerUrl:String? = null
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

    var currentQestion = Question()
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
        testBannerUrl = intent.getStringExtra("testBannerUrl")
        test.testBannerUrl = testBannerUrl
        val titleTV = findViewById<TextView>(R.id.titleTV)
        val title = intent.getStringExtra("title")
        titleTV.text = title
        val testDescription =intent.getStringExtra("description")
        val timeAllowed =Integer.parseInt(intent.getStringExtra("timer").toString())
        val addQuestion = findViewById<Button>(R.id.addQuestionBtn)
         question = findViewById<EditText>(R.id.question)
         optionA = findViewById<EditText>(R.id.optionA)
         optionB = findViewById<EditText>(R.id.optionB)
         optionC = findViewById<EditText>(R.id.optionC)
         optionD = findViewById<EditText>(R.id.optionD)
//         correctOption = findViewById<EditText>(R.id.correctOption)
         addedQuestion = findViewById<TextView>(R.id.tv_addedQuestions)
//        val  questionImageBtn = findViewById<Button>(R.id.questionImageBtn)
//        val  optionAImageBtn = findViewById<Button>(R.id.optionAImageBtn)
//        val  optionBBImageBtn = findViewById<Button>(R.id.optionBImageBtn)
//        val  optionCImageBtn = findViewById<Button>(R.id.optionCImageBtn)
//        val  optionDImageBtn = findViewById<Button>(R.id.optionDImageBtn)
        questionImage  = findViewById<ImageView>(R.id.questionImage)
        optionAImage = findViewById<ImageView>(R.id.optionAImage)
        optionBImage = findViewById<ImageView>(R.id.optionBImage)
        optionCImage = findViewById<ImageView>(R.id.optionCImage)
        optionDImage= findViewById<ImageView>(R.id.optionDImage)
        spinner = findViewById(R.id.spinner)
        quesDrawerGrid = findViewById(R.id.quesDrawerGrid)
        quesGridLL = findViewById(R.id.quesGridLL)
        addQuestionLL = findViewById(R.id.addQuestionLL)
        updateQuesBtn = findViewById(R.id.updateQuesBtn)
        backGridbtn = findViewById(R.id.backGridbtn)
        btnsInGridLL = findViewById(R.id.btnsInGridLL)
        questionIV = findViewById(R.id.questionIV)
        optionAIV = findViewById(R.id.optionAIV)
        optionBIV = findViewById(R.id.optionBIV)
        optionCIV = findViewById(R.id.optionCIV)
        optionDIV = findViewById(R.id.optionDIV)
        questionET = findViewById(R.id.questionET)
        optionAET = findViewById(R.id.optionAET)
        optionBET = findViewById(R.id.optionBET)
        optionCET = findViewById(R.id.optionCET)
        optionDET = findViewById(R.id.optionDET)
        correctOptionTV = findViewById(R.id.correctOptionTV)


        val items = arrayOf("A", "B", "C", "D")
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, items)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                correctOption = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@AddQuestionActivity,"Select the correct option",Toast.LENGTH_LONG).show()
            }


        }


        addQuestion.setOnClickListener(View.OnClickListener {
            currentQestion = Question()
            if((question.text.isNotEmpty() || questionPath != null)
                && (optionA.text.isNotEmpty() || optionApath != null)
                && (optionB.text.isNotEmpty() || optionBpath != null)
                && (optionC.text.isNotEmpty() || optionCpath != null)
                && (optionD.text.isNotEmpty() || optionDpath != null)
            ){
                val questionTxt = question.text.toString()
                test.testTitle = title
                test.testDescription = testDescription
                test.timeAllowed = timeAllowed
                test.noOfQuestion = addedQuestionsCount+1

                currentQestion.question = questionTxt
                 val index = addedQuestionsCount+1
                tv = layoutInflater.inflate(R.layout.question_grid_textview,null)
                tv.tag = index
                quesDrawerGrid.addView(tv)
                quesDrawerGrid.findViewWithTag<TextView>(index).text= index.toString()
//                indexForUpdating = Integer.parseInt(quesDrawerGrid.findViewWithTag<TextView>(index).text.toString())-1
                 arrayOfIndex = arrayOf( quesDrawerGrid.findViewWithTag<TextView>(index).text)
                tv.setOnClickListener(View.OnClickListener {
//                    Log.i("adi index ", indexForUpdating.toString())
                    onClickEventInGrid(index)
                })


                if (optionA.text.toString().isNotEmpty())
                    currentQestion.option1 = optionA.text.toString();

                if (optionB.text.toString().isNotEmpty())
                    currentQestion.option2 = optionB.text.toString();

                if (optionC.text.toString().isNotEmpty())
                    currentQestion.option3 = optionC.text.toString();

                if (optionD.text.toString().isNotEmpty())
                    currentQestion.option4 = optionD.text.toString();



//                Log.i("before updating question $index", test.questions[index-2].question)
//                Log.i("before updating optionA", test.questions[index-1].option1)
//                Log.i("before updating optionB", test.questions[index-1].option2)
//                Log.i("before updating optionC", test.questions[index-1].option3)
//                Log.i("before updating optionD", test.questions[index-1].option4)

                if(questionPath!=null){
                    uploadImage(questionPath!!, "question", OnSuccessListener {
                        currentQestion.quesImgUrl = it.toString();
                        Picasso.get()
                            .load(R.drawable.imageplace)
                            .into(questionImage);
                        hideProgress()
                        isQuesImgSelected = false
                    })
//                        questionPath= null
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
//                    optionApath = null
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
//                    optionBpath = null
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
//                    optionCpath = null

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
//                    optionDpath = null
                }

                currentQestion.correctOption = correctOption

                if (pendingImagUploads == 0){
                    saveCurrentQuestion()

                }
            }
            else{
                Toast.makeText(this,"Fill all the values and try again!",Toast.LENGTH_SHORT).show()
            }

        })

        backGridbtn.setOnClickListener(View.OnClickListener {
            addQuestionLL.visibility = View.VISIBLE
            quesGridLL.visibility = View.GONE
            btnsInGridLL.visibility = View.GONE
            questionIV.visibility=View.GONE
            optionAIV.visibility=View.GONE
            optionBIV.visibility=View.GONE
            optionCIV.visibility=View.GONE
            optionDIV.visibility=View.GONE
            questionPath = null
            optionApath = null
            optionBpath = null
            optionCpath = null
            optionDpath = null
            updateQuestionPath = null
            updateOptionApath = null
            updateOptionBpath = null
            updateOptionCpath = null
            updateOptionDpath = null

        })

        //TODO
        updateQuesBtn.setOnClickListener(View.OnClickListener {
            Log.i("adi checking index", "click question index is $indexForUpdating")

            updateQuestion(indexForUpdating)
//            Log.i("after updating question $index", test.questions[index-1].question)
//            Log.i("after updating optionA", test.questions[index-1].option1)
//            Log.i("after updating optionB", test.questions[index-1].option2)
//            Log.i("after updating optionC", test.questions[index-1].option3)
//            Log.i("after updating optionD", test.questions[index-1].option4)
        })




//         Adding images
        questionImage.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                0
            )
        })
        optionAImage.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                1
            )
        })
        optionBImage.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                2
            )
        })
        optionCImage.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                3
            )
        })
        optionDImage.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                4
            )
        })


        findViewById<Button>(R.id.submitBtn).setOnClickListener {
            val newPD = ProgressDialog(this)
            newPD.setTitle("Uploading Test")
            newPD.setMessage("Uploading Test")
            newPD.setCancelable(false)
            newPD.show()
            FirebaseFirestore.getInstance().collection("directory")
                .whereEqualTo("path", (sangharshBooks as SangharshBooks).path).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if(it.result.toObjects(Directory::class.java).size==1) {
                            val id = it.result.documents[0].id
                            val directory = it.result.toObjects(Directory::class.java)[0]
                            val shorty = ShortTest()
                            shorty.id = test.id;
                            shorty.title = test.testTitle;
                            shorty.numQuestion = test.questions.size
                            shorty.description = test.testDescription
                            shorty.time = test.timeAllowed.toString()
                            directory.tests.add(shorty)
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
                                                   finish()
                                               }
                                           }
                                    }
                                }
                        }else{
                            Toast.makeText(this,"DataBaseError",Toast.LENGTH_LONG).show()
                            newPD.dismiss()
                        }

                    }
                }
        }
    }

    private fun updateQuestion(index: Int) {
       if(test.questions[index].question!=questionET.text.toString() ){
           test.questions[index].question= questionET.text.toString()
       }
    if(test.questions[index].option1!=optionAET.text.toString() ){
           test.questions[index].option1= optionAET.text.toString()

       }
    if(test.questions[index].option2!=optionBET.text.toString() ){
           test.questions[index].question= questionET.text.toString()

       }
    if(test.questions[index].option3!=optionCET.text.toString() ){
           test.questions[index].option3= optionCET.text.toString()

       }
    if(test.questions[index].question!=questionET.text.toString() ){
           test.questions[index].option4= optionDET.text.toString()

       }
        if(updateQuestionPath!= null){
            test.questions[indexForUpdating].quesImgUrl = updateQuestionPath.toString()
        }
        if(updateOptionApath!= null){
            test.questions[indexForUpdating].option1ImgUrl = updateOptionApath.toString()
        }
        if(updateOptionBpath!= null){
            test.questions[indexForUpdating].option2ImgUrl = updateOptionBpath.toString()
        }
       if(updateOptionCpath!= null){
            test.questions[indexForUpdating].option3ImgUrl = updateOptionCpath.toString()
        }
       if(updateOptionDpath!= null){
            test.questions[indexForUpdating].option4ImgUrl = updateOptionDpath.toString()
        }




        addQuestionLL.visibility = View.VISIBLE
        quesGridLL.visibility = View.GONE
        btnsInGridLL.visibility = View.GONE
        questionIV.visibility=View.GONE
        optionAIV.visibility=View.GONE
        optionBIV.visibility=View.GONE
        optionCIV.visibility=View.GONE
        optionDIV.visibility=View.GONE
        Toast.makeText(this,"Updated successfully",Toast.LENGTH_LONG).show()


    }

    private fun onClickEventInGrid(index: Int) {
        indexForUpdating = index-1
        addQuestionLL.visibility = View.GONE
        quesGridLL.visibility = View.VISIBLE
        btnsInGridLL.visibility = View.VISIBLE
//        Log.i("question saved", test.questions[index-1].question )
        if(question.text!=null){
            questionET.setText( test.questions[index-1].question)
        }
        if(test.questions[index-1].quesImgUrl!=null){
            questionIV.visibility = View.VISIBLE
            Log.i("images q", test.questions[index-1].quesImgUrl)
            Picasso.get().load(test.questions[index-1].quesImgUrl).into(questionIV)
            questionIV.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select an image"),
                    5
                )
            })
        }
        if(optionA.text!=null){
            optionAET.setText( test.questions[index-1].option1)
        }
        if(test.questions[index-1].option1ImgUrl!=null){
            optionAIV.visibility = View.VISIBLE
            Picasso.get().load(test.questions[index-1].option1ImgUrl).into(optionAIV)
            optionAIV.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select an image"),
                    6
                )
            })
        }
        if(optionB.text!=null){
            optionBET.setText( test.questions[index-1].option2)
        }
        if(test.questions[index-1].option2ImgUrl!=null){
            optionBIV.visibility = View.VISIBLE
            Log.i("images q", test.questions[index-1].option2ImgUrl)
            Picasso.get().load(test.questions[index-1].option2ImgUrl).into(optionBIV)
            optionBIV.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select an image"),
                    7
                )
            })
        }
        if(optionC.text!=null){
            optionCET.setText( test.questions[index-1].option3)
        }
        if(test.questions[index-1].option3ImgUrl!=null){
            optionCIV.visibility = View.VISIBLE
            Log.i("images q", test.questions[index-1].option3ImgUrl)
            Picasso.get().load(test.questions[index-1].option3ImgUrl).into(optionCIV)
            optionCIV.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select an image"),
                    8
                )
            })
        }
        if(optionD.text!=null){
            optionDET.setText( test.questions[index-1].option4)
        }
        if(test.questions[index-1].option4ImgUrl!=null){
            optionDIV.visibility = View.VISIBLE
            Log.i("images q", test.questions[index-1].option4ImgUrl)
            Picasso.get().load(test.questions[index-1].option4ImgUrl).into(optionDIV)
            optionDIV.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select an image"),
                    9
                )
            })
        }
        if(test.questions[index-1].correctOption ==0){

            correctOptionTV.text = "Correct Option : A"
        }
        if(test.questions[index-1].correctOption ==1){

            correctOptionTV.text ="Correct Option : B"
        }
        if(test.questions[index-1].correctOption ==2){

            correctOptionTV.text ="Correct Option : C"
        }
        if(test.questions[index-1].correctOption ==3){

            correctOptionTV.text ="Correct Option : D"
        }


    }

    lateinit var addedQuestion: TextView
    lateinit var question: EditText
    lateinit var optionA: EditText
    lateinit var optionB: EditText
    lateinit var optionC: EditText
    lateinit var optionD: EditText

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
        questionPath= null
        optionApath= null
        optionBpath= null
        optionCpath= null
        optionDpath= null
        optionDpath= null

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
        if (requestCode == 5 && resultCode == RESULT_OK && data != null){
             updateQuestionPath= data.data!!
            Picasso.get()
                .load(data.data)
                .into(questionIV)
        }
        if (requestCode == 6 && resultCode == RESULT_OK && data != null){
            updateOptionApath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionAIV)
        }
        if (requestCode == 7 && resultCode == RESULT_OK && data != null){
            updateOptionBpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionBIV)
            isOptionDImageSelected =true
        }
        if (requestCode == 8 && resultCode == RESULT_OK && data != null){
            updateOptionCpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionCIV)
        }
        if (requestCode == 9 && resultCode == RESULT_OK && data != null){
            updateOptionDpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionDIV)
        }


    }







}