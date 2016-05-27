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
    private int aktuellerSLKindex;

    public BenutzerSitzung(int zeitVorgabe, Benutzer benutzer,
            ArrayList<LernKarte> lKs) {
        this.zeitVorgabe = zeitVorgabe;
        this.benutzer = benutzer;
        sLKs = new ArrayList<>();
        for (LernKarte lK : lKs) {
            this.sLKs.add(new SitzungsLernKarte(lK));
        }
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
        return sLKs.get(aktuellerSLKindex);
    }
    
    public SitzungsLernKarte geheZu(int nummer) {
        aktuellerSLKindex=nummer-1;
        return getAktuelleSitzungsLernKarte();
    }
    
    public String getTitelString(int modus) {
        // modus 0 : Frage x / y (ID xxx) Schwierigkeit xxx
        // modus 1 : Themengebiete
        String rueckgabe = "";
        if (modus == 1) {
            rueckgabe += "noch nicht drin";
        } else {
            rueckgabe += "Frage " + (aktuellerSLKindex + 1) + " / " + sLKs.size();
            rueckgabe += " (ID : " +getAktuelleSitzungsLernKarte().getlK().getId() + ")";
            rueckgabe += "Schwierigkeit " + sLKs.get(aktuellerSLKindex).getlK().getSchwierigkeitsGrad();
        }
        return rueckgabe;
    }
    
    public SitzungsLernKarte getNextSitzungsLernKarte() {
        if (aktuellerSLKIndex < sLKs.size() - 1) {
            aktuellerSLKIndex++;
            sLK.cache();
        }
        return getAktuelleSitzungsLernKarte();
    }

    public SitzungsLernKarte getPrevSitzungsLernKarte() {
        if (aktuellerSLKIndex > 0) {
            aktuellerSLKIndex--;
            sLK.cache();
            
        }
        return getAktuelleSitzungsLernKarte();
    }

}
