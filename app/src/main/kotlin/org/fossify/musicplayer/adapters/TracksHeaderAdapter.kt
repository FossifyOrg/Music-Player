package org.fossify.musicplayer.adapters

import android.annotation.SuppressLint
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller
import org.fossify.commons.dialogs.ConfirmationDialog
import org.fossify.commons.extensions.beGone
import org.fossify.commons.extensions.beVisible
import org.fossify.commons.extensions.getFormattedDuration
import org.fossify.commons.extensions.setupViewBackground
import org.fossify.commons.helpers.ensureBackgroundThread
import org.fossify.commons.views.MyRecyclerView
import org.fossify.musicplayer.R
import org.fossify.musicplayer.activities.SimpleActivity
import org.fossify.musicplayer.databinding.ItemAlbumHeaderBinding
import org.fossify.musicplayer.databinding.ItemTrackBinding
import org.fossify.musicplayer.dialogs.EditDialog
import org.fossify.musicplayer.extensions.audioHelper
import org.fossify.musicplayer.extensions.config
import org.fossify.musicplayer.extensions.getAlbumCoverArt
import org.fossify.musicplayer.models.AlbumHeader
import org.fossify.musicplayer.models.ListItem
import org.fossify.musicplayer.models.Track

class TracksHeaderAdapter(activity: SimpleActivity, items: ArrayList<ListItem>, recyclerView: MyRecyclerView, itemClick: (Any) -> Unit) :
    BaseMusicAdapter<ListItem>(items, activity, recyclerView, itemClick), RecyclerViewFastScroller.OnPopupTextUpdate {

    private val ITEM_HEADER = 0
    private val ITEM_TRACK = 1

    override val cornerRadius = resources.getDimension(org.fossify.commons.R.dimen.rounded_corner_radius_big).toInt()

    override fun getActionMenuId() = R.menu.cab_tracks_header

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            ITEM_HEADER -> ItemAlbumHeaderBinding.inflate(layoutInflater, parent, false)
            else -> ItemTrackBinding.inflate(layoutInflater, parent, false)
        }

        return createViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position) ?: return
        val allowClicks = item !is AlbumHeader
        holder.bindView(item, allowClicks, allowClicks) { itemView, _ ->
            when (item) {
                is AlbumHeader -> setupHeader(itemView, item)
                else -> setupTrack(itemView, item as Track)
            }
        }
        bindViewHolder(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is AlbumHeader -> ITEM_HEADER
            else -> ITEM_TRACK
        }
    }

    override fun prepareActionMode(menu: Menu) {
        menu.apply {
            findItem(R.id.cab_rename).isVisible = shouldShowRename()
            findItem(R.id.cab_play_next).isVisible = shouldShowPlayNext()
        }
    }

    override fun actionItemPressed(id: Int) {
        if (selectedKeys.isEmpty()) {
            return
        }

        when (id) {
            R.id.cab_add_to_playlist -> addToPlaylist()
            R.id.cab_add_to_queue -> addToQueue()
            R.id.cab_properties -> showProperties()
            R.id.cab_delete -> askConfirmDelete()
            R.id.cab_share -> shareFiles()
            R.id.cab_rename -> displayEditDialog()
            R.id.cab_select_all -> selectAll()
            R.id.cab_play_next -> playNextInQueue()
        }
    }

    override fun getSelectableItemCount() = items.size - 1

    override fun getIsItemSelectable(position: Int) = position != 0

    private fun askConfirmDelete() {
        ConfirmationDialog(context) {
            ensureBackgroundThread {
                val positions = ArrayList<Int>()
                val selectedTracks = getSelectedTracks()
                selectedTracks.forEach { track ->
                    val position = items.indexOfFirst { it is Track && it.mediaStoreId == track.mediaStoreId }
                    if (position != -1) {
                        positions.add(position)
                    }
                }

                context.deleteTracks(selectedTracks) {
                    context.runOnUiThread {
                        positions.sortDescending()
                        removeSelectedItems(positions)
                        positions.forEach {
                            items.removeAt(it)
                        }

                        // finish activity if all tracks are deleted
                        if (items.none { it is Track }) {
                            context.finish()
                        }
                    }
                }
            }
        }
    }

    private fun setupTrack(view: View, track: Track) {
        ItemTrackBinding.bind(view).apply {
            root.setupViewBackground(context)
            trackFrame.isSelected = selectedKeys.contains(track.hashCode())
            trackTitle.text = track.title
            trackInfo.beGone()

            arrayOf(trackId, trackTitle, trackDuration).forEach {
                it.setTextColor(textColor)
            }

            trackDuration.text = track.duration.getFormattedDuration()
            if (track.discNumber != null) {
                trackId.text = context.getString(R.string.track_on_disk, track.discNumber, track.trackId.toString().padStart(2, '0'))
            } else {
                trackId.text = track.trackId.toString()
            }
            trackImage.beGone()
            trackId.beVisible()
        }
    }

    private fun setupHeader(view: View, header: AlbumHeader) {
        ItemAlbumHeaderBinding.bind(view).apply {
            albumTitle.text = header.title
            albumArtist.text = header.artist

            val tracks = resources.getQuantityString(R.plurals.tracks_plural, header.trackCnt, header.trackCnt)
            var year = ""
            if (header.year != 0) {
                year = "${header.year} • "
            }

            @SuppressLint("SetTextI18n")
            albumMeta.text = "$year$tracks • ${header.duration.getFormattedDuration(true)}"

            arrayOf(albumTitle, albumArtist, albumMeta).forEach {
                it.setTextColor(textColor)
            }

            ensureBackgroundThread {
                val album = context.audioHelper.getAlbum(header.id)
                if (album != null) {
                    context.getAlbumCoverArt(album) { coverArt ->
                        loadImage(albumImage, coverArt, placeholderBig)
                    }
                } else {
                    context.runOnUiThread {
                        albumImage.setImageDrawable(placeholderBig)
                    }
                }
            }
        }
    }

    override fun onChange(position: Int): CharSequence {
        return when (val listItem = items.getOrNull(position)) {
            is Track -> listItem.getBubbleText(context.config.trackSorting)
            is AlbumHeader -> listItem.title
            else -> ""
        }
    }

    private fun displayEditDialog() {
        getSelectedTracks().firstOrNull()?.let { selectedTrack ->
            EditDialog(context, selectedTrack) { track ->
                val trackIndex = items.indexOfFirst { (it as? Track)?.mediaStoreId == track.mediaStoreId }
                if (trackIndex != -1) {
                    items[trackIndex] = track
                    notifyItemChanged(trackIndex)
                    finishActMode()
                }

                context.refreshQueueAndTracks(track)
            }
        }
    }
}
