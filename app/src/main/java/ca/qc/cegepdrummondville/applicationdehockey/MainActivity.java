package ca.qc.cegepdrummondville.applicationdehockey;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private final long timeInPeriod = 120000; // En millisecondes
    private final long timeInSecond = 1000; // En millisecondes
    private final int penaltiesPerSide = 3; // En millisecondes
    private TextView masterTimerView;
    private MySQLiteHelper sqliteHelper;
    public ArrayList<Penalty> localPenaltyList;
    public ArrayList<Penalty> visitorPenaltyList;
    public CountDownTimer masterTimer;
    public int masterTimerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqliteHelper = new MySQLiteHelper(this);
        localPenaltyList = new ArrayList<Penalty>(penaltiesPerSide);
        visitorPenaltyList = new ArrayList<Penalty>(penaltiesPerSide);
        masterTimerTime = (int) Math.floor(timeInPeriod / timeInSecond);
        masterTimerView = (TextView) findViewById(R.id.TextView5);
        masterTimer = new CountDownTimer(timeInPeriod + timeInSecond, timeInSecond) {
            public void onTick(long millisUntilFinished) {
                updatePenalties();
                updateTimer();
            }

            public void onFinish() {
                //
            }
        };
        masterTimer.start();
    }

    public void updateTimer() {
        //Ici on change le texte affiché par le timer pour afficher le bon temps;
        masterTimerTime = masterTimerTime - 1;
        long minutes = TimeUnit.SECONDS.toMinutes(masterTimerTime);
        long secondes = masterTimerTime - (minutes * 60);
        String timeString = String.format("%02d:%02d", minutes, secondes);
        masterTimerView.setText(String.valueOf(timeString));
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
        for (int i = penaltyList.size(); i > 0; i --) {
            penalty = penaltyList.get(i - 1);
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

    //Méthode douteuse. Ne pas toucher. Elle est apparue par magie(?)
    public void AjoutPenalite (View view){
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
}
