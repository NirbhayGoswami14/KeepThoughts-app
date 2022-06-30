package com.ncode.keepthoughts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ncode.keepthoughts.repository.NoteRepository


class NotesViewModelFactory(var repository: NoteRepository):ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(repository) as T
    }
}