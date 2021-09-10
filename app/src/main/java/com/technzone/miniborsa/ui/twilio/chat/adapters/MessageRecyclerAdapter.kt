package com.technzone.miniborsa.ui.twilio.chat.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.paginate.Paginate
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.MessageType
import com.technzone.miniborsa.databinding.RawIncomingImageBinding
import com.technzone.miniborsa.databinding.RawIncomingTextBinding
import com.technzone.miniborsa.databinding.RawOutcomingImageBinding
import com.technzone.miniborsa.databinding.RawOutcomingTextBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder
import com.technzone.miniborsa.utils.extensions.loadImage
import com.twilio.chat.CallbackListener
import com.twilio.chat.Message

class MessageRecyclerAdapter(
    context: Context,
    val myId: String,
    val userPicture: String,
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
            binding?.message = item
            loadMore(adapterPosition)
            binding?.imgPicture.loadImage(userPicture)
        }
    }

    inner class InComingImageViewHolder(private val binding: RawIncomingImageBinding) :
        BaseViewHolder<Message>(binding.root) {

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun bind(item: Message) {
            binding?.message = item
            binding?.imgPicture.loadImage(userPicture)
            binding?.imgMedia.shapeAppearanceModel = binding?.imgMedia.shapeAppearanceModel
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
                    binding?.imgMedia?.loadImage(p0)
                }
            })
            loadMore(adapterPosition)
            binding?.imgMedia?.setOnClickListener {
                itemClickListener?.onItemClick(
                    binding?.imgMedia,
                    adapterPosition,
                    item
                )
            }
        }
    }

    inner class OutComingTextViewHolder(private val binding: RawOutcomingTextBinding) :
        BaseViewHolder<Message>(binding.root) {

        override fun bind(item: Message) {
            binding?.message = item
            loadMore(adapterPosition)
        }
    }

    inner class OutComingImageViewHolder(private val binding: RawOutcomingImageBinding) :
        BaseViewHolder<Message>(binding.root) {

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun bind(item: Message) {
            binding?.message = item
            binding?.imgMedia.shapeAppearanceModel = binding?.imgMedia.shapeAppearanceModel
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
                    binding?.imgMedia?.loadImage(p0)
                }
            })

            loadMore(adapterPosition)
            binding?.imgMedia?.setOnClickListener {
                itemClickListener?.onItemClick(
                    binding?.imgMedia,
                    adapterPosition,
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