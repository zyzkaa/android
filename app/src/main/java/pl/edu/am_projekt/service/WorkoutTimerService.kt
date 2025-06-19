package pl.edu.am_projekt.service

import pl.edu.am_projekt.TimerViewModel

class WorkoutTimerService : TimerService() {
    private var time = ""
    override val notificationText = "Workout in progress"
    override val notificationTitle = "Workout in progress"

    private fun formatTime(currentSeconds: Int): String {
        val hours = currentSeconds / 3600
        val minutes = (currentSeconds % 3600) / 60
        val seconds = currentSeconds % 60
        var time = ""
        if(hours > 0){
            time += "$hours:"
        }
        time += if(minutes < 10){
            "0$minutes:"
        } else {
            "$minutes:"
        }
        time += if(seconds < 10){
            "0$seconds"
        } else {
            seconds
        }
        return time
    }

    override fun onTick(seconds: Int) {
        time = formatTime(seconds)
        TimerViewModel.updateTime(time)
    }

}