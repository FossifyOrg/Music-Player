package com.simplemobiletools.musicplayer.activities

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simplemobiletools.commons.dialogs.PermissionRequiredDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.NavigationIcon
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.musicplayer.R
import com.simplemobiletools.musicplayer.adapters.AlbumsTracksAdapter
import com.simplemobiletools.musicplayer.extensions.audioHelper
import com.simplemobiletools.musicplayer.helpers.ALBUM
import com.simplemobiletools.musicplayer.helpers.ARTIST
import com.simplemobiletools.musicplayer.models.*
import kotlinx.android.synthetic.main.activity_albums.*
import kotlinx.android.synthetic.main.view_current_track_bar.current_track_bar

// Artists -> Albums -> Tracks
class AlbumsActivity : SimpleMusicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        updateMaterialActivityViews(albums_coordinator, albums_holder, useTransparentNavigation = true, useTopSearchMenu = false)
        setupMaterialScrollListener(albums_list, albums_toolbar)

        albums_fastscroller.updateColors(getProperPrimaryColor())

        val artistType = object : TypeToken<Artist>() {}.type
        val artist = Gson().fromJson<Artist>(intent.getStringExtra(ARTIST), artistType)
        albums_toolbar.title = artist.title

        ensureBackgroundThread {
            val albums = audioHelper.getArtistAlbums(artist.id)
            val listItems = ArrayList<ListItem>()
            val albumsSectionLabel = resources.getQuantityString(R.plurals.albums_plural, albums.size, albums.size)
            listItems.add(AlbumSection(albumsSectionLabel))
            listItems.addAll(albums)

            val tracksToAdd = audioHelper.getAlbumTracks(albums)
            val trackFullDuration = tracksToAdd.sumOf { it.duration }

            var tracksSectionLabel = resources.getQuantityString(R.plurals.tracks_plural, tracksToAdd.size, tracksToAdd.size)
            tracksSectionLabel += " • ${trackFullDuration.getFormattedDuration(true)}"
            listItems.add(AlbumSection(tracksSectionLabel))
            listItems.addAll(tracksToAdd)

            runOnUiThread {
                AlbumsTracksAdapter(this, listItems, albums_list) {
                    hideKeyboard()
                    if (it is Album) {
                        Intent(this, TracksActivity::class.java).apply {
                            putExtra(ALBUM, Gson().toJson(it))
                            startActivity(this)
                        }
                    } else {
                        handleNotificationPermission { granted ->
                            if (granted) {
                                val mediaItems = tracksToAdd.toMediaItems()
                                val startIndex = tracksToAdd.indexOf(it as Track)
                                playMediaItems(mediaItems, startIndex)
                            } else {
                                PermissionRequiredDialog(this, R.string.allow_notifications_music_player, { openNotificationSettings() })
                            }
                        }
                    }
                }.apply {
                    albums_list.adapter = this
                }

                if (areSystemAnimationsEnabled) {
                    albums_list.scheduleLayoutAnimation()
                }
            }
        }

        setupCurrentTrackBar(current_track_bar)
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(albums_toolbar, NavigationIcon.Arrow)
    }
}
