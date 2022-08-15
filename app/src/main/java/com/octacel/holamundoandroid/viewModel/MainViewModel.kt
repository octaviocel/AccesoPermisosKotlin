package com.octacel.holamundoandroid.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private val state: SavedStateHandle):ViewModel(){

    private var count = state.getLiveData<Int>("counter",0)

    val _count : LiveData<Int>get() = count

    fun incrementCounter(){
        count.value = count.value!! +1
    }
}