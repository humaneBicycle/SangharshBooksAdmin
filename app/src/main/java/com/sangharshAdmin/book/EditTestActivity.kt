package com.sangharshAdmin.book

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.sangharshAdmin.book.adapter.QuestionsListAdapter
import com.sangharshAdmin.book.model.Question
import com.sangharshAdmin.book.model.ShortTest
import com.sangharshAdmin.book.model.Test
import com.squareup.picasso.Picasso

class EditTestActivity : AppCompatActivity() {

    //Todo: Add Ques, Delete Ques, Edit ques image, title and options

    private lateinit var shortTest: ShortTest
    private lateinit var test: Test
    private lateinit var titleET:EditText
    private lateinit var descriptionET:EditText
    private lateinit var addNewQuesBtn:TextView
    private lateinit var saveTestBtn:TextView
    private lateinit var addAndShowQuesLL: LinearLayout
    private lateinit var welcomeLL: LinearLayout
    private lateinit var testLLETA: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var questionET: EditText
    private lateinit var optionAET: EditText
    private lateinit var optionBET: EditText
    private lateinit var optionCET: EditText
    private lateinit var optionDET: EditText
    private lateinit var addQuestionET: EditText
    private lateinit var addOptionAET: EditText
    private lateinit var addOptionBET: EditText
    private lateinit var addOptionCET: EditText
    private lateinit var addOptionDET: EditText
    private lateinit var optionAIV: ImageView
    private lateinit var optionBIV: ImageView
    private lateinit var optionCIV: ImageView
    private lateinit var optionDIV: ImageView
    private lateinit var questionIV: ImageView
    private lateinit var addQuestionIV: ImageView
    private lateinit var addOptionAIV: ImageView
    private lateinit var addOptionBIV: ImageView
    private lateinit var addOptionCIV: ImageView
    private lateinit var addOptionDIV: ImageView
    private lateinit var editBannerIcon: ImageView
    private lateinit var deleteBannerIcon: ImageView
    private lateinit var progressLL: LinearLayout
    private lateinit var progressMessageTV: TextView
    private lateinit var viewQuesBtn: TextView
    private lateinit var saveQuesBtn: TextView
    private lateinit var addNewQuesLL: LinearLayout
    private lateinit var spinnerAddQues: Spinner
    private lateinit var newQuestion: Question
    private lateinit var backBtnAtNewQuesLL: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewLL: LinearLayout
    private lateinit var quesNoTV: TextView
    private lateinit var backBtnDuringUpdate: TextView
    private lateinit var updateBtnDuringUpdate: TextView
    private lateinit var backBtnDuringRecyclerView: ImageView
    private lateinit var backBtnDuringWelcomeLL: ImageView
    private lateinit var testBannerETA: ImageView
    var questions = ArrayList<String>()
    var currentQuestion = 0
    var correctOption =-2
    var  newQuestionPath :Uri? = null
    var  newOptionApath :Uri? = null
    var  newOptionBpath :Uri? = null
    var  newOptionCpath :Uri? = null
    var  newOptionDpath :Uri? = null
    var  updatedQuestionPath:Uri? = null
    var  updatedOptionApath:Uri? = null
    var  updatedOptionBpath:Uri? = null
    var  updatedOptionCpath:Uri? = null
    var  updatedOptionDpath:Uri? = null
    var  updatedBannerPath:Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_test)

        initialiseVariables()
        setUpInitialUI()
        setListeners()
        spinnerAdapter()


    }



    private fun spinnerAdapter() {
        val items = arrayOf("A", "B", "C", "D")
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, items)
        spinnerAddQues.adapter = adapter
        spinnerAddQues.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                correctOption = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@EditTestActivity,"Select the correct option",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setListeners() {
        viewQuesBtn.setOnClickListener(View.OnClickListener {
            welcomeLL.visibility = View.GONE
            addAndShowQuesLL.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerViewLL.visibility = View.VISIBLE
        })
        addNewQuesBtn.setOnClickListener(View.OnClickListener {
            welcomeLL.visibility = View.GONE
            addNewQuesLL.visibility = View.VISIBLE
            addAndShowQuesLL.visibility = View.GONE
        })

        saveTestBtn.setOnClickListener(View.OnClickListener {
            saveTest()
        })

        addQuestionIV.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                0
            )
        })
