package com.technzone.miniborsa.ui.investor.filter.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.activityViewModels
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.ActivityChooseGeneralBinding
import com.technzone.miniborsa.databinding.ActivityFilterBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.general.adapters.ChooseGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.investor.filter.filter.adapters.GeneralLookupRecyclerAdapter
import com.technzone.miniborsa.ui.investor.invistormain.viewmodels.InvestorMainViewModel
import com.technzone.miniborsa.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FilterActivity : BaseBindingActivity<ActivityFilterBinding>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter, hasToolbar = false)
        postponeEnterTransition()
    }

    companion object {

        fun start(context: Activity, view : View) {
            val p1: androidx.core.util.Pair<View, String> = androidx.core.util.Pair(
                view,
                view.transitionName
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context,
                p1
            )
            val intent = Intent(context, FilterActivity::class.java)
            context.startActivity(intent, options.toBundle())
        }

    }

}