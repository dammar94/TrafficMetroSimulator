/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
        printlnWithLine("Benvenuti nella procedura guidata alla creazione di una nuova Workspace.\n"
                + "Una volta terminata questa procedura potrai modificare ulteriormente e in piena libertà\n"
                + "il tuo ambiente di lavoro. Consigliamo di seguire questo tutorial se non si è ancora\n"
                + "a proprio agio con il programma. Ti ricordiamo inoltre che puoi disattivare quando vuoi\n"
                + "questa procedura dal menù Opzioni all'avvio dell'applicazione.");
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
            println("Inserire il nome della WorkSpace:");
            Scanner in = new Scanner(System.in);
            nomeWorkSpace = in.nextLine();
            println("Il nome inserito è: " + nomeWorkSpace);
            println("Vuoi procedere? [S/N]");
            sure = in.nextLine();
            while (!sure.toUpperCase().equals("S") && !sure.toUpperCase().equals("N")) {
                println("ERRORE: Input non valido. \n");
                println("Il nome inserito è: " + nomeWorkSpace);
                println("Vuoi procedere? [S/N]");
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
        printlnWithLine("Passiamo ora a creare la rete dei trasporti, "
                + "la quale sarà composta da diverse linee che\n"
                + "definiremo ad una ad una.");
        ArrayList<ArrayList<String>> fermate = new ArrayList<>();
        String nomeLinea;
        String answer = "S";
        while (answer.toUpperCase().equals("S")) {
            println("Inserire il nome della linea numero "
                    + (fermate.size() + 1) + ":");
            Scanner in = new Scanner(System.in);
            nomeLinea = in.nextLine();
            ArrayList<String> fermateLinea = new ArrayList<>();
            fermateLinea.add(nomeLinea); //L'indice 0 è riservato al nome della linea.
            String elencoFermate;
            println("Inserire in ordine le fermate della linea avendo cura di separarle con un trattino '-'\n"
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
            println("ERRORE: Input non valido. \n");
            println(question);
            answer = in.nextLine();
        }
        return answer;
    }

    /**
     * Legge e gestisce tutti gli input daconsole necessari a definire un
     * nuovo elenco di PassengerGenerator per la WorkSpace.
     */
    private void read_PassengerGenerators() {
        printlnWithLine("Il SimulationEngine utilizza dei PassengerGenerator per simulare\n"
                + "l'afflusso di viaggiatori nella rete. Ogni PassengerGenerator è costituito\n"
                + "da una fermata di partenza per i viaggiatori, una fermata di arrivo, e una\n"
                + "frequenza di generazione (automaticamente i viaggiatori sceglieranno il percorso\n"
                + "più breve che li porti dal loro punto di generazione al punto di arrivo). Si\n"
                + "possono creare tutti i PassengerGenerator che si desidera per la simulazione.\n"
                + "E' inutile dire che una simulazione senza nemmeno un PassengerGenerator ha poco\n"
                + "senso di esistere. Cominciamo a crearne alcuni.");
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
            println("Creazione del PassengerGenerator numero " + (numeroPassengerGenerators) + ":");
            //Otteniamo la fermata di partenza.
            println("Scegliere la fermata di partenza e di arrivo inserendo l'ID corrispondente tra i seguenti");
            String fermataPartenza = null;
            String fermataArrivo = null;
            int frequency = 0;
            this.print_Elencofermate(elencoID);
            boolean repeat;
            do {
                repeat = false;
                println("Inserire l'ID della fermata di partenza:");
                Scanner in = new Scanner(System.in);
                String idFermataPartenza = in.nextLine();
                //otteniamo la fermata dall'input, altrimenti ERRORE
                try {
                    fermataPartenza = elencoID.get(Integer.parseInt(idFermataPartenza));
                } catch (Exception e) {
                    //e.printStackTrace();
                    println("ERRORE: Input non valido. \n");
                    repeat = true;
                }
            } while (repeat);
            //Otteniamo la fermata di arrivo.
            do {
                repeat = false;
                println("Inserire l'ID della fermata di arrivo:");
                Scanner in2 = new Scanner(System.in);
                String idFermataArrivo = in2.nextLine();
                //otteniamo la fermata dall'input, altrimenti ERRORE
                try {
                    fermataArrivo = elencoID.get(Integer.parseInt(idFermataArrivo));
                } catch (Exception e) {
                    //e.printStackTrace();
                    println("ERRORE: Input non valido. \n");
                    repeat = true;
                }
            } while (repeat);
            //Otteniamo la frequency di generazione.
            do {
                repeat = false;
                println("Inserire quanti viaggiatori generare ogni ora:");
                Scanner in3 = new Scanner(System.in);
                try {
                    frequency = in3.nextInt();
                } catch (Exception e) {
                    //e.printStackTrace();
                    println("ERRORE: Input non valido. \n");
                    repeat = true;
                }
            } while (repeat);
            //Inviamo i dati alla workSpace ed eventualmente ripetiamo.
            workSpace.addNewPassengerGenerator(fermataPartenza, fermataArrivo, frequency);
            println("PassengerGenerator creato. Vuoi crearne un altro? [S/N]");
            answer = getYesOrNoWithCheck("Vuoi crearne un altro? [S/N]");
        }
    }
    
    /**
     * Legge e gestisce tutti gli input da console necessari a definire i campi 
     * cadency e capacity di ogni tipologia di Transport fatto partire durante 
     * la simulazione.
     */
    private void read_TransportParameters() {
        printlnWithLine("Tieni duro, Abbiamo quasi fatto, rimane soltanto una cosa da fare:\n"
                + "definire i parametri necessari al funzionamento dei Transport nella rete,\n"
                + "ovvero gli unici veri responsabili dello smaltimento dei viaggiatori. Ogni\n"
                + "tipologia di Transport è relativa a una linea e a una direzione, e per ogni\n"
                + "tipologia vanno definiti i parametri cadency e capacity, ovvero il ritmo\n"
                + "con cui i Transport partono dall'estremo e la capacità massima del mezzo\n"
                + "(un Transport non potrà portare più Passenger della sua capacity, inoltre\n"
                + "più sono i viaggiatori all'interno del mezzo più questo ne risentirà in\n"
                + "termini di performance). Iniziamo a definire questi parametri.");
        ArrayList<ArrayList<String>> linee = workSpace.getFermate();
        //Per ogni linea prendiamo in input i parametri necessari due volte, una per ogni direzione.
        for(int i=0; i<linee.size(); i++){ //INVALID INPUT BUG.
            ArrayList<String> fermateLinea = linee.get(i); 
            //Prima direzione
            println("Definizione parametri linea '" + fermateLinea.get(0) + "' direzione '" + fermateLinea.get(1) + "'");
            println("Inserire ogni quanti minuti far partire i Transport:");
            Scanner in = new Scanner(System.in);
            int cadency = in.nextInt();
            println("Inserire la capacità massima dei Transport:");
            Scanner in2 = new Scanner(System.in);
            int capacity = in2.nextInt();
            //Passiamo i dati alla WorkSpace
            workSpace.addNewTransportStencil(fermateLinea.get(0), fermateLinea.get(1), cadency, capacity);
            //Seconda direzione
            int size = fermateLinea.size();
            println("Definizione parametri linea '" + fermateLinea.get(0) + "' direzione '" + fermateLinea.get(size-1) + "'");
            println("Inserire ogni quanti minuti far partire i Transport:");
            in = new Scanner(System.in);
            cadency = in.nextInt();
            println("Inserire la capacità massima dei Transport:");
            in2 = new Scanner(System.in);
            capacity = in2.nextInt();
            //Passiamo i dati alla WorkSpace
            workSpace.addNewTransportStencil(fermateLinea.get(0), fermateLinea.get(size-1), cadency, capacity);
        }
    }
    
    private void print_ProcedureComplete() {
        println("Completamento della WorkSpace in corso...");
        this.displayStatus = 10; //Spostiamoci nel pannello di controllo della WorkSpace.
        breathe();breathe();breathe();
        System.out.println("Complimenti, hai portato a termine la procedura guidata. Verrai ora reinderizzato al\n"
                + "pannello di controllo della WorkSpace dove potrai modificare i parametri da\n"
                + "te inseriti e anche altri che non hai ancora visto, ma che\n"
                + "può essere interessante tenere sotto controllo. Puoi già da adesso far\n"
                + "girare la tua prima simulazione di traffico senza problemi selezionando l'apposita\n"
                + "voce dal menu successivo. Buon divertimento!");
        breathe();
        System.out.println("Premere INVIO per continuare...");
        this.waitForKeyPressed();
    }

    /**
     * Scorciatoia per il metodo System.out.println() con annessa una piccola pausa
     * prima della stampa a video.
     * @param string 
     */
    private void println(String string) {
        breathe();
        System.out.println(string);
    }

    /**
     * Stampa a schermo una lineaa separatrice.
     */
    private void print_Line() {
        System.out.println("---------------------------");
        breathe();
    }
    
    /**
     * Versione più elaborata del metodo println() con una linea prima separatrice
     * dal discorso precedente e un waitForKeyPressed() a concludere.
     * @param string 
     */
    private void printlnWithLine(String string) {
        print_Line();
        System.out.println(string);
        System.out.println("Premere INVIO per continuare...");
        this.waitForKeyPressed();
    }

    /**
     * Impone al Display di agire in base al suo displayStatus corrente.
     */
    public void act() {
        switch (displayStatus) {
            //Menù iniziale.
            case 0:
                this.read_MenuOption();
                break;
            //Nuova WorkSpace.
            case 1:
                //Procedura guidata alla creazione di una nuova WorkSpace.
                this.print_NuovaWorkSpace_IntroMessage();
                this.read_NomeWorkSpace();
                this.read_Graph();
                this.read_PassengerGenerators();
                this.read_TransportParameters();
                this.print_ProcedureComplete();
                break;
            //Carica WorkSpace.
            case 2:
                this.read_CaricaWorkSpace();
                break;
            //Esci.
            case 4:
                this.print_OutroMessage();
                this.active = false;
                break;
            //Pannello di controllo WorkSpace
            case 10:
                this.read_WorkSpaceMenuOption();
                break;
            //Salva WorkSpace
            case 16:
                this.print_SalvaWorkSpace();
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
    
    //STAVI IMPLEMENTANDO QUESTO
    /**
     * Legge l'opzione del pannello di controllo della WorkSpace scelta dall'utente.
     */
    private void read_WorkSpaceMenuOption() {
        println("Nome WorkSpace: " + workSpace.getNomeWorkSpace() + " | Data Creazione: " + 
                workSpace.getDataCreazione() + " | Ultimo Salvataggio: " + 
                workSpace.getDataUltimoSalvataggio());
        System.out.println(
                "1 - Avvia SimulationEngine\n"
                + "2 - Graph\n"
                + "3 - Transports\n"
                + "4 - PassengerGenerators\n"
                + "5 - Preferenze di Simulazione\n"
                + "6 - Salva WorkSpace\n"
                + "7 - Esci");
        breathe();
        System.out.println("Selezionare una voce dal menù: ");
        Scanner in = new Scanner(System.in);
        String num = in.nextLine();
        // richiede l'input se non è valido
        while (!num.equals("1") && !num.equals("2") && !num.equals("3")
                && !num.equals("4") && !num.equals("5") && !num.equals("6")
                && !num.equals("7")) {
            this.clearConsole(); //NON FUNZIONA
            System.out.println("ERRORE: Selezionare una voce del menù valida.\n");
            breathe();
            this.print_MenuOptions();
            num = in.nextLine();
        }
        // imposta il nuovo displayStatus
        this.displayStatus = Integer.parseInt(num) + 10;
    }

    private void print_SalvaWorkSpace() {
        println("Salvataggio WorkSpace in corso...");
        try {
            workSpace.saveInHardDrive();
        } catch (FileNotFoundException e) {
            System.out.println("ERRORE: File non trovato.\n");
        } catch (IOException e) {
            System.out.println("ERRORE: Stream non inizializzato.\n");
        }
        println("WorkSpace salvata.");
        //Torniamo al pannello di controllo
        this.displayStatus = 10;
    }

    /**
     * Coordina tutte le operazioni di input e output su console per caricare 
     * una WorkSpace scelta dall'utente precedentemente salvata selezionandola 
     * da una lista.
     */
    private void read_CaricaWorkSpace() {
        File [] files = WorkSpaceLoader.getListFiles();
        println("Elenco WorkSpace salvate:");
        for (int i=0; i<files.length; i++) {
            System.out.println(i + " - " + files[i]);
        }
        println("Selezionare una WorkSpace: ");
        Scanner in = new Scanner(System.in);
        int choice = in.nextInt();
        try {
            this.workSpace = WorkSpaceLoader.loadWorkSpaceFromFile(files[choice]);
        } catch (FileNotFoundException e) {
            System.out.println("ERRORE: File non trovato.\n");
        } catch (IOException e) {
            System.out.println("ERRORE: Stream non inizializzato.\n");
        } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

}
