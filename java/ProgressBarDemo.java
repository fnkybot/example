import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**	przyklad uzycia komponentu JProgressBar
 * 	ProgressBarDemo.java wymaga nastepujacych plikow:
 *   LongTask.java
 *   SwingWorker.java
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class ProgressBarDemo extends JPanel
                             implements ActionListener {
	/** stala oznaczajaca jedna sekunde */                             	
    public final static int ONE_SECOND = 1000;

	/** pasek postepu obliczen */
    private JProgressBar progressBar;
    /** timer */
    private Timer timer;
    /** przycisk rozpoczynajacy obliczenia */
    private JButton startButton;
    /** obiekt wykonujacy czasochlonne obliczenia */
    private LongTask task;
    /** pole tekstowe do wyswietlania komunikatow zadania */
    private JTextArea taskOutput;
    /** znak konca linii */
    private String newline = "\n";

	/** konstruktor bezparametrowy 
	 */
    public ProgressBarDemo() {
        super(new BorderLayout());
        task = new LongTask();

        //utworzenie interfejsu dla aplikacji
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);

        progressBar = new JProgressBar(0, task.getLengthOfTask());
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
        taskOutput.setCursor(null); //kursor taki jak dla panelu
                                    //see bug 4851758

		//rozmieszczenie komponentow
        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(progressBar);
		
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //utworzenie timera generujacego zdarzenie po 1 sekundzie
        timer = new Timer(ONE_SECOND, new ActionListener() {
        	//nasluch dla zadarzenia od timera
            public void actionPerformed(ActionEvent evt) {
                //aktualizacja paska postepu
                progressBar.setValue(task.getCurrent());
                //wyswietl komunikat zadania
                String s = task.getMessage();                
                if (s != null) {
                    taskOutput.append(s + newline);
                    taskOutput.setCaretPosition(
                            taskOutput.getDocument().getLength());
                }
                //jezeli zakonczono zadanie                
                if (task.isDone()) {
                	//sygnal dzwiekowy
                    Toolkit.getDefaultToolkit().beep();
                    //zatrzymanie timera
                    timer.stop();
                    //mozna wlaczac kolejne zadanie
                    startButton.setEnabled(true);
                    //wylaczenie kursora oczekiwania
                    setCursor(null); 
                    //wyzerowanie paska zadan
                    progressBar.setValue(progressBar.getMinimum());
                }
            }
        });
    }

    /**	nasluch przycisku start, rozpoczecie wykonania zadania
     *	@param evt nadchodzace zdarzenie od przycisku     
     */
    public void actionPerformed(ActionEvent evt) {
    	//wylacz przycisk gdy zadanie w toku
        startButton.setEnabled(false);
        //zmiana kursora na czekajacy
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //wlaczenie zadania
        task.go();
        //aktualizacja paska postepu co sekunde 
        timer.start();
    }

    /**	utworzenie i pokazanie GUI 
     *	dla zapewnienia bezpieczenstwa ponizsza metoda 
     *	powinna byc wywolywana z watku rozsylajacego zdarzenia
     */
    private static void createAndShowGUI() {        
        //ustawienie ladnego wygladu okien
        JFrame.setDefaultLookAndFeelDecorated(true);

        //utworzenie i przygotowanie okna
        JFrame frame = new JFrame("ProgressBarDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        JComponent newContentPane = new ProgressBarDemo();
        //panel niewidoczny
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);

        //wyswietlenie okna
        frame.pack();
        frame.setVisible(true);
    }

	/** metoda glowna 
	 *	@param args pomijany
	 */
    public static void main(String[] args) {
       	//zadaniem watku rozsylajacego zdarzenia bedzie 
        //utworzenie GUI aplikacji
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
