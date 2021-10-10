package com.technzone.minibursa.ui.userrole.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.data.models.auth.login.UserRoles
import com.technzone.minibursa.databinding.RowUserRoleBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder

class UserRolesRecyclerAdapter constructor(
        context: Context
) : BaseBindingRecyclerViewAdapter<UserRoles>(context) {

    fun getSelectedItem(): UserRoles? {
        return items.singleOrNull { it.selected.value == true }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                RowUserRoleBinding.inflate(
                        LayoutInflater.from(context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowUserRoleBinding) :
            BaseViewHolder<UserRoles>(binding.root) {

        override fun bind(item: UserRoles) {
            binding.item = item
            binding.lifecycleOwner = context as AppCompatActivity
            binding.root.setOnClickListener {
                items.singleOrNull { it.selected.value == true }?.let {
                    it.selected.value = false
                }
                item.selected.postValue(true)
            }
        }
    }
}