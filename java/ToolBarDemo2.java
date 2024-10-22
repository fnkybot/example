
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import java.net.URL;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Przyklad demonstrujacy uzycie komponentu JToolBar
 * 	ToolBarDemo2.java wymaga obecnosci Repozytorium Grafiki Java 
 *	Look and Feel (jlfgr-1_0.jar) w sciezce ClassPath
 *	w przypadku uruchamiania z linii polecen gdy plik jlfgr-1_0.jar 
 *	znajduje sie w podkatalogu jars nalezy wywolac:
 *
 *   java -cp .;jars/jlfgr-1_0.jar ToolBarDemo2 [Microsoft Windows]
 *   java -cp .:jars/jlfgr-1_0.jar ToolBarDemo2 [UNIX]
 *
 *	w przypadku niedzialania powyzszej komendy mozna uzyc cudzyslowu: 	
 *
 *   java -cp ".;jars/jlfgr-1_0.jar" ToolBarDemo2 [UNIX shell on Win32]
 *
 * <br>
 * <a href="http://developer.java.sun.com/developer/techDocs/hi/repository/">
 * http://developer.java.sun.com/developer/techDocs/hi/repository/</a>
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class ToolBarDemo2 extends JPanel
                          implements ActionListener {
	/** pole tekstowe */                          	
    protected JTextArea textArea;
    /** stala znaku nowej linii */
    protected String newline = "\n";
    /** stala napisowa komendy zdarzen */
    static final private String PREVIOUS = "previous";
    /** stala napisowa komendy zdarzen */
    static final private String UP = "up";
    /** stala napisowa komendy zdarzen */
    static final private String NEXT = "next";
    /** stala napisowa komendy zdarzen */
    static final private String SOMETHING_ELSE = "other";
    /** stala napisowa komendy zdarzen */
    static final private String TEXT_ENTERED = "text";

    public ToolBarDemo2() {
        super(new BorderLayout());

        //utworzenie paska toolbar
        JToolBar toolBar = new JToolBar("Still draggable");
        addButtons(toolBar);
        //jesli bedzie true to mozna pasek przeciagac po ekranie
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        //utworzenie pola tekstowego stosowanego jako wyjscie
        //rozmiar 5 wierszy i 30 kolumn
        textArea = new JTextArea(5, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //rozmieszczenie glownego panelu
        setPreferredSize(new Dimension(450, 130));
        add(toolBar, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
    }

	/** dodanie przyciskow i pola tekstowego do paska toolbar
	 *	@param toolBar pasek do ktorego dodane beda komponenty
	 */
    protected void addButtons(JToolBar toolBar) {
        JButton button = null;

        //pierwszy przycisk
        button = makeNavigationButton("Back24", PREVIOUS,
                                      "Back to previous something-or-other",
                                      "Previous");
        toolBar.add(button);

        //drugi przycisk
        button = makeNavigationButton("Up24", UP,
                                      "Up to something-or-other",
                                      "Up");
        toolBar.add(button);

        //trzeci przycisk
        button = makeNavigationButton("Forward24", NEXT,
                                      "Forward to something-or-other",
                                      "Next");
        toolBar.add(button);

        //separator
        toolBar.addSeparator();

        //czwarty przycisk
        button = new JButton("Another button");
        button.setActionCommand(SOMETHING_ELSE);
        button.setToolTipText("Something else");
        button.addActionListener(this);
        toolBar.add(button);

        //a to nie przycisk tylko pole tekstowe! :)
        JTextField textField = new JTextField("A text field");
        textField.setColumns(10);
        textField.addActionListener(this);
        textField.setActionCommand(TEXT_ENTERED);
        toolBar.add(textField);
    }

	/** metoda tworzaca przycisk nawigacyjny z firmowym Look and Feel
	 *	@param imageName nazwa pliku z obrazkiem
	 *	@param actionCommand komenda przycisku
	 *	@param toolTipText podpowiedz nad przyciskiem
	 *	@param altText tekst zastepujacy obrazek
	 *	@return utworzony przycisk
	 */
    protected JButton makeNavigationButton(String imageName,
                                           String actionCommand,
                                           String toolTipText,
                                           String altText) {
        //znalezienie obrazka
        String imgLocation = "toolbarButtonGraphics/navigation/"
                             + imageName
                             + ".gif";
        URL imageURL = ToolBarDemo2.class.getResource(imgLocation);

        //Create and initialize the button.
        //utworzenie i inicjalizacja przycisku
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        if (imageURL != null) {	//jest obrazek
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {				//brak obrazka
            button.setText(altText);
            System.err.println("Resource not found: "
                               + imgLocation);
        }

        return button;
    }

	/** obsluga zdarzen generowanych przez przyciski
	 *	oraz przez pole tekstowe 
	 */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String description = null;

        // obsluga zdarzenia dla kazdego przycisku
        if (PREVIOUS.equals(cmd)) { //pierwszy przycisk wcisniety
            description = "taken you to the previous <something>.";
        } else if (UP.equals(cmd)) { //drugi przycisk wcisniety
            description = "taken you up one level to <something>.";
        } else if (NEXT.equals(cmd)) { //trzeci przycisk wcisniety
            description = "taken you to the next <something>.";
        } else if (SOMETHING_ELSE.equals(cmd)) { //czwarty przycisk wcisniety
            description = "done something else.";
        } else if (TEXT_ENTERED.equals(cmd)) { //pole tekstowe
            JTextField tf = (JTextField)e.getSource();
            String text = tf.getText();
            tf.setText("");
            description = "done something with this text: "
                          + newline + "  \""
                          + text + "\"";
        }

        displayResult("If this were a real app, it would have "
                        + description);
    }

    /** wypisanie komunikatu zwiazanego z zaistnialym zdarzeniem
     *	na pole tekstowe bedace wyjsciem aplikacji
     *	@param actionDescription krotki opis obslugi zdarzenia
     */
    protected void displayResult(String actionDescription) {
        textArea.append(actionDescription + newline);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**	utworzenie i pokazanie GUI 
     *	dla zapewnienia bezpieczenstwa ponizsza metoda 
     *	powinna byc wywolywana z watku rozsylajacego zdarzenia   
     */
    private static void createAndShowGUI() {
        //ustawienie ladnego wygladu okien
        JFrame.setDefaultLookAndFeelDecorated(true);

		//utworzenie i przygotowanie okna
        JFrame frame = new JFrame("ToolBarDemo2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        ToolBarDemo2 newContentPane = new ToolBarDemo2();
        //panel widoczny
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
