package com.technzone.miniborsa.ui.base.sheet.lookupselector

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.BottomSheetLookupSelectorBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.sheet.lookupselector.adapter.LookUpSelectorRecyclerAdapter
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.setupClearButtonWithAction
import com.technzone.miniborsa.utils.extensions.showErrorAlert
import com.technzone.miniborsa.utils.extensions.visible
import com.technzone.miniborsa.utils.recycleviewutils.VerticalSpaceDecoration
import com.technzone.miniborsa.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class LookupSelectorBottomSheet(
    private val callBack: LookupSelectorCallBack,
    private val list: List<GeneralLookup>,
    private val fullScreen: Boolean = false,
) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetLookupSelectorBinding
    lateinit var adapter: LookUpSelectorRecyclerAdapter
    private val originalList: MutableList<GeneralLookup> = mutableListOf()

    var compositeDisposable: CompositeDisposable? = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onStart() {
        super.onStart()
        if (fullScreen)
            view?.post {
                val parent = requireView().parent as View
                val params = (parent).layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior
                val bottomSheetBehavior = behavior as BottomSheetBehavior
                setupFullHeight(parent)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
    }
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_lookup_selector, null, false)
        binding.viewModel = this
        isCancelable = true
        setUpData()
        setUpListeners()
        originalList.addAll(list)
        setUpRecyclerView()
        initSearch()
        return binding.root
    }

    private fun setUpData() {
        if (fullScreen) {
            binding.layoutToolbar.appbar.visible()
            binding.edSearch.visible()
            binding.btnDone.gone()
            binding.tvHeader.gone()
        } else {
            binding.layoutToolbar.appbar.gone()
            binding.edSearch.gone()
            binding.tvHeader.visible()
        }
    }

    private fun setUpListeners() {
        binding.btnDone.setOnClickListener {
            onDoneClicked()
        }
        binding.layoutToolbar.toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    fun onDoneClicked() {
        if (adapter.getSelectedItem() == null) {
            requireActivity().showErrorAlert(
                getString(R.string.app_name)
                    ?: "", getString(R.string.please_select_item)
            )
            return
        }
        adapter.getSelectedItem()?.let {
            callBack.callBack(it)
            dialog?.cancel()
        }
    }

    private fun setUpRecyclerView() {
        adapter = LookUpSelectorRecyclerAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setOnItemClickListener(object : BaseBindingRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, item: Any) {
                onDoneClicked()
            }
        })
        binding.recyclerView.addItemDecoration(
            VerticalSpaceDecoration(
                resources.getDimension(R.dimen._5sdp).toInt(),
                0
            )
        )
        adapter.submitItems(list)
        adapter.selectedPosition = list.withIndex().singleOrNull { it.value.selected }?.index ?: -1
    }

    private fun initSearch() {
        binding.edSearch.setupClearButtonWithAction()
        compositeDisposable + binding.edSearch.textChangeEvents()
            .skipInitialValue()
            .debounce(400, TimeUnit.MILLISECONDS)
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                if (it.text.isNotEmpty()) {
                    applyFilter(it.text.toString())
                } else {
                    applyFilter("")
                }
            }
    }

    private fun applyFilter(text: String) {
        adapter.clear()
        if (text.isNullOrEmpty()) {
            adapter.submitItems(originalList)
        } else {
            adapter.submitItems(originalList.filter {
                (it.name?.toLowerCase(Locale.ROOT)?.contains(
                    text.toLowerCase(Locale.ROOT)
                ) == true)
            })
        }
    }

    fun onCancelClicked() {
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState) as BottomSheetDialog
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    interface LookupSelectorCallBack {
        fun callBack(selectedItem: GeneralLookup)
    }
}