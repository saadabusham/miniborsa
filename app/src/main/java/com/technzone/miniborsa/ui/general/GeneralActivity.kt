package com.technzone.miniborsa.ui.general

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.ActivityChooseGeneralBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.general.adapters.ChooseGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class GeneralActivity : BaseBindingActivity<ActivityChooseGeneralBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    lateinit var selectedSymptomsAdapter: SelectedGeneralRecyclerAdapter

    lateinit var adapter: ChooseGeneralRecyclerAdapter
    private var list = ArrayList<GeneralLookup>()
    private var selectedList = ArrayList<GeneralLookup>()
    private var searchList = ArrayList<GeneralLookup>()
    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_general, hasToolbar = false)
        setAdapter()
//        viewModel.getSymptoms(serviceId = args.appointmentsDetails.service?.id ?: 0)
//        observe()
        initSearch()
    }

    private fun setAdapter() {
        adapter = ChooseGeneralRecyclerAdapter(this)
        binding?.rvItems?.adapter = adapter
        selectedSymptomsAdapter = SelectedGeneralRecyclerAdapter(this)
        binding?.rvSelectedItems?.adapter = selectedSymptomsAdapter
//        selectedSymptomsAdapter.submitItems(selectedSymptomsList)
        binding?.rvItems?.setOnItemClickListener(this)
        binding?.rvSelectedItems?.setOnItemClickListener(this)

        onSelectedChanged()
    }

    private fun initSearch() {
        if (binding?.etSearch?.text?.isEmpty() == true) {
            adapter.submitItems(list)
        }

        disposable + binding?.etSearch?.textChangeEvents()
                ?.debounce(300, TimeUnit.MILLISECONDS)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe {
                    if (it.text.isNotEmpty()) {
                        searchList.clear()
                        list.forEach { generalLookUp ->
                            if (generalLookUp.name?.contains(it.text) == true) {
                                searchList.add(generalLookUp)
                            }
                        }
                        if (searchList.size > 0) {
                            adapter.submitItems(searchList)
                        }
                    } else {
                        adapter.submitItems(list)
                    }
                }
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, GeneralActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }

    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as GeneralLookup
        if (selectedList.contains(item)) {
            selectedList.remove(item)
            selectedSymptomsAdapter.submitItems(selectedList)
            adapter.unCheckItem(item)
            onSelectedChanged()
        }
    }

    override fun onItemChecked(isChecked: Boolean?, item: Any, position: Int) {
        item as GeneralLookup
        if (isChecked == true && !selectedList.contains(item)) {
            selectedList.add(item)
            selectedSymptomsAdapter.submitItems(selectedList)
            onSelectedChanged()
        } else if (isChecked == false && selectedList.contains(item)) {
            selectedList.remove(item)
            selectedSymptomsAdapter.submitItems(selectedList)
            onSelectedChanged()
        }
    }

    private fun onSelectedChanged() {

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}