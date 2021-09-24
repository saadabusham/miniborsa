package com.technzone.miniborsa.ui.general

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.ActivityChooseGeneralBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.general.adapters.ChooseGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.utils.extensions.showErrorAlert
import com.technzone.miniborsa.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class GeneralActivity : BaseBindingActivity<ActivityChooseGeneralBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    lateinit var selectedGeneralRecyclerAdapter: SelectedGeneralRecyclerAdapter

    lateinit var adapter: ChooseGeneralRecyclerAdapter
    private var list = ArrayList<GeneralLookup>()
    private var selectedList = ArrayList<GeneralLookup>()
    private var searchList = ArrayList<GeneralLookup>()
    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_general, hasToolbar = false)
        setAdapter()
        initSearch()
        setUpListeners()
    }

    private fun setUpListeners() {
        binding?.btnConfirm?.setOnClickListener {
            selectedGeneralRecyclerAdapter.getSelectedItems().let {
                if (it.size > 0) {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(Constants.BundleData.GENERAL_LIST, it)
                    })
                    finish()
                } else {
                    showErrorAlert(getString(R.string.app_name), getString(R.string.please_select_item))
                }
            }
        }
    }

    private fun setAdapter() {
        adapter = ChooseGeneralRecyclerAdapter(this)
        binding?.rvItems?.adapter = adapter
        selectedGeneralRecyclerAdapter = SelectedGeneralRecyclerAdapter(this,true)
        binding?.rvSelectedItems?.adapter = selectedGeneralRecyclerAdapter
        adapter.submitNewItems((intent.getSerializableExtra(Constants.BundleData.GENERAL_LIST) as ArrayList<GeneralLookup>).also {
            list = it
        })
        binding?.rvItems?.setOnItemClickListener(this)
        binding?.rvSelectedItems?.setOnItemClickListener(this)

        onSelectedChanged()
    }

    private fun initSearch() {
        if (binding?.etSearch?.text?.isEmpty() == true) {
            adapter.submitNewItems(list)
        }

        disposable + binding?.etSearch?.textChangeEvents()
            ?.skipInitialValue()
            ?.debounce(300, TimeUnit.MILLISECONDS)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                if (it.text.isNotEmpty()) {
                    searchList.clear()
                    list.forEach { generalLookUp ->
                        if (generalLookUp.name?.toUpperCase()?.contains(it.text.toString().toUpperCase()) == true) {
                            searchList.add(generalLookUp)
                        }
                    }
                    if (searchList.size > 0) {
                        adapter.submitNewItems(searchList)
                    }
                } else {
                    adapter.submitNewItems(list)
                }
            }
    }


    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as GeneralLookup
        if (selectedList.contains(item)) {
            selectedList.remove(item)
            selectedGeneralRecyclerAdapter.submitNewItems(selectedList)
            adapter.unCheckItem(item)
            onSelectedChanged()
        }
    }

    override fun onItemChecked(isChecked: Boolean?, item: Any, position: Int) {
        item as GeneralLookup
        if (isChecked == true && !selectedList.contains(item)) {
            selectedList.add(item)
            selectedGeneralRecyclerAdapter.submitNewItems(selectedList)
            onSelectedChanged()
        } else if (isChecked == false && selectedList.contains(item)) {
            selectedList.remove(item)
            selectedGeneralRecyclerAdapter.submitNewItems(selectedList)
            onSelectedChanged()
        }
    }

    private fun onSelectedChanged() {

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    companion object {
        fun start(
            context: Context?,
            list: ArrayList<GeneralLookup>,
            resultLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, GeneralActivity::class.java).apply {
                putExtra(Constants.BundleData.GENERAL_LIST, list)
            }
            resultLauncher.launch(intent)
        }
    }
}