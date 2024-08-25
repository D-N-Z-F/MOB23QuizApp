package com.daryl.mob23quizapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daryl.mob23quizapp.core.utils.Utils.debugLog
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.ItemQuizBinding

class QuizAdapter(
    private var quizzes: List<Quiz>,
    private val holderType: Int
): RecyclerView.Adapter<QuizAdapter.BaseViewHolder>() {
    var listener: QuizListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemQuizBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return when(holderType) {
            1 -> TeacherQuizViewHolder(binding)
            else -> StudentQuizViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = quizzes.size
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.bind(quizzes[position])
    inner class TeacherQuizViewHolder(
        private val binding: ItemQuizBinding
    ): BaseViewHolder(binding) {
        override fun bind(quiz: Quiz) {
            binding.run {
                super.bind(quiz)
                tvID.text = quiz.id
                ivCopy.setOnClickListener { listener?.onClickCopy(quiz.id!!) }
                ivDelete.setOnClickListener { listener?.onClickDelete(quiz.id!!) }
                llQuiz.setOnClickListener { listener?.onClickItem(quiz.id!!) }
            }
        }
    }
    inner class StudentQuizViewHolder(
        private val binding: ItemQuizBinding
    ): BaseViewHolder(binding) {
        override fun bind(quiz: Quiz) {
            binding.run {
                super.bind(quiz)
                setOf(tvID, ivCopy, ivDelete).forEach { it.isInvisible = true }
            }
        }
    }
    abstract inner class BaseViewHolder(
        private val binding: ItemQuizBinding
    ): ViewHolder(binding.root) {
        open fun bind(quiz: Quiz) {
            binding.run {
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
    interface QuizListener {
        fun onClickCopy(id: String)
        fun onClickDelete(id: String)
        fun onClickItem(id: String)
    }
}