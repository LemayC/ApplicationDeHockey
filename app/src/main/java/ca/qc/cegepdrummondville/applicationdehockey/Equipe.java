package ca.qc.cegepdrummondville.applicationdehockey;

/**
 * Created by 9565960 on 2016-10-31.
 */

public class Equipe {
    private long id;
    private String nom;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return nom;
    }

    public void setComment(String comment) {
        this.nom = nom;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return nom;
    }
}