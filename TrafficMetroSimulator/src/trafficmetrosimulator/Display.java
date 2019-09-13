/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
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
    private WorkSpace workSpace;

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
    private void print_IntroMessage() {
        //this.print_Line();
        System.out.println("Benvenuti in TrafficMetroSimulator");
        breathe();
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
    private void print_NuovaWorkSpace_IntroMessage() {
        this.print_Line();
//        System.out.println("Benvenuti nella procedura guidata alla creazione di una nuova Workspace.\n"
//                + "Nella prima fase ci occuperemo di definire la rete dei trasporti sulla quale\n"
//                + "navigheranno i mezzi; e nella seconda invece ci dedicheremo a tutti i dettagli necessari\n"
//                + "all'avvio della prima simulazione. Una volta terminata questa procedura potrai modificare\n"
//                + "ulteriormente in piena libertà il tuo ambiente di lavoro. Consigliamo di seguire questo\n"
//                + "tutorial se non si è ancora a proprio agio con il programma. Ti ricordiamo inoltre che puoi\n"
//                + "disattivare quando vuoi questa procedura dal menù Opzioni all'avvio dell'applicazione.\n"
//                + "Cominciamo!\n");
        System.out.println("Benvenuti nella procedura guidata alla creazione di una nuova Workspace.\n"
                + "Una volta terminata questa procedura potrai modificare ulteriormente e in piena libertà\n"
                + "il tuo ambiente di lavoro. Consigliamo di seguire questo tutorial se non si è ancora\n"
                + "a proprio agio con il programma. Ti ricordiamo inoltre che puoi disattivare quando vuoi\n"
                + "questa procedura dal menù Opzioni all'avvio dell'applicazione. Premere INVIO per continuare...");
        this.waitForKeyPressed();
    }

    /**
     * Ferma l'evoluzione del display di un tempo standard in millisecondi
     */
    private void breathe() {
        try {
            Thread.sleep(400);
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
            breathe();
            System.out.println("Inserire il nome della WorkSpace:");
            Scanner in = new Scanner(System.in);
            nomeWorkSpace = in.nextLine();
            breathe();
            System.out.println("Il nome inserito è: " + nomeWorkSpace);
            breathe();
            System.out.println("Vuoi procedere? [S/N]");
            sure = in.nextLine();
            while (!sure.toUpperCase().equals("S") && !sure.toUpperCase().equals("N")) {
                breathe();
                System.out.println("ERRORE: Input non valido. \n");
                breathe();
                System.out.println("Il nome inserito è: " + nomeWorkSpace);
                breathe();
                System.out.println("Vuoi procedere? [S/N]");
                sure = in.nextLine();
            }
        }
        this.workSpace = new WorkSpace(nomeWorkSpace);
    }

    /**
     * Mostra le opzioni del menù principale.
     */
    private void print_MenuOptions() {
        this.print_IntroMessage();
        System.out.println(
                "1 - Crea nuova WorkSpace \n"
                + "2 - Carica WorkSpace \n"
                + "3 - Opzioni \n"
                + "4 - Esci");
        breathe();
        System.out.println("Selezionare una voce dal menù: ");
    }

    /**
     * Legge l'opzione del menù scelta dall'utente, e la richiede se l'input non
     * è valido.
     */
    private void read_MenuOption() {
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
     * Legge e gestisce tutti gli input da console necessari a definire un
     * nuovo grafo per la WorkSpace.
     */
    private void read_Graph() {
        print_Line();
        System.out.println("Passiamo ora a creare la rete dei trasporti, "
                + "la quale sarà composta da diverse linee che\n"
                + "definiremo ad una ad una. Premere INVIO per continuare...");
        this.waitForKeyPressed();
        ArrayList<ArrayList<String>> fermate = new ArrayList<>();
        String nomeLinea;
        String answer = "S";
        while (answer.toUpperCase().equals("S")) {
            breathe();
            System.out.println("Inserire il nome della linea numero "
                    + (fermate.size() + 1) + ":");
            Scanner in = new Scanner(System.in);
            nomeLinea = in.nextLine();
            ArrayList<String> fermateLinea = new ArrayList<>();
            fermateLinea.add(nomeLinea); //L'indice 0 è riservato al nome della linea.
            String elencoFermate;
            breathe();
            System.out.println("Inserire in ordine le fermate della linea avendo cura di separarle con un trattino '-'\n"
                    + "(Per esempio: Clodio-Piazzale San Giovanni-Repubblica-Colosseo)");
            elencoFermate = in.nextLine();
            //Manipolazioni sull'elenco delle fermate.
            String[] parts = elencoFermate.split("-");
            fermateLinea.addAll(Arrays.asList(parts));
            breathe();
            System.out.println("Salvare la linea? [S/N]");
            answer = getYesOrNoWithCheck("Salvare la linea? [S/N]");
            //Salva la linea se la risposta è S, non salva altrimenti.
            if (answer.toUpperCase().equals("S")) {
                fermate.add(fermateLinea);
            }
            breathe();
            System.out.println("Linee salvate: " + fermate.size() + ". Si vuole aggiungere un'altra linea? [S/N]");
            answer = getYesOrNoWithCheck("Si vuole aggiungere un'altra linea? [S/N]");
        }
        //Creazione del grafo dagli input ottenuti.
        breathe();
        System.out.println("Creazione del grafo in corso. Attendere...");
        workSpace.setFermate(fermate);
        workSpace.generateGraph();
        //GraphHolder graphHolder = new GraphHolder();
        //graphHolder.generateGraphFromFermate(fermate);
        //workSpace.setGraphHolder(graphHolder);
        breathe();
        breathe();
        System.out.println("Creazione del grafo completata. Vuoi visualizzare il grafo? [S/N]");
        answer = getYesOrNoWithCheck("Vuoi visualizzare il grafo? [S/N]");
        if (answer.toUpperCase().equals("S")) {
            workSpace.displayGraph();
        }
    }

    private String getYesOrNoWithCheck(String question) {
        Scanner in = new Scanner(System.in);
        String answer = in.nextLine();
        // Check su [S/N].
        while (!answer.toUpperCase().equals("S") && !answer.toUpperCase().equals("N")) {
            breathe();
            System.out.println("ERRORE: Input non valido. \n");
            breathe();
            System.out.println(question);
            answer = in.nextLine();
        }
        return answer;
    }

    /**
     * Legge e gestisce tutti gli input daconsole necessari a definire un
     * nuovo elenco di PassengerGenerator per la WorkSpace.
     */
    private void read_PassengerGenerators() {
        print_Line();
        System.out.println("Il SimulationEngine utilizza dei PassengerGenerator per simulare\n"
                + "l'afflusso di viaggiatori nella rete. Ogni PassengerGenerator è costituito\n"
                + "da una fermata di partenza per i viaggiatori, una fermata di arrivo, e una\n"
                + "frequenza di generazione (automaticamente i viaggiatori sceglieranno il percorso\n"
                + "più breve che li porti dal loro punto di generazione al punto di arrivo). Si\n"
                + "possono creare tutti i PassengerGenerator che si desidera per la simulazione.\n"
                + "E' inutile dire che una simulazione senza nemmeno un PassengerGenerator ha poco\n"
                + "senso di esistere. Cominciamo a crearne alcuni. Premere INVIO per continuare...");
        this.waitForKeyPressed();
        //Creiamo la corrispondenza ID<->fermata necessaria in questa comunicazione utente<->applicazione.
        ArrayList<String> elencoID = new ArrayList<>();
        ArrayList<ArrayList<String>> fermate = workSpace.getFermate();
        for (int i = 0; i < fermate.size(); i++) {
            for (int j = 1; j < fermate.get(i).size(); j++) {
                if (!elencoID.contains(fermate.get(i).get(j))) {
                    elencoID.add(fermate.get(i).get(j));
                }
            }
        }
        //
        String answer = "S";
        while (answer.toUpperCase().equals("S")) {
            int numeroPassengerGenerators = workSpace.getNumberOfPassengerGenerators();
            breathe();
            System.out.println("Creazione del PassengerGenerator numero " + (numeroPassengerGenerators) + ":");
            breathe();breathe();
            //Otteniamo la fermata di partenza.
            System.out.println("Scegliere la fermata di partenza e di arrivo inserendo l'ID corrispondente tra i seguenti");
            String fermataPartenza = null;
            String fermataArrivo = null;
            int frequency = 0;
            this.print_Elencofermate(elencoID);
            breathe();
            boolean repeat;
            do {
                repeat = false;
                System.out.println("Inserire l'ID della fermata di partenza:");
                Scanner in = new Scanner(System.in);
                String idFermataPartenza = in.nextLine();
                //otteniamo la fermata dall'input, altrimenti ERRORE
                try {
                    fermataPartenza = elencoID.get(Integer.parseInt(idFermataPartenza));
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("ERRORE: Input non valido. \n");
                    repeat = true;
                    breathe();
                }
            } while (repeat);
            //Otteniamo la fermata di arrivo.
            breathe();
            do {
                repeat = false;
                System.out.println("Inserire l'ID della fermata di arrivo:");
                Scanner in2 = new Scanner(System.in);
                String idFermataArrivo = in2.nextLine();
                //otteniamo la fermata dall'input, altrimenti ERRORE
                try {
                    fermataArrivo = elencoID.get(Integer.parseInt(idFermataArrivo));
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("ERRORE: Input non valido. \n");
                    repeat = true;
                    breathe();
                }
            } while (repeat);
            //Otteniamo la frequency di generazione.
            breathe();
            do {
                repeat = false;
                System.out.println("Inserire quanti viaggiatori generare ogni ora:");
                Scanner in3 = new Scanner(System.in);
                try {
                    frequency = in3.nextInt();
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("ERRORE: Input non valido. \n");
                    repeat = true;
                    breathe();
                }
            } while (repeat);
            //Inviamo i dati alla workSpace ed eventualmente ripetiamo.
            workSpace.addNewPassengerGenerator(fermataPartenza, fermataArrivo, frequency);
            breathe();
            System.out.println("PassengerGenerator creato. Vuoi crearne un altro? [S/N]");
            answer = getYesOrNoWithCheck("Vuoi crearne un altro? [S/N]");
        }
    }
    
    /**
     * Legge e gestisce tutti gli input da console necessari a definire i campi 
     * cadency e capacity di ogni tipologia di Transport fatto partire durante 
     * la simulazione.
     */
    private void read_TransportParameters() {
        print_Line();
        System.out.println("Tieni duro, Abbiamo quasi concluso la procedura guidata, rimane soltanto "
                + "da definire i parametri necessari al funzionamento dei Transport nella rete, "
                + "ovvero gli unici veri responsabili dello smaltimento dei viaggiatori. Ogni"
                + "tipologia di Transport è relativa a una linea e a una direzione, e per ogni"
                + "tipologia vanno definiti i parametri cadency e capacity, ovvero il ritmo"
                + "con cui i Transport partono dall'estremo e la capacità massima del mezzo"
                + "(un Transport non potrà portare più Passenger della sua capacity, inoltre"
                + "più sono i viaggiatori all'interno del mezzo più questo ne risentirà in"
                + "termini di performance). Premere INVIO per continuare...");
        this.waitForKeyPressed();
    }

    /**
     * Stampa a schermo una lineaa separatrice.
     */
    private void print_Line() {
        System.out.println("---------------------------");
        breathe();
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
                this.read_PassengerGenerators();
                this.read_TransportParameters();
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

    private void print_Elencofermate(ArrayList<String> elencoID) {
        for (int i = 0; i < elencoID.size(); i++) {
            System.out.println(elencoID.get(i) + " -> " + i);
        }
    }
    
    /**
     * Pulisce lo schermo della console in tutti i sistemi operativi.
     */
    //NON FUNZIONA
    private void clearConsole() {
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
     * Mette il Display in pausa, in attesa che l'utente prema INVIO.
     */
    //Bug: il buffer non viene pulito.
    private void waitForKeyPressed() {
        try {
            System.in.read();
            //breathe();
        } catch (IOException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
