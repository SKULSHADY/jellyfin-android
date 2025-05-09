package org.jellyfin.mobile.downloads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.jellyfin.mobile.R
import org.jellyfin.mobile.data.entity.DownloadEntity
import org.jellyfin.mobile.databinding.DownloadItemBinding
import org.jellyfin.sdk.model.api.BaseItemDto

class DownloadsAdapter(
    private val onItemClick: (DownloadEntity) -> Unit,
    private val onItemHold: (DownloadEntity) -> Unit,
) : ListAdapter<DownloadEntity, DownloadsAdapter.DownloadViewHolder>(
    DownloadDiffCallback(),
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val binding = DownloadItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DownloadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.updateBackground(position)
    }

    inner class DownloadViewHolder(private val binding: DownloadItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(downloadEntity: DownloadEntity) {
            if (downloadEntity.thumbnail == null) {
                onItemHold(downloadEntity)
                return
            }

            val context = itemView.context

            val mediaItem: BaseItemDto? = downloadEntity.mediaSource.item
            if (mediaItem?.seriesName != null) {

            }
            binding.textViewName.text = when {
                mediaItem?.seriesName != null -> context.getString(
                    R.string.tv_show_title,
                    mediaItem.seriesName,
                )
                else -> downloadEntity.mediaSource.name
            }
            binding.textViewDescription.text = when {
                mediaItem?.seriesName != null -> context.getString(
                    R.string.tv_show_desc,
                    downloadEntity.mediaSource.name,
                    mediaItem.parentIndexNumber,
                    mediaItem.indexNumber,
                )
                mediaItem?.productionYear != null -> mediaItem.productionYear.toString()
                else -> downloadEntity.mediaSource.id
            }
            binding.textViewFileSize.text = downloadEntity.fileSize

            itemView.setOnClickListener {
                onItemClick(downloadEntity)
            }
            itemView.setOnLongClickListener {
                onItemHold(downloadEntity)
                true
            }

            binding.imageViewThumbnail.setImageBitmap(downloadEntity.thumbnail)
        }

        fun updateBackground(position: Int) {
            val drawable = if (itemCount == 1) {
                R.drawable.rounded_bg_full
            } else if (position == 0) {
                R.drawable.rounded_bg_top
            } else if (position == itemCount - 1) {
                R.drawable.rounded_bg_bottom
            } else {
                R.drawable.rounded_bg
            }
            binding.root.background = AppCompatResources.getDrawable(itemView.context, drawable)
        }
    }
}
