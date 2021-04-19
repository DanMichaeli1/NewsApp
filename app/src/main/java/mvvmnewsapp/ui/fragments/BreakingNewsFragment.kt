package mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.flow.collect
import mvvmnewsapp.adapters.NewsAdapter
import mvvmnewsapp.ui.NewsActivity
import mvvmnewsapp.ui.NewsViewModel
import mvvmnewsapp.utils.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapater: NewsAdapter

    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        // Handling NewsResponse using State Flow
        lifecycleScope.launchWhenCreated {
            viewModel.breakingNewsUiState.collect {
                when (it) {
                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { newsResponse ->
                            newsAdapater.differ.submitList(newsResponse.articles)
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        it.message?.let { message ->
                            Log.e(TAG, "An error occurred: $message")
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        }

        // Handling NewsResponse using Observer
//        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
//            when(response) {
//                is Resource.Success -> {
//                    hideProgressBar()
//                    response.data?.let { newsResponse ->
//                        newsAdapater.differ.submitList(newsResponse.articles)
//                    }
//                }
//                is Resource.Error -> {
//                    hideProgressBar()
//                    response.message?.let { message ->
//                        Log.e(TAG, "An error occurred: $message")
//                    }
//                }
//                is Resource.Loading -> {
//                    showProgressBar()
//                }
//            }
//        })
    }


        private fun hideProgressBar() {
            paginationProgressBar.visibility = View.INVISIBLE
        }

        private fun showProgressBar() {
            paginationProgressBar.visibility = View.VISIBLE
        }

        private fun setupRecyclerView() {
            newsAdapater = NewsAdapter()
            rvBreakingNews.apply {
                adapter = newsAdapater
                layoutManager = LinearLayoutManager(activity)
            }
        }
}
