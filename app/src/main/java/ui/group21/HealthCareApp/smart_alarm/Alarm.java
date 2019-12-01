package ui.group21.HealthCareApp.smart_alarm;

public class Alarm {
    public String time;
    public Boolean music;
    public Boolean rung;
    public Alarm(String time, Boolean music, Boolean rung){
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
