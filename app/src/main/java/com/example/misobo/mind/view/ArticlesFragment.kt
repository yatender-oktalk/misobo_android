package com.example.misobo.mind.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.blogs.BlogDetailedFetchState
import com.example.misobo.blogs.BlogsDetailFragment
import com.example.misobo.blogs.BlogsFetchState
import com.example.misobo.blogs.BlogsViewModel
import com.example.misobo.mind.items.DetailedArticlesRecyclerItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_articles.*

class ArticlesFragment : Fragment() {

    private val blogsViewModel: BlogsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.bottomNavigationView?.visibility = View.GONE
        activity?.arcSeparator?.visibility = View.GONE
        activity?.arc?.visibility = View.GONE

        val adapter = GroupAdapter<ViewHolder>()
        articlesRecyclerView.adapter = adapter
        blogsViewModel.fetchBlogs()

        backIcon.setOnClickListener {
            activity?.onBackPressed()
        }

        blogsViewModel.blogLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is BlogsFetchState.Success -> {
                    adapter.clear()
                    val blogsSection = Section()
                    state.blogsModel.data?.forEachIndexed { index, model ->
                        blogsSection.add(DetailedArticlesRecyclerItem(model) {
                            blogsViewModel.detailedBlogLiveData.postValue(
                                BlogDetailedFetchState.Success(
                                    state.blogsModel.data.get(it)
                                ))
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.mainContainer, BlogsDetailFragment.newInstance(it))
                                ?.addToBackStack(null)?.commit()
                        })
                    }
                    adapter.add(blogsSection)
                }
                is BlogsFetchState.Loading -> {
                }
                is BlogsFetchState.Error -> {

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomNavigationView?.visibility = View.VISIBLE
        activity?.arcSeparator?.visibility = View.VISIBLE
        activity?.arc?.visibility = View.VISIBLE
    }
}