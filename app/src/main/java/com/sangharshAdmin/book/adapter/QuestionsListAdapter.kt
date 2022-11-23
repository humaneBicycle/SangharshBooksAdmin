package com.sangharshAdmin.book.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sangharshAdmin.book.R

class QuestionsListAdapter(private val dataSet: ArrayList<String>, private val listner: Listener
) :
    RecyclerView.Adapter<QuestionsListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionNo: TextView
        val questionTitle: TextView
        val questionLL: LinearLayout


        init {
            // Define click listener for the ViewHolder's View.
            questionTitle = view.findViewById(R.id.questionTitle)
            questionNo = view.findViewById(R.id.questionNo)
            questionLL = view.findViewById(R.id.questionLL)
        }
    }

    public interface Listener{
        fun questionClicked(qIndex: Int)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.question_list_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.questionTitle.text = dataSet[position]
        viewHolder.questionNo.text =(position+1).toString()
        viewHolder.questionLL.setOnClickListener(View.OnClickListener {
            listner.questionClicked(position)
        })
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
