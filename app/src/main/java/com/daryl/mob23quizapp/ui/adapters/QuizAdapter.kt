package com.daryl.mob23quizapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_1
import com.daryl.mob23quizapp.core.services.AuthService
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.databinding.ItemQuizBinding

class QuizAdapter(
    private var quizzes: List<Quiz>,
    private val holderType: Int,
    private val resourceProvider: ResourceProvider?
): RecyclerView.Adapter<QuizAdapter.BaseViewHolder>() {
    private val authService = AuthService()
    var listener: QuizListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemQuizBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return when(holderType) {
            HOLDER_TYPE_1 -> TeacherQuizViewHolder(binding)
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
            super.bind(quiz)
            binding.run {
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
            super.bind(quiz)
            binding.run {
                tvID.visibility = View.GONE
                llActions.visibility = View.GONE
                val participant = quiz.participants.find {
                    it.studentEmail == authService.getCurrentUser()?.email
                }
                participant?.let {
                    tvTimeTaken.text = it.timeTaken.toString()
                    mbScore.text = resourceProvider?.getString(
                        R.string.score, it.score, quiz.questions.size
                    )
                    llDetails.visibility = View.VISIBLE
                }
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