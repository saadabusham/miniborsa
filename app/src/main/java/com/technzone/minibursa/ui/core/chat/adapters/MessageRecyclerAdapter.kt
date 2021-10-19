package com.technzone.minibursa.ui.core.chat.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.paginate.Paginate
import com.technzone.minibursa.R
import com.technzone.minibursa.data.enums.MessageType
import com.technzone.minibursa.databinding.RawIncomingImageBinding
import com.technzone.minibursa.databinding.RawIncomingTextBinding
import com.technzone.minibursa.databinding.RawOutcomingImageBinding
import com.technzone.minibursa.databinding.RawOutcomingTextBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder
import com.technzone.minibursa.ui.base.bindingadapters.setImageFromUrl
import com.twilio.chat.CallbackListener
import com.twilio.chat.Message

class MessageRecyclerAdapter(
    context: Context,
    val myId: String,
    var userPicture: String,
    private val paginate: Paginate.Callbacks
) : BaseBindingRecyclerViewAdapter<Message>(context) {

    override fun getItemViewType(position: Int): Int {
        return if (items[position].author != myId) {
            if (!items[position].hasMedia())
                MessageType.INCOMING_TEXT.type
            else
                MessageType.INCOMING_IMAGE.type
        } else {
            if (!items[position].hasMedia())
                MessageType.OUT_COMING_TEXT.type
            else
                MessageType.OUT_COMING_IMAGE.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MessageType.INCOMING_TEXT.type)
            return InComingTextViewHolder(
                RawIncomingTextBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        else if (viewType == MessageType.INCOMING_IMAGE.type)
            return InComingImageViewHolder(
                RawIncomingImageBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        else if (viewType == MessageType.OUT_COMING_TEXT.type)
            return OutComingTextViewHolder(
                RawOutcomingTextBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        else {
            return OutComingImageViewHolder(
                RawOutcomingImageBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is InComingTextViewHolder) {
            holder.bind(items[position])
        } else if (holder is InComingImageViewHolder) {
            holder.bind(items[position])
        } else if (holder is OutComingTextViewHolder) {
            holder.bind(items[position])
        } else if (holder is OutComingImageViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class InComingTextViewHolder(private val binding: RawIncomingTextBinding) :
        BaseViewHolder<Message>(binding.root) {

        override fun bind(item: Message) {
            binding.message = item
            loadMore(bindingAdapterPosition)
            binding.imgPicture.setImageFromUrl(
                userPicture,
                imageErrorPlaceholder = R.drawable.ic_profile_place_holder,
                imagePlaceholder = R.drawable.ic_profile_place_holder
            )
        }
    }

    inner class InComingImageViewHolder(private val binding: RawIncomingImageBinding) :
        BaseViewHolder<Message>(binding.root) {

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun bind(item: Message) {
            binding.message = item
            binding.imgPicture.setImageFromUrl(
                userPicture,
                imageErrorPlaceholder = R.drawable.ic_profile_place_holder,
                imagePlaceholder = R.drawable.ic_profile_place_holder
            )
            binding.imgMedia.shapeAppearanceModel = binding.imgMedia.shapeAppearanceModel
                .toBuilder()
                .setTopRightCorner(
                    CornerFamily.ROUNDED,
                    context.resources.getDimension(R.dimen._15sdp)
                )
                .setTopLeftCorner(
                    CornerFamily.ROUNDED,
                    context.resources.getDimension(R.dimen._15sdp)
                )
                .setBottomRightCorner(
                    CornerFamily.ROUNDED,
                    context.resources.getDimension(R.dimen._15sdp)
                )
                .setBottomLeftCorner(CornerFamily.ROUNDED, 0F)
                .build()
            item.media.getContentTemporaryUrl(object : CallbackListener<String>() {
                override fun onSuccess(p0: String?) {
                    binding.imgMedia.setImageFromUrl(
                        p0
                    )
                }
            })
            loadMore(bindingAdapterPosition)
            binding.imgMedia.setOnClickListener {
                itemClickListener?.onItemClick(
                    binding.imgMedia,
                    bindingAdapterPosition,
                    item
                )
            }
        }
    }

    inner class OutComingTextViewHolder(private val binding: RawOutcomingTextBinding) :
        BaseViewHolder<Message>(binding.root) {

        override fun bind(item: Message) {
            binding.message = item
            loadMore(bindingAdapterPosition)
        }
    }

    inner class OutComingImageViewHolder(private val binding: RawOutcomingImageBinding) :
        BaseViewHolder<Message>(binding.root) {

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun bind(item: Message) {
            binding.message = item
            binding.imgMedia.shapeAppearanceModel = binding.imgMedia.shapeAppearanceModel
                .toBuilder()
                .setTopRightCorner(
                    CornerFamily.ROUNDED,
                    context.resources.getDimension(R.dimen._15sdp)
                )
                .setTopLeftCorner(
                    CornerFamily.ROUNDED,
                    context.resources.getDimension(R.dimen._15sdp)
                )
                .setBottomLeftCorner(
                    CornerFamily.ROUNDED,
                    context.resources.getDimension(R.dimen._15sdp)
                )
                .setBottomRightCorner(CornerFamily.ROUNDED, 0F)
                .build()
            item.media.getContentTemporaryUrl(object : CallbackListener<String>() {
                override fun onSuccess(p0: String?) {
                    binding.imgMedia.setImageFromUrl(p0, R.drawable.ic_profile_place_holder)
                }
            })

            loadMore(bindingAdapterPosition)
            binding.imgMedia.setOnClickListener {
                itemClickListener?.onItemClick(
                    binding.imgMedia,
                    bindingAdapterPosition,
                    item
                )
            }
        }
    }

    private fun loadMore(position: Int) {
        if (position == 0) {
            paginate.onLoadMore()
        }
    }
}