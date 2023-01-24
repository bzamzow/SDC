package edu.wgu.zamzow.medalert.objects;

public class Report {

    private int numMeds;
    private int numTaken;
    private int numMissed;

    public int getNumMeds() {
        return numMeds;
    }

    public int getNumMissed() {
        return numMissed;
    }

    public int getNumTaken() {
        return numTaken;
    }

    public void setNumMeds(int numMeds) {
        this.numMeds = numMeds;
    }

    public void setNumMissed(int numMissed) {
        this.numMissed = numMissed;
    }

    public void setNumTaken(int numTaken) {
        this.numTaken = numTaken;
    }
}
