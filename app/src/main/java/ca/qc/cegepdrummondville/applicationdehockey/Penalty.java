package ca.qc.cegepdrummondville.applicationdehockey;

import android.content.Context;
import android.widget.Chronometer;

/**
 * Created by 9565960 on 2016-11-14.
 */

public class Penalty {

    private int id;
    private String code;
    private int time; //En secondes
    private int player_number;
    private int local;

    public Penalty(){}

    public Penalty(String code, int time,int player_number, int local) {
        super();
        this.code = code;
        this.time = time;
        this.player_number = player_number;
        this.local = local;
    }

    //-------- Getters & setters ------ //

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPlayer_number() {
        return player_number;
    }

    public void setPlayer_number(int player_number) {
        this.player_number = player_number;
    }

    public boolean isLocal() {
        return (local > 0);
    }

    public int getLocal() { return local; }

    public void setLocal(int local) {
        this.local = local;
    }
}