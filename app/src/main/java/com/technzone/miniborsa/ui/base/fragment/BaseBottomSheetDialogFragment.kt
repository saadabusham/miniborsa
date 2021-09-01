package com.technzone.miniborsa.ui.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    var mView: View? = null

    protected abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mView == null) {
            mView = inflater.inflate(layoutId, container, false)
        }
        return mView
    }
}