/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcelearner;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.event.ActionListener;

/**
 *
 * @author J.Weidehaas
 */
public class LernenUI extends javax.swing.JFrame {

    BenutzerSitzung session;

    /**
     * Creates new form LernenUIMockup
     */
    public LernenUI() {
        initComponents();

        textAreasAntwort = new javax.swing.JTextArea[]{textAreaAntwortA,
            textAreaAntwortB, textAreaAntwortC, textAreaAntwortD, textAreaAntwortE,
            textAreaAntwortF, textAreaAntwortG, textAreaAntwortH};

        checkBoxesAntwort = new javax.swing.JCheckBox[]{checkBoxA, checkBoxB,
            checkBoxC, checkBoxD, checkBoxE, checkBoxF, checkBoxG, checkBoxH};

        panelsAntwort = new javax.swing.JPanel[]{panelAntwortA, panelAntwortB,
            panelAntwortC, panelAntwortD, panelAntwortE, panelAntwortF, panelAntwortG,
            panelAntwortH};

        timerDauer = session.getZeitVorgabe();
        timerZaehlt();
        fillWithValues();

    }

    public LernenUI(BenutzerSitzung session) {
        this.session = session;
        initComponents();

        textAreasAntwort = new javax.swing.JTextArea[]{textAreaAntwortA,
            textAreaAntwortB, textAreaAntwortC, textAreaAntwortD, textAreaAntwortE,
            textAreaAntwortF, textAreaAntwortG, textAreaAntwortH};

        checkBoxesAntwort = new javax.swing.JCheckBox[]{checkBoxA, checkBoxB,
            checkBoxC, checkBoxD, checkBoxE, checkBoxF, checkBoxG, checkBoxH};

        panelsAntwort = new javax.swing.JPanel[]{panelAntwortA, panelAntwortB,
            panelAntwortC, panelAntwortD, panelAntwortE, panelAntwortF, panelAntwortG,
            panelAntwortH};

        timerDauer = session.getZeitVorgabe();
        timerZaehlt();
        fillWithValues();
    }

    public void fillWithValues() {

        if (session.getAktuellerSLKIndex() < session.getsLKs().size() - 1) {
            buttonVor.setEnabled(true);
        } else {
            buttonVor.setEnabled(false);
        }

        if (session.getAktuellerSLKIndex() > 0) {
            buttonZurueck.setEnabled(true);
        } else {
            buttonZurueck.setEnabled(false);
        }

        textAreaFrage.setText(session.getAktuelleSitzungsLernKarte().getlK().getFrage());

        labelTitel.setText(session.getTitelString(0));

        toggleButtonMogeln.setSelected(session.getAktuelleSitzungsLernKarte().isMogelnAktiv());

        toggleButtonWiedervorlage.setSelected(session.getAktuelleSitzungsLernKarte().isWiederVorlage());

        panelFrage.setPreferredSize(new Dimension(800, 30 + calcStringHoehe(textAreaFrage)));

        for (int i = 0; i < 8; i++) {
            if (i < session.getAktuelleSitzungsLernKarte().getlK().getpAs().size()) {
                textAreasAntwort[i].setText(session.getAktuelleSitzungsLernKarte().getlK().getpAs().get(i).getAntwort());
                if (modus == 0) { //wenn Lernmodus
                    checkBoxesAntwort[i].setEnabled(true); // Checkbox aktivieren
                } else { // wenn nicht Lernmodus, also Lesemodus
                    // #WIP hier code zur hervorhebung der richtigen/falschen Antworten
                }
                if (session.getAktuelleSitzungsLernKarte().getGegebeneAntworten().contains(
                        session.getAktuelleSitzungsLernKarte().getlK().getpAs().get(i))) {
                    // wenn Antwort unter den gegebenen Antworten
                    checkBoxesAntwort[i].setSelected(true);
                } else {
                    // wenn und Antwort nicht unter den gegebenen Antworten
                    checkBoxesAntwort[i].setSelected(false);
                }
                panelsAntwort[i].setPreferredSize(new Dimension(800, 30 + calcStringHoehe(textAreasAntwort[i])));
                panelsAntwort[i].setVisible(true);
            } else {
                textAreasAntwort[i].setText("");
                checkBoxesAntwort[i].setSelected(false);
                checkBoxesAntwort[i].setEnabled(false);

                panelsAntwort[i].setVisible(false);
            }
        }
        scrollPane1.setViewportView(panelText);
    }

