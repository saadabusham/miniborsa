package com.technzone.miniborsa.ui.base.bindingadapters

import androidx.annotation.ArrayRes
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@BindingAdapter(value = ["tlViewPager2Id", "tlTitlesArrayRes"], requireAll = true)
fun TabLayout?.setUpTabLayoutWithViewPager2(@IdRes viewPager2Id: Int, @ArrayRes titlesArray: Int) {
    val viewPager2: ViewPager2 = this?.rootView?.findViewById(viewPager2Id) ?: return
    val tabsTitleArray = this.resources.getStringArray(titlesArray)

    TabLayoutMediator(
        this,
        viewPager2
    ) { tab, position ->
        tab.text = tabsTitleArray[position]
    }.attach()

}