package ui.group21.HealthCareApp.smart_alarm;


public class Alarm {
    public int hour;
    public int minute;
    public String time;
    public Boolean music;
    public Boolean rung;
    public Alarm(int hour, int minute,String time, Boolean music, Boolean rung){
        this.hour = hour;
        this.minute = minute;
        this.time = time;
        this.music = music;
        this.rung = rung;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Boolean getMusic() {
        return music;
    }

    public void setMusic(Boolean music) {
        this.music = music;
    }

    public Boolean getRung() {
        return rung;
    }

    public void setRung(Boolean rung) {
        this.rung = rung;
    }
}
