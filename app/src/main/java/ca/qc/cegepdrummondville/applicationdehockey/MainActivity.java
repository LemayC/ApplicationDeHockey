package ca.qc.cegepdrummondville.applicationdehockey;


import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final long timeInPeriod = 1200000; // En millisecondes
    private final long timeInSecond = 1000; // En millisecondes
    private final int penaltiesPerSide = 3; // En millisecondes
    private Button button2;
    private TextView masterTimerView;
    private MySQLiteHelper sqliteHelper;
    public ArrayList<Penalty> localPenaltyList;
    public ArrayList<Penalty> visitorPenaltyList;
    public CountDownTimer masterTimer;
    public int masterTimerTime;
    Button ajoutPenalite;

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

        //Initialisation du bouton

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ajoutPenalite();
    }

    public void updateTimer() {
        //Ici on change le texte affiché par le timer pour afficher le bon temps;
        masterTimerTime = masterTimerTime - 1;
        long minutes = TimeUnit.SECONDS.toMinutes(masterTimerTime);
        long secondes = masterTimerTime - (minutes * 60);
        String timeString = String.format("%02d:%02d", minutes, secondes);
        masterTimerView.setText(String.valueOf(timeString));
    }

    public void stopTimer() {
        masterTimer = null;
    }

    public void resumeTimer() {
        long timeLeft = (masterTimerTime * timeInSecond) + timeInSecond;
        masterTimer = new CountDownTimer(timeLeft, timeInSecond) {
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

        //Ici, on rempli les espaces vides des listes.
        if (localPenaltyList.size() < penaltiesPerSide) {
            penalty = sqliteHelper.getLastPenalty(true);
            if (penalty != null) {
                localPenaltyList.add(penalty);
            }
        }

        if (visitorPenaltyList.size() < penaltiesPerSide) {
            penalty = sqliteHelper.getLastPenalty(false);
            if (penalty != null) {
                visitorPenaltyList.add(penalty);
            }
        }
        
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
            if (localPenaltyList.size() >= penaltiesPerSide) {
                sqliteHelper.updatePenalty(localPenaltyList.get(penaltiesPerSide - 1));
                localPenaltyList.remove(penaltiesPerSide - 1);
            }
            localPenaltyList.add(0, penalty);
        } else {
            if (visitorPenaltyList.size() >= penaltiesPerSide) {
                sqliteHelper.updatePenalty(visitorPenaltyList.get(penaltiesPerSide - 1));
                visitorPenaltyList.remove(penaltiesPerSide - 1);
            }
            visitorPenaltyList.add(0, penalty);
        }

        //La vue est mise à jour avec les nouvelles listes.
        updatePenaltyListView();
    }

    public void updatePenaltyListView() {
        //Ici, on ajoute chaque pénalités dans la bonne ListView.
    }

    public void ajoutPenalite() {

    }
}
