package com.misohe.misohe.blogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.misohe.misohe.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_blogs_detail.*

private const val ARG_PARAM1 = "param1"

class BlogsDetailFragment : Fragment() {
    private var blogId: Int? = null
    private val blogsViewModel: BlogsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            blogId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blogs_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(blogId: Int) =
            BlogsDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, blogId)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //blogsViewModel.getDetailedBlog(blogId ?: 0)
        activity?.bottomNavGroup?.visibility = View.GONE
        activity?.arcSeparator?.visibility = View.GONE
        activity?.arc?.visibility = View.GONE

        backIcon.setOnClickListener { activity?.onBackPressed() }

        blogsViewModel.detailedBlogLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is BlogDetailedFetchState.Success -> {
                    titleTextView.text = state.blogsModel.title
                    articleTitle.text = state.blogsModel.title
                    Glide.with(requireContext()).load(state.blogsModel.image).into(imageView)
                    blogDesc.text = state.blogsModel.content
                }
                is BlogDetailedFetchState.Error -> {

                }
                is BlogDetailedFetchState.Loading -> {

                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomNavGroup?.visibility = View.VISIBLE
        activity?.arcSeparator?.visibility = View.VISIBLE
        activity?.arc?.visibility = View.VISIBLE
    }
}