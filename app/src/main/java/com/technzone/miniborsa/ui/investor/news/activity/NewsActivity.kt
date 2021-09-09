package com.technzone.miniborsa.ui.investor.news.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.databinding.ActivityNewsBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.auth_nav_host_fragment
import kotlinx.android.synthetic.main.activity_news.*

@AndroidEntryPoint
class NewsActivity : BaseBindingActivity<ActivityNewsBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news, hasToolbar = false)
        setStartDestination()
    }

    private fun setStartDestination() {

        val navHostFragment = news_nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.news_nav_graph)

        if (intent.getIntExtra(Constants.BundleData.NEWS_ID,-1) == -1) {
            graph.startDestination = R.id.newsFragment
        } else {
            graph.startDestination = R.id.newsDetailsFragment
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