addOptionAIV.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                1
            )
        })
        addOptionBIV.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                2
            )
        })
        addOptionCIV.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                3
            )
        })
        addOptionDIV.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                4
            )
        })

        saveQuesBtn.setOnClickListener(View.OnClickListener {
            saveNewQuestion()
        })
        backBtnAtNewQuesLL.setOnClickListener(View.OnClickListener {
            welcomeLL.visibility = View.VISIBLE
            addNewQuesLL.visibility = View.GONE
            addAndShowQuesLL.visibility = View.VISIBLE

        })
        backBtnDuringUpdate.setOnClickListener(View.OnClickListener {
            testLLETA.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerViewLL.visibility = View.VISIBLE
        })
        updateBtnDuringUpdate.setOnClickListener(View.OnClickListener {

            if(test.questions[currentQuestion].question != questionET.text.toString()){
               editQuestion()
            }
            if(test.questions[currentQuestion].option1 != optionAET.text.toString()){
               editOptionA()
            }
            if(test.questions[currentQuestion].option2 != optionBET.text.toString()){
               editOptionB()
            }
            if(test.questions[currentQuestion].option3 != optionCET.text.toString()){
               editOptionC()
            }
            if(test.questions[currentQuestion].option4 != optionDET.text.toString()){
               editOptionD()
            }
            if(updatedQuestionPath!=null){
                editQuestionImage(currentQuestion,updatedQuestionPath!!)
            }
            if(updatedOptionApath!=null){
                editOption1Image(currentQuestion,updatedOptionApath!!)
            }
            if(updatedOptionBpath!=null){
                editOption2Image(currentQuestion,updatedOptionBpath!!)
            }
            if(updatedOptionCpath!=null){
                editOption3Image(currentQuestion,updatedOptionDpath!!)
            }
            if(updatedOptionDpath!=null){
                editOption4Image(currentQuestion,updatedOptionDpath!!)
            }
            Toast.makeText(this,"Updated successfully..",Toast.LENGTH_LONG).show()
            testLLETA.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerViewLL.visibility = View.VISIBLE

        })
        backBtnDuringRecyclerView.setOnClickListener(View.OnClickListener {
            welcomeLL.visibility = View.VISIBLE
            addAndShowQuesLL.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            recyclerViewLL.visibility = View.GONE
        })
        backBtnDuringWelcomeLL.setOnClickListener(View.OnClickListener {
            finish()
        })
        editBannerIcon.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                10
            )
        })
        deleteBannerIcon.setOnClickListener(View.OnClickListener {
            editTestBanner(null)
            Picasso.get().load(R.drawable.imageplace).into(testBannerETA)
        })


    }

    private fun editQuestion(){
        test.questions[currentQuestion].question = questionET.text.toString()
//        test.questions.get(4).option1 = "New Option Value"
    }
    private fun editOptionA() {
        test.questions[currentQuestion].option1 = optionAET.text.toString()
    }
    private fun editOptionB() {
        test.questions[currentQuestion].option2 = optionBET.text.toString()
    }
    private fun editOptionC() {
        test.questions[currentQuestion].option3 = optionCET.text.toString()
    }
    private fun editOptionD() {
        test.questions[currentQuestion].option4 = optionDET.text.toString()
    }

    private fun saveNewQuestion() {
        newQuestion = Question()
        if (addQuestionET.text.toString().isNotEmpty())
            newQuestion.question = addQuestionET.text.toString()
        if (addOptionAET.text.toString().isNotEmpty())
            newQuestion.option1 = addOptionAET.text.toString()

        if (addOptionBET.text.toString().isNotEmpty())
            newQuestion.option2 = addOptionBET.text.toString()

        if (addOptionCET.text.toString().isNotEmpty())
            newQuestion.option3 = addOptionCET.text.toString()

        if (addOptionDET.text.toString().isNotEmpty())
            newQuestion.option4 = addOptionDET.text.toString()

        newQuestion.correctOption = correctOption
        if(newQuestionPath!=null){
            editQuestionImage(test.questions.size,newQuestionPath!!)
        }
        if(newOptionApath!=null){
            editOption1Image(test.questions.size,newOptionApath!!)
        }
        if(newOptionBpath!=null){
            editOption1Image(test.questions.size,newOptionBpath!!)
        }
        if(newOptionCpath!=null){
            editOption1Image(test.questions.size,newOptionCpath!!)
        }
        if(newOptionDpath!=null){
            editOption1Image(test.questions.size,newOptionDpath!!)
        }
        addQuestions(newQuestion)
        hideAddQuestionLayout()
    }

    private fun hideAddQuestionLayout() {
        addQuestionET.text = null
        addOptionAET.text = null
        addOptionBET.text = null
        addOptionCET.text = null
        addOptionDET.text = null
        newQuestionPath= null
        newOptionApath= null
        newOptionBpath= null
        newOptionCpath= null
        newOptionDpath= null
        Picasso.get().load(R.drawable.imageplace).into(addQuestionIV)
        Picasso.get().load(R.drawable.imageplace).into(addOptionAIV)
        Picasso.get().load(R.drawable.imageplace).into(addOptionBIV)
        Picasso.get().load(R.drawable.imageplace).into(addOptionCIV)
        Picasso.get().load(R.drawable.imageplace).into(addOptionDIV)
        addNewQuesLL.visibility = View.GONE
        welcomeLL.visibility = View.VISIBLE
        addAndShowQuesLL.visibility = View.VISIBLE
    }

    private fun loadTestInUI(currentQuestion:Int) {
        quesNoTV.text = "Question ${currentQuestion+1}"
        if (test.questions[currentQuestion].question!=null){
            questionET.setText(test.questions[currentQuestion].question)
        }
        if(test.questions[currentQuestion].quesImgUrl!=null){
            questionIV.visibility = View.VISIBLE
            Picasso.get().load(test.questions[currentQuestion].quesImgUrl).into(questionIV)
            startEditImageOption()
        }
        if (test.questions[currentQuestion].option1!=null){
            optionAET.setText(test.questions[currentQuestion].option1)
        }
        if(test.questions[currentQuestion].option1ImgUrl!=null){
            optionAIV.visibility = View.VISIBLE
            Picasso.get().load(test.questions[currentQuestion].option1ImgUrl).into(optionAIV)
        }
        if (test.questions[currentQuestion].option2!=null){
            optionBET.setText(test.questions[currentQuestion].option2)
        }
        if(test.questions[currentQuestion].option2ImgUrl!=null){
            optionBIV.visibility = View.VISIBLE
            Picasso.get().load(test.questions[currentQuestion].option2ImgUrl).into(optionBIV)

        }
        if(test.questions[currentQuestion].option3!=null){
            optionCET.setText(test.questions[currentQuestion].option3)
        }
        if(test.questions[currentQuestion].option3ImgUrl!=null){
            optionCIV.visibility = View.VISIBLE
            Picasso.get().load(test.questions[currentQuestion].option3ImgUrl).into(optionCIV)


        }
        if (test.questions[currentQuestion].option4!=null){
            optionDET.setText(test.questions[currentQuestion].option4)
        }
        if(test.questions[currentQuestion].option4ImgUrl!=null){
            optionDIV.visibility = View.VISIBLE
            Picasso.get().load(test.questions[currentQuestion].option4ImgUrl).into(optionDIV)
        }

    }

    private fun startEditImageOption() {
        questionIV.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                5
            )
        })
    optionAIV.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                6
            )
        })
    optionBIV.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                7
            )
        })
    optionCIV.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select an image"),
                8
            )
        })
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

    private fun initialiseVariables(){
        shortTest = Gson().fromJson(intent.getStringExtra("Test"), ShortTest::class.java)
        titleET = findViewById(R.id.testTitleETA)
        descriptionET = findViewById(R.id.testDescriptionETA)
        addAndShowQuesLL = findViewById(R.id.addAndShowBtnsLL)
        welcomeLL = findViewById(R.id.welcomeLL)
        testLLETA = findViewById(R.id.testLLETA)
        progressBar = findViewById(R.id.progressBar)
        questionET = findViewById(R.id.questionETETA)
        optionAET = findViewById(R.id.optionAETETA)
        optionBET = findViewById(R.id.optionBETETA)
        optionCET = findViewById(R.id.optionCETETA)
        optionDET = findViewById(R.id.optionDETETA)
        questionIV = findViewById(R.id.questionIVETA)
        optionAIV = findViewById(R.id.optionAETAIV)
        optionBIV = findViewById(R.id.optionBETAIV)
        optionCIV = findViewById(R.id.optionCETAIV)
        optionDIV = findViewById(R.id.optionDETAIV)
        progressLL = findViewById(R.id.progressLL)
        progressMessageTV = findViewById(R.id.progressMessageTV)
        saveQuesBtn = findViewById(R.id.saveQuesBtn)
        viewQuesBtn = findViewById(R.id.viewQuesBtn)
        addQuestionET = findViewById(R.id.addQuestionET)
        addOptionAET = findViewById(R.id.addOptionAET)
        addOptionBET = findViewById(R.id.addOptionBET)
        addOptionCET = findViewById(R.id.addOptionCET)
        addOptionDET = findViewById(R.id.addOptionDET)
        addQuestionIV = findViewById(R.id.addQuestionImageIV)
        addOptionAIV = findViewById(R.id.addOptionAImageIV)
        addOptionBIV = findViewById(R.id.addOptionBImageIV)
        addOptionCIV = findViewById(R.id.addOptionCImageIV)
        addOptionDIV = findViewById(R.id.addOptionDImageIV)
        editBannerIcon = findViewById(R.id.editBannerIcon)
        deleteBannerIcon = findViewById(R.id.deleteBannerIcon)
        spinnerAddQues = findViewById(R.id.spinnerAddQues)
        saveTestBtn = findViewById(R.id.saveTestBtn)
        addNewQuesBtn = findViewById(R.id.addNewQuesBtn)
        addNewQuesLL = findViewById(R.id.addNewQuesLL)
        backBtnAtNewQuesLL= findViewById(R.id.backBtnAtNewQuesLL)
        recyclerView= findViewById(R.id.recyclerViewOfQuestions)
        quesNoTV= findViewById(R.id.quesNoTV)
        backBtnDuringUpdate= findViewById(R.id.backBtnDuringUpdate)
        updateBtnDuringUpdate= findViewById(R.id.updateBtnDuringUpdate)
        recyclerViewLL= findViewById(R.id.recyclerViewLL)
        backBtnDuringRecyclerView= findViewById(R.id.backBtnDuringRecyclerView)
        backBtnDuringWelcomeLL= findViewById(R.id.backBtnDuringWelcomeLL)
        testBannerETA= findViewById(R.id.testBannerETA)
        downloadFullTest()
    }

    private fun setUpInitialUI(){
        titleET.setText(shortTest.title)
        descriptionET.setText(shortTest.description)
        if(shortTest.imageUrl!=null){
            Picasso.get().load(shortTest.imageUrl).into(testBannerETA)
        }
        else Picasso.get().load(R.drawable.imageplace).into(testBannerETA)

    }


    private fun downloadFullTest(){
        welcomeLL.visibility = View.GONE
        showProgressBar("Downloading test")
        FirebaseFirestore.getInstance()
            .collection("directory")
            .document(shortTest.id)
            .get()
            .addOnCompleteListener {
                hideProgressBar()
                if (it.isSuccessful){
                    test = it.result.toObject(Test::class.java)!!
                    hideProgressBar()
                    showEditQuestionButton()
                    createRecyclerView()
                } else {
                    showError(it.exception?.message);
                }
            }
    }

    private fun createRecyclerView() {
        for(index in 0 until test.questions.size) {
            if(test.questions[index].question!=null)
                questions.add(test.questions[index].question)
            else questions.add("Question $index")
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = QuestionsListAdapter(questions, object : QuestionsListAdapter.Listener{
            override fun questionClicked(qIndex: Int) {
                testLLETA.visibility = View.VISIBLE
                addAndShowQuesLL.visibility = View.GONE
                recyclerView.visibility = View.GONE
                recyclerViewLL.visibility = View.GONE
                currentQuestion = qIndex
                loadTestInUI(qIndex)

            }
        })
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
        welcomeLL.visibility = View.VISIBLE
        addAndShowQuesLL.visibility = View.VISIBLE
            viewQuesBtn.visibility = View.VISIBLE
            viewQuesBtn.isEnabled = true
        addNewQuesBtn.visibility = View.VISIBLE
            addNewQuesBtn.isEnabled = true
    }


    private fun hideProgressBar(){
        progressLL.visibility = View.GONE
    }

    //Function to save test to server
    private fun saveTest(){
        disableButtons()
        showProgressBar("Updating Test")
        if(test.testTitle!= titleET.text.toString()){
            editTestTitle(titleET.text.toString())
        }
        if(test.testDescription!= descriptionET.text.toString()){
            editDescription(descriptionET.text.toString())
        }


        FirebaseFirestore.getInstance()
            .collection("directory")
            .document(test.id)
            .set(test)
            .addOnCompleteListener {
                hideProgressBar()
                enableButtons()
                if (it.isSuccessful){
                    Toast.makeText(this@EditTestActivity, "Test was edited successfully!", Toast.LENGTH_LONG).show()
                    this@EditTestActivity.finish()
                } else {
                    showError("While upload: " + it.exception!!.message);
                }
            }

            //Todo Edit short test on firebase also
    }

    private fun showProgressBar() {
       progressLL.visibility = View.VISIBLE
    }

    private fun showProgressBar(message: String){
        progressLL.visibility = View.VISIBLE
        progressMessageTV.text = message

    }

    private fun disableButtons(){
        //Todo disable all buttons
    }

    private fun enableButtons(){
        //Todo enable all buttons
    }

    private fun addQuestions(newQues: Question){
        if (test.questions == null)
            test.questions = ArrayList<Question>()
        test.questions.add(newQues)
        test.noOfQuestion++
        shortTest.numQuestion++
    }

    private fun deleteQues(index: Int){
        test.questions.removeAt(index)
        test.noOfQuestion--
        shortTest.numQuestion--
    }


    private fun editQuestionImage(index: Int, uri: Uri){
        uploadImage(uri, index, "question", object : UploadListener {
            override fun uploaded(url: String) {
                test.questions.get(index).quesImgUrl = url;
            }
        })
    }

    private fun editOption1Image(questionIndex: Int, uri: Uri){
        uploadImage(uri, questionIndex, "optionA", object : UploadListener{
            override fun uploaded(url: String) {
                test.questions.get(questionIndex).option1ImgUrl = url
            }
        })
    }
    private fun editOption2Image(questionIndex: Int, uri: Uri){
        uploadImage(uri, questionIndex, "optionA", object : UploadListener{
            override fun uploaded(url: String) {
                test.questions.get(questionIndex).option2ImgUrl = url
            }
        })
    }
    private fun editOption3Image(questionIndex: Int, uri: Uri){
        uploadImage(uri, questionIndex, "optionA", object : UploadListener{
            override fun uploaded(url: String) {
                test.questions.get(questionIndex).option3ImgUrl = url
            }
        })
    }
    private fun editOption4Image(questionIndex: Int, uri: Uri){
        uploadImage(uri, questionIndex, "optionA", object : UploadListener{
            override fun uploaded(url: String) {
                test.questions.get(questionIndex).option4ImgUrl = url
            }
        })
    }



    public interface UploadListener{
       fun uploaded(url: String);
    }

    private fun uploadImage(image: Uri, index: Int, postScript: String, listener: UploadListener){
        showProgressBar("Uploading ${postScript} image")
        FirebaseStorage.getInstance()
            .reference
            .child(test.id)
            .child("Q${index}images")
            .child("${postScript}.png")
            .putFile(image)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    FirebaseStorage.getInstance()
                        .reference
                        .child(test.id)
                        .child("Q${index}images")
                        .child("${postScript}.png")
                        .downloadUrl
                        .addOnCompleteListener {
                            hideProgressBar()
                            if (it.isSuccessful){
                                listener.uploaded(it.result.toString())
                            } else showError("while getting banner url: " + it.exception!!.message)
                        }
                } else {
                    showError("while uploading banner: "+ it.exception!!.message)
                }
            }

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

    private fun editTestBanner(banner: String?){
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null){
            newQuestionPath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(addQuestionIV)

        }

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            newOptionApath= data.data!!
            Picasso.get()
                .load(data.data)
                .into(addOptionAIV)

        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            newOptionBpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(addOptionBIV)
        }
        if (requestCode == 3 && resultCode == RESULT_OK && data != null){
            newOptionCpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(addOptionCIV)
        }
        if (requestCode == 4 && resultCode == RESULT_OK && data != null){
            newOptionDpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(addOptionDIV)
        }
        if (requestCode == 5 && resultCode == RESULT_OK && data != null){
            updatedQuestionPath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(questionIV)

        }

        if (requestCode == 6 && resultCode == RESULT_OK && data != null){
            updatedOptionApath= data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionAIV)

        }
        if (requestCode == 7 && resultCode == RESULT_OK && data != null){
           updatedOptionBpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionBIV)
        }
        if (requestCode == 8 && resultCode == RESULT_OK && data != null){
            updatedOptionCpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionCIV)
        }
        if (requestCode == 9 && resultCode == RESULT_OK && data != null){
            updatedOptionDpath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(optionDIV)
        }
        if (requestCode == 10 && resultCode == RESULT_OK && data != null){
            updatedBannerPath = data.data!!
            Picasso.get()
                .load(data.data)
                .into(testBannerETA)
               uploadBanner(data.data!!)
        }

    }

}