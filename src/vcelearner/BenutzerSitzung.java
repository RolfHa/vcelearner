/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcelearner;

import java.util.ArrayList;

/**
 *
 * @author J.Bleich
 */
public class BenutzerSitzung {

    private int zeitVorgabe;
    private Benutzer benutzer;
    private ArrayList<SitzungsLernKarte> sLKs;
    private int aktuellerSLKIndex;
//    private LernSitzung lernSitzung;

    public BenutzerSitzung(int zeitVorgabe, Benutzer benutzer,
            ArrayList<LernKarte> lKs) {
        this.zeitVorgabe = zeitVorgabe;
        this.benutzer = benutzer;
        sLKs = new ArrayList<>();
//        ArrayList<Benutzer2LernKarte> b2LKs = Benutzer2LernKarte.getByBenutzer();
//        ArrayList<Integer> wiederVorlageLKIDs = new ArrayList<>();
//        for (Benutzer2LernKarte b2LK : b2LKs) {
//            wiederVorlageLKIDs.add(b2LK.getLernKarte_id);
//        }
        for (LernKarte lK : lKs) {
            this.sLKs.add(new SitzungsLernKarte(lK));
//            if (wiederVorlageLKIDs.contains(lK.getID)) {
//                this.sLKs.get(this.sLKs.size()-1).setWiederVorlage(true);
//            }
        }
//        lernSitzung= new LernSitzung("ungewertet",java.time.LocalDate.now().toString(),benutzer.getId());        
//        LernSitzung.insert(lernSitzung);
    }

    public BenutzerSitzung(int zeitVorgabe, Benutzer benutzer,
            ArrayList<LernKarte> lKs, String lernSitzungsTyp) {
        this.zeitVorgabe = zeitVorgabe;
        this.benutzer = benutzer;
        sLKs = new ArrayList<>();
//        ArrayList<Benutzer2LernKarte> b2LKs = Benutzer2LernKarte.getByBenutzer();
//        ArrayList<Integer> wiederVorlageLKIDs = new ArrayList<>();
//        for (Benutzer2LernKarte b2LK : b2LKs) {
//            wiederVorlageLKIDs.add(b2LK.getLernKarte_id);
//        }
        for (LernKarte lK : lKs) {
            this.sLKs.add(new SitzungsLernKarte(lK));
//            if (wiederVorlageLKIDs.contains(lK.getID)) {
//                this.sLKs.get(this.sLKs.size()-1).setWiederVorlage(true);
//            }
        }
//        lernSitzung= new LernSitzung(lernSitzungsTyp,java.time.LocalDate.now().toString(),benutzer.getId());        
//        LernSitzung.insert(lernSitzung);
    }

    public int getAktuellerSLKIndex() {
        return aktuellerSLKIndex;
    }

    public int getZeitVorgabe() {
        return zeitVorgabe;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public ArrayList<SitzungsLernKarte> getsLKs() {
        return sLKs;
    }

    public SitzungsLernKarte getAktuelleSitzungsLernKarte() {
        return sLKs.get(aktuellerSLKIndex);
    }

    public SitzungsLernKarte geheZu(int nummer) {
        aktuellerSLKIndex = nummer - 1;
        return getAktuelleSitzungsLernKarte();
    }

    public String getTitelString(int modus) {
        // modus 0 : Frage x / y (ID xxx) Schwierigkeit xxx
        // modus 1 : Themengebiete
        String rueckgabe = "";
        if (modus == 1) {
            rueckgabe += "Themenbereich(e): " + getAktuelleSitzungsLernKarte().getlK().gettBs().toString();
        } else {
            rueckgabe += "Frage " + (aktuellerSLKIndex + 1) + " / " + sLKs.size();
            rueckgabe += "           (ID = " + getAktuelleSitzungsLernKarte().getlK().getId() + ")";
            rueckgabe += "          Schwierigkeit: " + sLKs.get(aktuellerSLKIndex).getlK().getSchwierigkeitsGrad();
        }
        return rueckgabe;
    }

    public void speichereInDB() {
        // Dummy-Code
        String ausgabe = "\nBenutzer : " + benutzer.getLogin();
        ausgabe += "\nZeitlimit : " + zeitVorgabe;
        for (SitzungsLernKarte sLK : sLKs) {
            ausgabe += "\nFrage-ID " + sLK.getlK().getId() + " gegebene Antworten : ";
            for (PotentielleAntwort pA : sLK.getlK().getpAs()) {
                if (sLK.getGegebeneAntworten().contains(pA)) {
                    ausgabe += pA.getId() + "(" + (sLK.getlK().getpAs().indexOf(pA) + 1) + "), ";
                }
            }
            ausgabe += "Gemogelt = " + sLK.isGemogelt() + ", Wiedervorlage = "
                    + sLK.isWiederVorlage() + "\n";
        }
        System.out.println(ausgabe);
    }

    public SitzungsLernKarte getNextSitzungsLernKarte() {
        if (aktuellerSLKIndex < sLKs.size() - 1) {
            aktuellerSLKIndex++;
        }
        return getAktuelleSitzungsLernKarte();
    }

    public SitzungsLernKarte getPrevSitzungsLernKarte() {
        if (aktuellerSLKIndex > 0) {
            aktuellerSLKIndex--;
        }
        return getAktuelleSitzungsLernKarte();
    }

}
