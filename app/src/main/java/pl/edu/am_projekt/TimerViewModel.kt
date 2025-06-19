package pl.edu.am_projekt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object TimerViewModel : ViewModel() {
    private val _time = MutableLiveData<String>()
    val time: LiveData<String> = _time

    fun updateTime(newTime: String){
        _time.postValue(newTime)
    }
}