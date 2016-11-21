package ca.qc.cegepdrummondville.applicationdehockey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MySQLiteHelper sqliteHelper;
    public ArrayList<Penalty> penaltyList;
    public Chronometer masterTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqliteHelper = new MySQLiteHelper(this);
        penaltyList = new ArrayList<Penalty>();
        masterTimer = new Chronometer(this);
        masterTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer)
            {
                //Cette méthode s'éxécute à chaque tick.
                updatePenalties();
            }
        });
    }

    public void addPenaltiesToView(Penalty penalty) {
        //Ici, on ajoute chaque pénalités dans la bonne ListView.
    }

    public void updatePenalties() {

    }
}
