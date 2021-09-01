package com.technzone.miniborsa.ui.base.bindingadapters

import androidx.databinding.BindingAdapter
import com.rilixtech.widget.countrycodepicker.CountryCodePicker

@BindingAdapter("ccp_selected_country_code_iso2")
fun CountryCodePicker?.setSelectedCountryCodeUsingISO2Name(iso2Name:String?){
    if(iso2Name.isNullOrEmpty()) return
    this?.setCountryForNameCode(iso2Name)
}