package ca.qc.cegepdrummondville.applicationdehockey;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final long timeInPeriod = 30000; // En millisecondes
    private final long timeInSecond = 1000; // En millisecondes
    private final int penaltiesPerSide = 3; // En millisecondes
    private MySQLiteHelper sqliteHelper;
    public ArrayList<Penalty> localPenaltyList;
    public ArrayList<Penalty> visitorPenaltyList;
    public CountDownTimer masterTimer;

    Button ajoutPenalite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqliteHelper = new MySQLiteHelper(this);
        localPenaltyList = new ArrayList<Penalty>(penaltiesPerSide);
        visitorPenaltyList = new ArrayList<Penalty>(penaltiesPerSide);
        masterTimer = new CountDownTimer(timeInPeriod, timeInSecond) {
            public void onTick(long millisUntilFinished) {
                updatePenalties();
                updateTimer();
            }

            public void onFinish() {
                //
            }
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialisation du bouton

        ajoutPenalite = (Button) findViewById(R.id.button2);

        ajoutPenalite.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {

    }

    public void updateTimer() {
        //Ici on change le texte affiché par le timer pour afficher le bon temps;
    }

    /*
        Diminue le temps de chaque penalités dans 'penaltyList'.
        Si une pénalité atteint 0, elle est supprimée de la liste et
        la prochaine est chargée de la base de donnée.
     */
    public void updatePenalties() {
        ArrayList<Penalty> penaltyList = new ArrayList<Penalty>();
        //Combine les deux listes en une seule
        penaltyList.addAll(localPenaltyList);
        penaltyList.addAll(visitorPenaltyList);
        Penalty penalty;

        //Les deux listes sont vidées, mais les pénalités sont réajoutées dans le loop. (C'est d'la marde je sais)
        localPenaltyList.clear();
        visitorPenaltyList.clear();

        //Ici, le temps des pénalités sont diminués. Les pénalités qui sont terminées ne sont pas réajoutées dans les listes. RIP
        for (int i = penaltyList.size(); i >= 0; i --) {
            penalty = penaltyList.get(i);
            if (penalty.getTime() <= 1) {
                penalty.setTime(penalty.getTime() - 1);
                if (penalty.getLocal() > 0) {
                    localPenaltyList.add(penalty);
                } else {
                    visitorPenaltyList.add(penalty);
                }
            } else {
                penalty.setTime(0);
                sqliteHelper.updatePenalty(penalty);
            }
        }

        //Ici, il fadrait remplir les espaces vides des listes.

        //La vue est mise à jour avec les nouvelles listes.
        updatePenaltyListView();
    }

    /*
        Ajoute une pénalité à 'penaltyList'. Si la liste est pleine, la dernière
        est sauvegardée dans la base de données et supprimée de la liste.
        La vue est ensuite mise à jour avec 'updatePenaltyListView'.
     */
    public void addPenalty(String code, int time,int player_number, int local) {
        Penalty penalty = new Penalty(code, time, player_number, local);
        sqliteHelper.addPenalty(penalty);
        if (local > 0) {
            //Wow la duplication de code nice
            if (localPenaltyList.size() < penaltiesPerSide) {
                localPenaltyList.add(penalty);
            }
        } else {
            //Wow la duplication de code nice
            if (visitorPenaltyList.size() < penaltiesPerSide) {
                visitorPenaltyList.add(penalty);
            }
        }
        //La vue est mise à jour avec les nouvelles listes.
        updatePenaltyListView();
    }

    public void updatePenaltyListView() {
        //Ici, on ajoute chaque pénalités dans la bonne ListView.
    }

}
