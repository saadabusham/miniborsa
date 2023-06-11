package com.technzone.minibursa.ui.investor.news.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.technzone.minibursa.R
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.databinding.ActivityNewsBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.investor.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : BaseBindingActivity<ActivityNewsBinding>() {

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news, hasToolbar = false)
        setStartDestination()
    }

    private fun setStartDestination() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.news_nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.news_nav_graph)
        intent.getIntExtra(Constants.BundleData.NEWS_ID, -1).let {
            if (it == -1) {
                graph.setStartDestination(R.id.newsFragment)
            } else {
                viewModel.blogId = it
                graph.setStartDestination(R.id.newsDetailsFragment)
            }
        }

        navHostFragment.navController.graph = graph
    }

    companion object {

        fun start(context: Context, newId: Int? = -1) {
            val intent = Intent(context, NewsActivity::class.java).apply {
                putExtra(Constants.BundleData.NEWS_ID, newId)
            }
            context.startActivity(intent)
        }

    }

}