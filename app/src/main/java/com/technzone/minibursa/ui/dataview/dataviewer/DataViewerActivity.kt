package com.technzone.minibursa.ui.dataview.dataviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.technzone.minibursa.R
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.databinding.ActivityDataviewerBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataViewerActivity : BaseBindingActivity<ActivityDataviewerBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_dataviewer,
            hasToolbar = true
        )
        addToolbar(
            toolbarView = binding?.toolbarLayout?.toolbar,
            tvToolbarTitleView = binding?.toolbarLayout?.tvToolbarTitle,
            hasToolbar = true,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            titleString = intent.getStringExtra(Constants.title)
        )
        binding?.body = intent.getStringExtra(Constants.body)
    }

    companion object {

        fun start(context: Context?,
                  title: String,
                  body: String) {
            val intent = Intent(context, DataViewerActivity::class.java)
            intent.putExtra(Constants.title, title)
            intent.putExtra(Constants.body, body)
            context?.startActivity(intent)
        }

    }

}