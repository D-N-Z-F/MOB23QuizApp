package com.daryl.mob23quizapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_1
import com.daryl.mob23quizapp.core.utils.Utils.debugLog
import com.daryl.mob23quizapp.data.models.Question
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.ItemQuestionBinding
import com.daryl.mob23quizapp.databinding.ItemQuizBinding

class QuestionAdapter(
    private var questions: List<Question>,
    private val holderType: Int
): RecyclerView.Adapter<QuestionAdapter.BaseViewHolder>() {
    private var answerList = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemQuestionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return when (holderType) {
            HOLDER_TYPE_1 -> TeacherQuestionViewHolder(binding)
            else -> StudentQuestionViewHolder(binding)
        }
    }
    override fun getItemCount(): Int = questions.size
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.bind(questions[position], position)
    inner class TeacherQuestionViewHolder(
        private val binding: ItemQuestionBinding
    ): BaseViewHolder(binding) {
        override fun bind(question: Question, position: Int) {
            super.bind(question, position)
            binding.run {
                setOf(rb1, rb2, rb3, rb4).forEachIndexed { index, view ->
                    view.isEnabled = false
                    view.text = question.options[index]
                }
                tvAnswer.text = question.answer
            }
        }
    }
    inner class StudentQuestionViewHolder(
        private val binding: ItemQuestionBinding
    ): BaseViewHolder(binding) {
        override fun bind(question: Question, position: Int) {
            super.bind(question, position)
            binding.run {
                tvAnswer.isInvisible = true
                val buttons = setOf(rb1, rb2, rb3, rb4)
                buttons.forEachIndexed { index, button ->
                    button.apply {
                        text = question.options[index]
                        isChecked = answerList[position] == question.options[index]
                        setOnClickListener { view ->
                            buttons.map { if(it.id != view.id) it.isChecked = false }
                            answerList[position] = (view as RadioButton).text.toString()
                        }
                    }
                }
            }
        }
    }
    abstract inner class BaseViewHolder(
        private val binding: ItemQuestionBinding
    ): ViewHolder(binding.root) {
        open fun bind(question: Question, position: Int) { binding.tvTitle.text = question.title }
    }
    fun setQuestions(questions: List<Question>) {
        this.questions = questions
        answerList = MutableList(questions.size) { "" }
        notifyDataSetChanged()
    }
    fun getAnswerList(): List<String> = answerList.toList()
}