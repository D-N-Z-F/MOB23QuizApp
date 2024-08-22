package com.daryl.mob23quizapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daryl.mob23quizapp.data.models.Question
import com.daryl.mob23quizapp.databinding.ItemQuestionBinding

class QuestionAdapter(
    private var questions: List<Question>
): RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder =
        QuestionViewHolder(
            ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    override fun getItemCount(): Int = questions.size
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) =
        holder.bind(questions[position])
    inner class QuestionViewHolder(
        private val binding: ItemQuestionBinding
    ): ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.run {
                tvTitle.text = question.title
                setOf(rb1, rb2, rb3, rb4).forEachIndexed { index, view ->
                    view.isEnabled = false
                    view.text = question.options[index]
                }
                tvAnswer.text = question.answer
            }
        }
    }
    fun setQuestions(questions: List<Question>) {
        this.questions = questions
        notifyDataSetChanged()
    }
}