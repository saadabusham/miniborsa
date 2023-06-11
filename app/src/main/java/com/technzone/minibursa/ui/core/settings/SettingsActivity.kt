package com.technzone.minibursa.ui.core.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.common.CommonEnums
import com.technzone.minibursa.databinding.ActivitySettingsBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsActivity : BaseBindingActivity<ActivitySettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_settings,
            hasToolbar = true
        )
        addToolbar(
            toolbarView = binding?.toolbarLayout?.toolbar,
            tvToolbarTitleView = binding?.toolbarLayout?.tvToolbarTitle,
            hasToolbar = true,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.settings
        )
        binding?.viewModel = viewModel
        setUpListeners()
    }

    private fun setUpListeners() {
        binding?.switchNotifications?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setIsNotificationsIsEnabled(isChecked)
        }
        binding?.linearLanguage?.setOnClickListener {
            viewModel.saveLanguage().observe(this) {
                (this as BaseBindingActivity<*>)
                    .setLanguage(
                        if (viewModel.englishSelected.value!!)
                            CommonEnums.Languages.English.value
                        else CommonEnums.Languages.Arabic.value
                    )
            }
        }
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, SettingsActivity::class.java)
            context?.startActivity(intent)
        }

    }

}