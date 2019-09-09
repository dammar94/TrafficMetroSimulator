/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JRootPane;

/**
 * Questa classe è responsabile di tutte le iterazioni di input e output tramite
 * terminal tra l'utente è l'applicazione
 *
 * @author damiano
 */
public class Display {

    /**
     * Descrive lo stato corrente del dialogo fra l'utente e l'applicazione.
     */
    private int displayStatus = 0;
    /**
     * Indica se il display è attivo e pronto ad agire con l'utente.
     */
    public boolean active = false;
    public WorkSpace workSpace;

    /**
     * Costruttore che imposta anche il parametro active del display
     *
     * @param b parametro active del display
     */
    public Display(boolean b) {
        this.active = b;
    }

    /**
     * Mostra il messaggio introduttivo all'applicazione.
     */
    public void print_IntroMessage() {
        this.print_Line();
        System.out.println("Benvenuti in TrafficMetroSimulator");
    }

    /**
     * Mostra il messaggio di uscita dall'applicazione.
     */
    private void print_OutroMessage() {
        System.out.println("Grazie per aver scelto TrafficMetroSimulator");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Mostra il messaggio introduttivo all'applicazione.
     */
    public void print_NuovaWorkSpace_IntroMessage() {
        this.print_Line();
        System.out.println("Benvenuti nella procedura guidata alla creazione di una nuova Workspace.\n"
                + "Nella prima fase ci occuperemo di definire la rete dei trasporti sulla quale\n"
                + "navigheranno i mezzi; e nella seconda invece ci dedicheremo a tutti i dettagli necessari\n"
                + "all'avvio della prima simulazione. Una volta terminata questa procedura potrai modificare\n"
                + "ulteriormente in piena libertà il tuo ambiente di lavoro. Consigliamo di seguire questo\n"
                + "tutorial se non si è ancora a proprio agio con il programma. Ti ricordiamo inoltre che puoi\n"
                + "disattivare quando vuoi questa procedura dal menù Opzioni all'avvio dell'applicazione.\n"
                + "Cominciamo! Premere INVIO...\n");
        //breathe();
        getCh();
    }

    /**
     * Ferma l'evoluzione del display di un tempo standard in millisecondi
     */
    private void breathe() {
        try {
            Thread.sleep(650);
        } catch (InterruptedException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Legge il nome della WorkSpace da input di tastiera e crea l'oggetto
     * WorkSpace
     */
    private void read_NomeWorkSpace() {
        String nomeWorkSpace = null;
        String sure = "N";
        // richiede l'input se l'utente non da l'ok
        while (sure.toUpperCase().equals("N")) {
            //this.print_Line();
            System.out.println("Inserire il nome della WorkSpace:");
            Scanner in = new Scanner(System.in);
            nomeWorkSpace = in.nextLine();
            System.out.println("Il nome inserito è: " + nomeWorkSpace);
            breathe();
            System.out.println("Vuoi procedere? [S/N]");
            sure = in.nextLine();
            while (!sure.toUpperCase().equals("S") && !sure.toUpperCase().equals("N")) {
                System.out.println("ERRORE: Input non valido. \n");
                breathe();
                System.out.println("Il nome inserito è: " + nomeWorkSpace);
                System.out.println("Vuoi procedere? [S/N]");
                sure = in.nextLine();
            }
        }
        this.workSpace = new WorkSpace(nomeWorkSpace);
    }

    /**
     * Mostra le opzioni del menù principale.
     */
    public void print_MenuOptions() {
        this.print_IntroMessage();
        System.out.println(
                "1 - Crea nuova WorkSpace \n"
                + "2 - Carica WorkSpace \n"
                + "3 - Opzioni \n"
                + "4 - Esci \n"
                + "Selezionare una voce dal menù: ");
    }

    /**
     * Legge l'opzione del menù scelta dall'utente, e la richiede se l'input non
     * è valido.
     */
    public void read_MenuOption() {
        this.print_MenuOptions();
        Scanner in = new Scanner(System.in);
        String num = in.nextLine();
        // richiede l'input se non è valido
        while (!num.equals("1") && !num.equals("2") && !num.equals("3")
                && !num.equals("4")) {
            this.clearConsole(); //NON FUNZIONA
            System.out.println("ERRORE: selezionare una voce del menù valida.\n");
            breathe();
            this.print_MenuOptions();
            num = in.nextLine();
        }
        // imposta il nuovo displayStatus
        this.displayStatus = Integer.parseInt(num);
    }

    /**
     * Legge e gestisce tutti gli input dalla console necessari a definire un
     * nuovo grafo per la WorkSpace.
     */
    private void read_Graph() {
        print_Line();
        System.out.println("Passiamo ora a creare la rete dei trasporti, "
                + "la quale sarà composta da diverse linee che\n"
                + "noi definiremo ad una ad una. Premere INVIO per continuare...\n");
        getCh();
        ArrayList<ArrayList<String>> fermate = new ArrayList<>();
        String nomeLinea;
        String answer = "S";
        while (answer.toUpperCase().equals("S")) {
            System.out.println("Inserire il nome della linea numero "
                    + (fermate.size() + 1) + ":");
            Scanner in = new Scanner(System.in);
            nomeLinea = in.nextLine();
            ArrayList<String> fermateLinea = new ArrayList<>();
            fermateLinea.add(nomeLinea); //L'indice 0 è riservato al nome della linea.
            String elencoFermate;
            System.out.println("Inserire in ordine le fermate della linea avendo cura di separarle con un trattino '-'\n"
                    + "(Per esempio: Clodio-Piazzale San Giovanni-Repubblica-Colosseo)");
            elencoFermate = in.nextLine();
            //Manipolazioni sull'elenco delle fermate.
            String[] parts = elencoFermate.split("-");
            fermateLinea.addAll(Arrays.asList(parts));
            System.out.println("Salvare la linea? [S/N]");
            answer = in.nextLine();
            // Check su [S/N].
            while (!answer.toUpperCase().equals("S") && !answer.toUpperCase().equals("N")) {
                System.out.println("ERRORE: Input non valido. \n");
                breathe();
                //System.out.println("Il nome inserito è: " + nomeWorkSpace);
                System.out.println("Salvare la linea? [S/N]");
                answer = in.nextLine();
            }
            //Salva la linea se la risposta è S, non salva altrimenti.
            if (answer.toUpperCase().equals("S")) {
                fermate.add(fermateLinea);
            }
            System.out.println("Linee salvate: " + fermate.size() + ". Si vuole aggiungere un'altra linea? [S/N]");
            //System.out.println("Si vuole aggiungere un'altra linea? [S/N]");
            answer = in.nextLine();
            // Check su [S/N].
            while (!answer.toUpperCase().equals("S") && !answer.toUpperCase().equals("N")) {
                System.out.println("ERRORE: Input non valido. \n");
                breathe();
                //System.out.println("Il nome inserito è: " + nomeWorkSpace);
                System.out.println("Si vuole aggiungere un'altra linea? [S/N]");
                answer = in.nextLine();
            }
        }
        //Creazione del grafo dagli input ottenuti.
        System.out.println("Creazione del grafo in corso. Attendere...");
        breathe();
        breathe();
        workSpace.setFermate(fermate);
        GraphHolder graphHolder = new GraphHolder();
        graphHolder.generateGraphFromFermate(fermate);
        workSpace.setGraphHolder(graphHolder);
        System.out.println("Creazione del grafo completata. Vuoi visualizzare il grafo? [S/N]");
        Scanner in = new Scanner(System.in);
        answer = in.nextLine();
        // Check su [S/N].
        while (!answer.toUpperCase().equals("S") && !answer.toUpperCase().equals("N")) {
            System.out.println("ERRORE: Input non valido. \n");
            breathe();
            //System.out.println("Il nome inserito è: " + nomeWorkSpace);
            System.out.println("Vuoi visualizzare il grafo? [S/N]");
            answer = in.nextLine();
        }
        if (answer.toUpperCase().equals("S")) {
            graphHolder.displayGraph();
        }
    }
    
    /**
     * Legge e gestisce tutti gli input dalla console necessari a definire un
     * nuovo elenco di PassengerGenerator per la WorkSpace.
     */
    private void read_PassengersGenerators() {
        print_Line();
        System.out.println("Il SimulationEngine utilizza dei PassengersGenerator per simulare\n"
                + "l'afflusso di viaggiatori nella rete. Ogni PassengerGenerator è costituito\n"
                + "da: Una fermata di partenza per i viaggiatori, una fermata di arrivo, e una\n"
                + "frequenza di generazione (automaticamente i viaggiatori sceglieranno il percorso\n"
                + "più breve che li porti dal loro punto di generazione al punto di arrivo). Si\n"
                + "possono creare tutti i PassengersGenerator che si desidera per la simulazione.\n"
                + "E' inutile dire che una simulazione senza nemmeno un PassengerGenerator ha poco\n"
                + "senso di esistere. Cominciamo infatti a crearne alcuni. Premere INVIO per continuare...\n");
        getCh();
        String answer = "S";
        while(answer.toUpperCase().equals("S")) {
            int numeroPassengerGenerators = workSpace.getNumberOfPassengerGenerators();
            System.out.println("Creazione del PassengerGenerator numero " + (numeroPassengerGenerators) + "," );
            //Otteniamo la fermata di partenza.
            System.out.println("Scegliere la fermata di partenza inserendo l'ID corrispondente tra i seguenti");
            this.print_Elencofermate();
            System.out.println("Inserire l'ID:");
            Scanner in = new Scanner(System.in);
            String idFermataPartenza = in.nextLine();
            //otteniamo la fermata dall'input, altrimenti ERRORE
            String fermataPartenza = null;
            try {
                fermataPartenza = this.getFermataFromID(idFermataPartenza);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //Otteniamo la fermata di arrivo.
            System.out.println("Scegliere la fermata di arrivo inserendo l'ID corrispondente tra i seguenti");
            this.print_Elencofermate();
            System.out.println("Inserire l'ID:");
            Scanner in2 = new Scanner(System.in);
            String idFermataArrivo = in.nextLine();
            //otteniamo la fermata dall'input, altrimenti ERRORE
            String fermataArrivo = null;
            try {
                fermataArrivo = this.getFermataFromID(idFermataArrivo);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //Otteniamo la frequency di generazione.
            System.out.println("Inserire quanti viaggiatori generare ogni ora:");
            Scanner in3 = new Scanner(System.in);
            int frequency = in.nextInt();
            //Inviamo i dati alla workSpace ed eventualmente ripetiamo.
            workSpace.addNewPassengerGenerator(fermataPartenza, fermataArrivo, frequency);
            System.out.println("PassengerGenerator creato. Vuoi crearne un altro? [S/N]");
            Scanner in4 = new Scanner(System.in);
            // Check su [S/N].
            while (!answer.toUpperCase().equals("S") && !answer.toUpperCase().equals("N")) {
                System.out.println("ERRORE: Input non valido. \n");
                breathe();
                //System.out.println("Il nome inserito è: " + nomeWorkSpace);
                System.out.println("Vuoi crearne un altro? [S/N]");
                answer = in.nextLine();
            }
        }
    }

    /**
     * Pulisce lo schermo della console in tutti i sistemi operativi.
     */
    //NON FUNZIONA
    private final void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stampa a schermo una lineaa separatrice.
     */
    private final void print_Line() {
        System.out.println("---------------------------");
    }

    /**
     * Impone al Display di agire in base al suo displayStatus corrente.
     */
    public void act() {
        switch (displayStatus) {
            case 0:
                this.read_MenuOption();
                break;
            case 1:
                this.print_NuovaWorkSpace_IntroMessage();
                this.read_NomeWorkSpace();
                this.read_Graph();
                this.read_PassengersGenerators();
                break;
            case 4:
                this.print_OutroMessage();
                this.active = false;
                break;
        }

    }

    public static void main(String[] args) {
        Display display = new Display(true);
        //display.print_IntroMessage();
        while (display.active) {
            display.act();
        }
    }

    private void print_Elencofermate() {
        ArrayList<ArrayList<String>> fermate = workSpace.getFermate();
        for(int i=0; i<fermate.size(); i++){
            for(int j=0; j<fermate.get(i).size(); j++){
                if(j!=0){
                    System.out.println(fermate.get(i).get(j) + " -> "+i+"#"+j);
                }
            }
        }
    }

    /**
     * Ottiene una stringa col nome della fermata a partire da un ID del tipo
     * 3#12.
     * @param idFermata
     * @return 
     */
    private String getFermataFromID(String idFermata) {
        ArrayList<ArrayList<String>> fermate = workSpace.getFermate();
        String[] parts = idFermata.split("#");
        return fermate.get(Integer.parseInt(parts[0])).get(Integer.parseInt(parts[1]));
    }
    
    /**
     * Equivalente di getch() di C ma in Java.
     */
    public static void getCh() {  
        final JFrame frame = new JFrame();  
        synchronized (frame) {  
            frame.setUndecorated(true);  
            frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);  
            frame.addKeyListener(new KeyListener() {
                @Override 
                public void keyPressed(KeyEvent e) {  
                    synchronized (frame) {  
                        frame.setVisible(false);  
                        frame.dispose();  
                        frame.notify();  
                    }  
                }  
                @Override 
                public void keyReleased(KeyEvent e) {  
                }  
                @Override 
                public void keyTyped(KeyEvent e) {  
                }  
            });  
            frame.setVisible(true);  
            try {  
                frame.wait();  
            } catch (InterruptedException e1) {  
            }  
        }  
    }

}
