package com.example.playlistmaker.ui.media.view_model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.App
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID.randomUUID

open class PlaylistAddFragmentViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    protected var fileName: String = ""

    fun addPlaylist(plName: String, plDescr: String) {
        viewModelScope.launch {
            val pl = Playlist(0, plName, plDescr, fileName, listOf<Int>(), 0)
            playlistInteractor.addPlaylist(pl)

        }
    }

    fun saveImageToPrivateStorage(uri: Uri, context:Context) {

        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "covers")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        var fn = ""
        var file: File
        while (true) {
            fn = randomUUID().toString()
            file = File(filePath, fn)
            if (!file.exists()) {
                break
            }
        }
        val inputStream = context.getContentResolver()?.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        fileName = fn

    }
}