    private void leseModus() {
        modus = 1;
        for (javax.swing.JCheckBox cb : checkBoxesAntwort) {
            cb.setEnabled(false);
        }
        session.geheZu(1);
        fillWithValues();
    }

    private void beendeLernModus() {
        session.speichereInDB();
        leseModus();
    }

    private void timerZaehlt() {

        zaehlerLaeuft = true;

        final long start = System.currentTimeMillis();
        final long end = start + timerDauer * 60 * 1000;
        // Setzen Eingabe für Dauer auf 19min fest , Variable ist timerDauer

        final Timer timer = new Timer(1000, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //long zwischen = 0;  
                long now = System.currentTimeMillis();
                if (now >= end) {
                    //remainingMinLabel.setText( "" );
                    labelTimer.setText("");
                    //startButton.setEnabled( true );
                    //JOptionPane.showMessageDialog( null, "BING!" );
                    timer.stop();
                    beendeLernModus();
                    zaehlerLaeuft = false;

                } else //zwischen = (end-now)/1000; 
                {
                    String countdown = ((end - now) / 60000) + ":";
                    if ((((end - now) / 1000) % 60) > 9) {
                        countdown += (((end - now) / 1000) % 60);
                    } else {
                        countdown += "0" + (((end - now) / 1000) % 60);
                    }
                    labelTimer.setText(countdown);
                }
            }
        });
        timer.start();

    }

    public void cache() {

        for (int i = 0; i < checkBoxesAntwort.length; i++) {
            if (i < session.getAktuelleSitzungsLernKarte().getlK().getpAs().size()) {

                if ((checkBoxesAntwort[i].isSelected() == true)
                        && !session.getAktuelleSitzungsLernKarte().getGegebeneAntworten().contains(session.getAktuelleSitzungsLernKarte().getlK().getpAs().get(i))) {
                    session.getAktuelleSitzungsLernKarte().getGegebeneAntworten().add(session.getAktuelleSitzungsLernKarte().getlK().getpAs().get(i));
                } else if ((checkBoxesAntwort[i].isSelected() == false)
                        && session.getAktuelleSitzungsLernKarte().getGegebeneAntworten().contains(session.getAktuelleSitzungsLernKarte().getlK().getpAs().get(i))) {
                    session.getAktuelleSitzungsLernKarte().getGegebeneAntworten().remove(session.getAktuelleSitzungsLernKarte().getlK().getpAs().get(i));
                }
            }
        }

        session.getAktuelleSitzungsLernKarte().setWiederVorlage((toggleButtonWiedervorlage.isSelected()));

    }

    private static int calcZeilenZahl(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        int zeilen = 1;
        int pos = 0;
        while ((pos = text.indexOf("\n", pos) + 1) != 0) {
            zeilen++;
        }
        return zeilen;
    }

    private static int calcStringHoehe(javax.swing.JTextArea txtArea) {
        int hoehe = 0;
        java.awt.Graphics grafik = txtArea.getGraphics();
        java.awt.FontMetrics metrik = grafik.getFontMetrics();
        hoehe = metrik.getHeight();
        hoehe *= calcZeilenZahl(txtArea.getText());
        return hoehe;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBackground = new javax.swing.JPanel();
        scrollPane1 = new javax.swing.JScrollPane();
        panelText = new javax.swing.JPanel();
        panelAntwortD = new javax.swing.JPanel();
        checkBoxD = new javax.swing.JCheckBox();
        scrollPaneAntwortD = new javax.swing.JScrollPane();
        textAreaAntwortD = new javax.swing.JTextArea();
        panelAntwortC = new javax.swing.JPanel();
        checkBoxC = new javax.swing.JCheckBox();
        scrollPaneAntwortC = new javax.swing.JScrollPane();
        textAreaAntwortC = new javax.swing.JTextArea();
        panelAntwortA = new javax.swing.JPanel();
        scrollPaneAntwortA = new javax.swing.JScrollPane();
        textAreaAntwortA = new javax.swing.JTextArea();
        checkBoxA = new javax.swing.JCheckBox();
        panelAntwortF = new javax.swing.JPanel();
        scrollPaneAntwortF = new javax.swing.JScrollPane();
        textAreaAntwortF = new javax.swing.JTextArea();
        checkBoxF = new javax.swing.JCheckBox();
        panelAntwortE = new javax.swing.JPanel();
        checkBoxE = new javax.swing.JCheckBox();
        scrollPaneAntwortE = new javax.swing.JScrollPane();
        textAreaAntwortE = new javax.swing.JTextArea();
        panelAntwortB = new javax.swing.JPanel();
        checkBoxB = new javax.swing.JCheckBox();
        scrollPaneAntwortB = new javax.swing.JScrollPane();
        textAreaAntwortB = new javax.swing.JTextArea();
        panelFrage = new javax.swing.JPanel();
        scrollPaneFrage = new javax.swing.JScrollPane();
        textAreaFrage = new javax.swing.JTextArea();
        panelAntwortG = new javax.swing.JPanel();
        scrollPaneAntwortG = new javax.swing.JScrollPane();
        textAreaAntwortG = new javax.swing.JTextArea();
        checkBoxG = new javax.swing.JCheckBox();
        panelAntwortH = new javax.swing.JPanel();
        checkBoxH = new javax.swing.JCheckBox();
        scrollPaneAntwortH = new javax.swing.JScrollPane();
        textAreaAntwortH = new javax.swing.JTextArea();
        jPanelTitel = new javax.swing.JPanel();
        labelTitel = new javax.swing.JLabel();
        labelTimer = new javax.swing.JLabel();
        panelNavigation = new javax.swing.JPanel();
        buttonVor = new javax.swing.JButton();
        toggleButtonMogeln = new javax.swing.JToggleButton();
        ButtonEnde = new javax.swing.JButton();
        buttonZurueck = new javax.swing.JButton();
        textFieldGeheZu = new javax.swing.JTextField();
        buttonGeheZu = new javax.swing.JButton();
        toggleButtonWiedervorlage = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelBackground.setBackground(new java.awt.Color(153, 153, 153));

        panelAntwortD.setBackground(new java.awt.Color(240,240,240));

        checkBoxD.setText("D:");

        scrollPaneAntwortD.setBackground(panelAntwortD.getBackground());
        scrollPaneAntwortD.setBorder(null);

        textAreaAntwortD.setEditable(false);
        textAreaAntwortD.setBackground(panelAntwortD.getBackground());
        textAreaAntwortD.setColumns(20);
        textAreaAntwortD.setRows(1);
        textAreaAntwortD.setBorder(null);
        scrollPaneAntwortD.setViewportView(textAreaAntwortD);

        javax.swing.GroupLayout panelAntwortDLayout = new javax.swing.GroupLayout(panelAntwortD);
        panelAntwortD.setLayout(panelAntwortDLayout);
        panelAntwortDLayout.setHorizontalGroup(
            panelAntwortDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortDLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(checkBoxD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneAntwortD)
                .addContainerGap())
        );
        panelAntwortDLayout.setVerticalGroup(
            panelAntwortDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAntwortDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxD)
                    .addComponent(scrollPaneAntwortD, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelAntwortC.setBackground(new java.awt.Color(255,255,255));

        checkBoxC.setText("C:");

        scrollPaneAntwortC.setBackground(panelAntwortC.getBackground());
        scrollPaneAntwortC.setBorder(null);

        textAreaAntwortC.setEditable(false);
        textAreaAntwortC.setBackground(panelAntwortC.getBackground());
        textAreaAntwortC.setColumns(20);
        textAreaAntwortC.setRows(1);
        textAreaAntwortC.setBorder(null);
        scrollPaneAntwortC.setViewportView(textAreaAntwortC);

        javax.swing.GroupLayout panelAntwortCLayout = new javax.swing.GroupLayout(panelAntwortC);
        panelAntwortC.setLayout(panelAntwortCLayout);
        panelAntwortCLayout.setHorizontalGroup(
            panelAntwortCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortCLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(checkBoxC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneAntwortC)
                .addContainerGap())
        );
        panelAntwortCLayout.setVerticalGroup(
            panelAntwortCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortCLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAntwortCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxC)
                    .addComponent(scrollPaneAntwortC, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelAntwortA.setBackground(new java.awt.Color(255,255,255));

        scrollPaneAntwortA.setBackground(panelAntwortA.getBackground());
        scrollPaneAntwortA.setBorder(null);

        textAreaAntwortA.setEditable(false);
        textAreaAntwortA.setBackground(panelAntwortA.getBackground());
        textAreaAntwortA.setColumns(20);
        textAreaAntwortA.setRows(1);
        textAreaAntwortA.setBorder(null);
        scrollPaneAntwortA.setViewportView(textAreaAntwortA);

        checkBoxA.setBackground(panelAntwortA.getBackground());
        checkBoxA.setText("A:");

        javax.swing.GroupLayout panelAntwortALayout = new javax.swing.GroupLayout(panelAntwortA);
        panelAntwortA.setLayout(panelAntwortALayout);
        panelAntwortALayout.setHorizontalGroup(
            panelAntwortALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortALayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(checkBoxA)
                .addGap(2, 2, 2)
                .addComponent(scrollPaneAntwortA)
                .addContainerGap())
        );
        panelAntwortALayout.setVerticalGroup(
            panelAntwortALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortALayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelAntwortALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxA)
                    .addComponent(scrollPaneAntwortA, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

        panelAntwortF.setBackground(new java.awt.Color(240,240,240));

        scrollPaneAntwortF.setBackground(panelAntwortF.getBackground());
        scrollPaneAntwortF.setBorder(null);

        textAreaAntwortF.setEditable(false);
        textAreaAntwortF.setBackground(panelAntwortF.getBackground());
        textAreaAntwortF.setColumns(20);
        textAreaAntwortF.setRows(1);
        textAreaAntwortF.setBorder(null);
        scrollPaneAntwortF.setViewportView(textAreaAntwortF);

        checkBoxF.setText("F:");

        javax.swing.GroupLayout panelAntwortFLayout = new javax.swing.GroupLayout(panelAntwortF);
        panelAntwortF.setLayout(panelAntwortFLayout);
        panelAntwortFLayout.setHorizontalGroup(
            panelAntwortFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortFLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(checkBoxF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneAntwortF)
                .addContainerGap())
        );
        panelAntwortFLayout.setVerticalGroup(
            panelAntwortFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortFLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAntwortFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxF)
                    .addComponent(scrollPaneAntwortF, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelAntwortE.setBackground(new java.awt.Color(255,255,255));

        checkBoxE.setText("E:");
        checkBoxE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxEActionPerformed(evt);
            }
        });

        scrollPaneAntwortE.setBackground(panelAntwortE.getBackground());
        scrollPaneAntwortE.setBorder(null);

        textAreaAntwortE.setEditable(false);
        textAreaAntwortE.setBackground(panelAntwortE.getBackground());
        textAreaAntwortE.setColumns(20);
        textAreaAntwortE.setRows(1);
        textAreaAntwortE.setBorder(null);
        scrollPaneAntwortE.setViewportView(textAreaAntwortE);

        javax.swing.GroupLayout panelAntwortELayout = new javax.swing.GroupLayout(panelAntwortE);
        panelAntwortE.setLayout(panelAntwortELayout);
        panelAntwortELayout.setHorizontalGroup(
            panelAntwortELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortELayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(checkBoxE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneAntwortE)
                .addContainerGap())
        );
        panelAntwortELayout.setVerticalGroup(
            panelAntwortELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortELayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAntwortELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxE)
                    .addComponent(scrollPaneAntwortE, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelAntwortB.setBackground(new java.awt.Color(240,240,240));

        checkBoxB.setText("B:");

        scrollPaneAntwortB.setBackground(panelAntwortB.getBackground());
        scrollPaneAntwortB.setBorder(null);

        textAreaAntwortB.setEditable(false);
        textAreaAntwortB.setBackground(panelAntwortB.getBackground());
        textAreaAntwortB.setColumns(20);
        textAreaAntwortB.setRows(1);
        textAreaAntwortB.setBorder(null);
        scrollPaneAntwortB.setViewportView(textAreaAntwortB);

        javax.swing.GroupLayout panelAntwortBLayout = new javax.swing.GroupLayout(panelAntwortB);
        panelAntwortB.setLayout(panelAntwortBLayout);
        panelAntwortBLayout.setHorizontalGroup(
            panelAntwortBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortBLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(checkBoxB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneAntwortB, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        panelAntwortBLayout.setVerticalGroup(
            panelAntwortBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortBLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAntwortBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxB)
                    .addComponent(scrollPaneAntwortB, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelFrage.setBackground(new java.awt.Color(240,240,240)
        );

        scrollPaneFrage.setBackground(scrollPaneFrage.getBackground());
        scrollPaneFrage.setBorder(null        );

        textAreaFrage.setEditable(false);
        textAreaFrage.setBackground(panelFrage.getBackground());
        textAreaFrage.setColumns(20);
        textAreaFrage.setRows(1);
        textAreaFrage.setBorder(null);
        scrollPaneFrage.setViewportView(textAreaFrage);

        javax.swing.GroupLayout panelFrageLayout = new javax.swing.GroupLayout(panelFrage);
        panelFrage.setLayout(panelFrageLayout);
        panelFrageLayout.setHorizontalGroup(
            panelFrageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFrageLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(scrollPaneFrage)
                .addGap(10, 10, 10))
        );
        panelFrageLayout.setVerticalGroup(
            panelFrageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFrageLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(scrollPaneFrage)
                .addGap(10, 10, 10))
        );

        panelAntwortG.setBackground(new java.awt.Color(255,255,255));

        scrollPaneAntwortG.setBackground(panelAntwortG.getBackground());
        scrollPaneAntwortG.setBorder(null);

        textAreaAntwortG.setEditable(false);
        textAreaAntwortG.setBackground(panelAntwortG.getBackground());
        textAreaAntwortG.setColumns(20);
        textAreaAntwortG.setRows(1);
        textAreaAntwortG.setBorder(null);
        scrollPaneAntwortG.setViewportView(textAreaAntwortG);

        checkBoxG.setText("G:");

        javax.swing.GroupLayout panelAntwortGLayout = new javax.swing.GroupLayout(panelAntwortG);
        panelAntwortG.setLayout(panelAntwortGLayout);
        panelAntwortGLayout.setHorizontalGroup(
            panelAntwortGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortGLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(checkBoxG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneAntwortG)
                .addContainerGap())
        );
        panelAntwortGLayout.setVerticalGroup(
            panelAntwortGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortGLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAntwortGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxG)
                    .addComponent(scrollPaneAntwortG, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        panelAntwortH.setBackground(new java.awt.Color(240,240,240));

        checkBoxH.setText("H:");
        checkBoxH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxHActionPerformed(evt);
            }
        });

        scrollPaneAntwortH.setBackground(panelAntwortH.getBackground());
        scrollPaneAntwortH.setBorder(null);

        textAreaAntwortH.setEditable(false);
        textAreaAntwortH.setBackground(panelAntwortH.getBackground());
        textAreaAntwortH.setColumns(20);
        textAreaAntwortH.setRows(1);
        textAreaAntwortH.setBorder(null);
        scrollPaneAntwortH.setViewportView(textAreaAntwortH);

        javax.swing.GroupLayout panelAntwortHLayout = new javax.swing.GroupLayout(panelAntwortH);
        panelAntwortH.setLayout(panelAntwortHLayout);
        panelAntwortHLayout.setHorizontalGroup(
            panelAntwortHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortHLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(checkBoxH)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneAntwortH)
                .addContainerGap())
        );
        panelAntwortHLayout.setVerticalGroup(
            panelAntwortHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAntwortHLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAntwortHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxH)
                    .addComponent(scrollPaneAntwortH, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelTextLayout = new javax.swing.GroupLayout(panelText);
        panelText.setLayout(panelTextLayout);
        panelTextLayout.setHorizontalGroup(
            panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTextLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAntwortB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAntwortA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAntwortC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAntwortD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAntwortE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAntwortF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAntwortG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAntwortH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFrage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panelTextLayout.setVerticalGroup(
            panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTextLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(panelFrage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelAntwortA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelAntwortB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelAntwortC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelAntwortD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelAntwortE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelAntwortF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelAntwortG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelAntwortH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        scrollPane1.setViewportView(panelText);

        jPanelTitel.setBackground(panelBackground.getBackground());

        labelTitel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelTitel.setForeground(new java.awt.Color(240, 240, 240));
        labelTitel.setText("Frage X von Y (ID=Z) Schwierigkeitsgrad=0");
        labelTitel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labelTitelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                labelTitelMouseExited(evt);
            }
        });

        labelTimer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelTimer.setForeground(new java.awt.Color(240, 240, 240));
        labelTimer.setText("00:00");

        javax.swing.GroupLayout jPanelTitelLayout = new javax.swing.GroupLayout(jPanelTitel);
        jPanelTitel.setLayout(jPanelTitelLayout);
        jPanelTitelLayout.setHorizontalGroup(
            jPanelTitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTitelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(labelTitel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelTimer)
                .addGap(50, 50, 50))
        );
        jPanelTitelLayout.setVerticalGroup(
            jPanelTitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTitelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTitelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTitel)
                    .addComponent(labelTimer))
                .addContainerGap())
        );

        panelNavigation.setBackground(panelBackground.getBackground());

        buttonVor.setText(">>");
        buttonVor.setPreferredSize(new java.awt.Dimension(120, 23));
        buttonVor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVorActionPerformed(evt);
            }
        });

        toggleButtonMogeln.setText("Antwort anzeigen");
        toggleButtonMogeln.setPreferredSize(new java.awt.Dimension(120, 23));
        toggleButtonMogeln.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonMogelnActionPerformed(evt);
            }
        });

        ButtonEnde.setText("Beenden");
        ButtonEnde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEndeActionPerformed(evt);
            }
        });

        buttonZurueck.setText("<<");
        buttonZurueck.setPreferredSize(new java.awt.Dimension(120, 23));
        buttonZurueck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonZurueckActionPerformed(evt);
            }
        });

        textFieldGeheZu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldGeheZuKeyPressed(evt);
            }
        });

        buttonGeheZu.setText("gehe zu:");
        buttonGeheZu.setPreferredSize(new java.awt.Dimension(120, 23));
        buttonGeheZu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGeheZuActionPerformed(evt);
            }
        });

        toggleButtonWiedervorlage.setText("Wiedervorlage");
        toggleButtonWiedervorlage.setPreferredSize(new java.awt.Dimension(120, 23));
        toggleButtonWiedervorlage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonWiedervorlageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelNavigationLayout = new javax.swing.GroupLayout(panelNavigation);
        panelNavigation.setLayout(panelNavigationLayout);
        panelNavigationLayout.setHorizontalGroup(
            panelNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNavigationLayout.createSequentialGroup()
                .addGroup(panelNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNavigationLayout.createSequentialGroup()
                        .addContainerGap(20, Short.MAX_VALUE)
                        .addComponent(toggleButtonMogeln, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNavigationLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(buttonZurueck, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                .addComponent(buttonGeheZu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                .addComponent(buttonVor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                            .addComponent(toggleButtonWiedervorlage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ButtonEnde, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNavigationLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(textFieldGeheZu, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        panelNavigationLayout.setVerticalGroup(
            panelNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavigationLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(buttonVor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(buttonZurueck, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(buttonGeheZu, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldGeheZu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(toggleButtonWiedervorlage, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(toggleButtonMogeln, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ButtonEnde, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelBackgroundLayout = new javax.swing.GroupLayout(panelBackground);
        panelBackground.setLayout(panelBackgroundLayout);
        panelBackgroundLayout.setHorizontalGroup(
            panelBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBackgroundLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBackgroundLayout.createSequentialGroup()
                        .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(panelNavigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelTitel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelBackgroundLayout.setVerticalGroup(
            panelBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBackgroundLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jPanelTitel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelNavigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPane1))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBackground, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void labelTitelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelTitelMouseEntered
        labelTitel.setText(session.getTitelString(1));
    }//GEN-LAST:event_labelTitelMouseEntered

    private void labelTitelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelTitelMouseExited
        labelTitel.setText(session.getTitelString(0));
    }//GEN-LAST:event_labelTitelMouseExited

    private void checkBoxHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxHActionPerformed

    private void checkBoxEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxEActionPerformed

    private void toggleButtonWiedervorlageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleButtonWiedervorlageActionPerformed
        session.getAktuelleSitzungsLernKarte().setWiederVorlage(true);
    }//GEN-LAST:event_toggleButtonWiedervorlageActionPerformed

    private void buttonGeheZuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGeheZuActionPerformed
        cache();
        session.geheZu(Integer.parseInt(textFieldGeheZu.getText()));
        fillWithValues();
    }//GEN-LAST:event_buttonGeheZuActionPerformed

    private void buttonZurueckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonZurueckActionPerformed
        cache();
        session.getPrevSitzungsLernKarte();
        fillWithValues();
    }//GEN-LAST:event_buttonZurueckActionPerformed

    private void ButtonEndeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEndeActionPerformed
        if (modus == 0) {
            beendeLernModus();
        }
    }//GEN-LAST:event_ButtonEndeActionPerformed

    private void buttonVorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVorActionPerformed
        cache();
        session.getNextSitzungsLernKarte();
        fillWithValues();
    }//GEN-LAST:event_buttonVorActionPerformed

    private void toggleButtonMogelnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleButtonMogelnActionPerformed
        if (toggleButtonMogeln.isSelected()) {
            session.getAktuelleSitzungsLernKarte().setGemogeltTrue();
            session.getAktuelleSitzungsLernKarte().setMogelnAktiv(true);
        } else {
            session.getAktuelleSitzungsLernKarte().setMogelnAktiv(false);
        }
        fillWithValues();
    }//GEN-LAST:event_toggleButtonMogelnActionPerformed

    private void textFieldGeheZuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldGeheZuKeyPressed
        // key=10 heisst Enter o. Return-Taste gedrückt
        int key = evt.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_ENTER) {
            cache();
            session.geheZu(Integer.parseInt(textFieldGeheZu.getText()));
            fillWithValues();
        }
    }//GEN-LAST:event_textFieldGeheZuKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LernenUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LernenUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LernenUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LernenUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LernenUI().setVisible(true);
            }
        });
    }
    private javax.swing.JPanel[] panelsAntwort;
    private int modus = 0; //LernModus = 0, LeseModus = 1
    private javax.swing.JCheckBox[] checkBoxesAntwort;
    boolean zaehlerLaeuft = false;  // gibt an ob der Timr noch läuft
    int timerDauer; // max. Laufzeit des Timers (aus der Vorauswahlmodul in Minuten)
    private javax.swing.JTextArea[] textAreasAntwort;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonEnde;
    private javax.swing.JButton buttonGeheZu;
    private javax.swing.JButton buttonVor;
    private javax.swing.JButton buttonZurueck;
    private javax.swing.JCheckBox checkBoxA;
    private javax.swing.JCheckBox checkBoxB;
    private javax.swing.JCheckBox checkBoxC;
    private javax.swing.JCheckBox checkBoxD;
    private javax.swing.JCheckBox checkBoxE;
    private javax.swing.JCheckBox checkBoxF;
    private javax.swing.JCheckBox checkBoxG;
    private javax.swing.JCheckBox checkBoxH;
    private javax.swing.JPanel jPanelTitel;
    private javax.swing.JLabel labelTimer;
    private javax.swing.JLabel labelTitel;
    private javax.swing.JPanel panelAntwortA;
    private javax.swing.JPanel panelAntwortB;
    private javax.swing.JPanel panelAntwortC;
    private javax.swing.JPanel panelAntwortD;
    private javax.swing.JPanel panelAntwortE;
    private javax.swing.JPanel panelAntwortF;
    private javax.swing.JPanel panelAntwortG;
    private javax.swing.JPanel panelAntwortH;
    private javax.swing.JPanel panelBackground;
    private javax.swing.JPanel panelFrage;
    private javax.swing.JPanel panelNavigation;
    private javax.swing.JPanel panelText;
    private javax.swing.JScrollPane scrollPane1;
    private javax.swing.JScrollPane scrollPaneAntwortA;
    private javax.swing.JScrollPane scrollPaneAntwortB;
    private javax.swing.JScrollPane scrollPaneAntwortC;
    private javax.swing.JScrollPane scrollPaneAntwortD;
    private javax.swing.JScrollPane scrollPaneAntwortE;
    private javax.swing.JScrollPane scrollPaneAntwortF;
    private javax.swing.JScrollPane scrollPaneAntwortG;
    private javax.swing.JScrollPane scrollPaneAntwortH;
    private javax.swing.JScrollPane scrollPaneFrage;
    private javax.swing.JTextArea textAreaAntwortA;
    private javax.swing.JTextArea textAreaAntwortB;
    private javax.swing.JTextArea textAreaAntwortC;
    private javax.swing.JTextArea textAreaAntwortD;
    private javax.swing.JTextArea textAreaAntwortE;
    private javax.swing.JTextArea textAreaAntwortF;
    private javax.swing.JTextArea textAreaAntwortG;
    private javax.swing.JTextArea textAreaAntwortH;
    private javax.swing.JTextArea textAreaFrage;
    private javax.swing.JTextField textFieldGeheZu;
    private javax.swing.JToggleButton toggleButtonMogeln;
    private javax.swing.JToggleButton toggleButtonWiedervorlage;
    // End of variables declaration//GEN-END:variables
}
