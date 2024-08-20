package com.daryl.mob23quizapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.ItemQuizBinding

class QuizAdapter(
    private var quizzes: List<Quiz>
): RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder =
        QuizViewHolder(
            ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    override fun getItemCount(): Int = quizzes.size
    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) =
        holder.bind(quizzes[position])

    inner class QuizViewHolder(private val binding: ItemQuizBinding): ViewHolder(binding.root) {
        fun bind(quiz: Quiz) {
            binding.run {
                tvID.text = quiz.id.toString()
                tvName.text = quiz.name
                tvCategory.text = quiz.category
                tvNoOfQuestions.text = quiz.questions.size.toString()
                tvTimeLimit.text = quiz.timePerQuestion.toString()
            }
        }
    }
    fun setQuizzes(quizzes: List<Quiz>) {
        this.quizzes = quizzes
        notifyDataSetChanged()
    }
}