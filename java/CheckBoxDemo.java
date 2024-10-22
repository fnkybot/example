import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Przyklad demonstrujacy uzycie komopnentu JCheckBox	
 * 	CheckBoxDemo.java wymaga 16 plikow graficznych
 *	w katalogu images/geek: 
 * 	geek-----.gif, geek-c---.gif, geek--g--.gif, geek---h-.gif, geek----t.gif,
 * 	geek-cg--.gif, ..., geek-cght.gif.
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class CheckBoxDemo extends JPanel
                          implements ItemListener {
                          	
	/** przycisk wyboru dla atrybutow */
    JCheckBox chinButton,glassesButton, hairButton, teethButton;

    /**	4 atrybuty daja mozliwosc wykorzystania 16 kombinacji,
     * 	obraz dla kazdej kombinacji jest przechowywany w oddzielnym
     *	pliku, ktorego nazwa odpowiada atrybutom wygladu 
     *	wyswietlanego chlopka,
     *	nazwa kazdego pliku to "geek-XXXX.gif", gdzie 
     *	XXXX oznacza dana kombinacje <br><br>
     
       ----             //brak atrybutow<br><br>

       c---             //1 atrybut<br>
       -g--<br>
       --h-<br>
       ---t<br><br>

       cg--             //2 atrybuty<br>
       c-h-<br>
       c--t<br>
       -gh-<br>
       -g-t<br>
       --ht<br>

       -ght             //3 atrybuty<br>
       c-ht<br>
       cg-t<br>
       cgh-<br><br>

       cght             //wszystkie atrybuty<br><br>
       
     *	aktualnie wybrana kombinacja jest przechowywana 
     *	przez obiekt choices klasy StringBuffer 
     *	wykorzystywany do generowania nazwy wyswietlanego pliku    
     */     
    StringBuffer choices;
    
    /** obrazek */
    JLabel pictureLabel;

	/** konstruktor */
    public CheckBoxDemo() {
    	//ustawienie rozkladu BorderLayout (konstruktor nadklasy JPanel)
        super(new BorderLayout());

        //utworzenie checkbox'a
        //ustawienie skrotu klawiaturowego
        //zaznaczenie jako wybranego
        chinButton = new JCheckBox("Chin");
        chinButton.setMnemonic(KeyEvent.VK_C);
        chinButton.setSelected(true);

        glassesButton = new JCheckBox("Glasses");
        glassesButton.setMnemonic(KeyEvent.VK_G);
        glassesButton.setSelected(true);

        hairButton = new JCheckBox("Hair");
        hairButton.setMnemonic(KeyEvent.VK_H);
        hairButton.setSelected(true);

        teethButton = new JCheckBox("Teeth");
        teethButton.setMnemonic(KeyEvent.VK_T);
        teethButton.setSelected(true);
		
		//rejestracja biezacej klasy jako sluchacza 
        chinButton.addItemListener(this);
        glassesButton.addItemListener(this);
        hairButton.addItemListener(this);
        teethButton.addItemListener(this);

        //okreslenie atrybutow wyswietlanego chlopka
        choices = new StringBuffer("cght");

        //ustawienie obrazka
        pictureLabel = new JLabel();
        pictureLabel.setFont(pictureLabel.getFont().deriveFont(Font.ITALIC));
        //zaladowanie wlasciwego obrazka
        updatePicture();

        //ustawienie checkbox'ow w kolumnie
        JPanel checkPanel = new JPanel(new GridLayout(0, 1));
        checkPanel.add(chinButton);
        checkPanel.add(glassesButton);
        checkPanel.add(hairButton);
        checkPanel.add(teethButton);

        add(checkPanel, BorderLayout.LINE_START);
        add(pictureLabel, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    /** metoda nasluchujaca zdarzenia pochodzace od
     *	checkbox'ow 
     *	@param e nadchodzace zdarzenie ItemEvent 
     */
    public void itemStateChanged(ItemEvent e) {
        int index = 0;
        char c = '-';
        
        //source okresla jaki obiekt wygenerowal zdarzenie
        Object source = e.getItemSelectable();
        
		//w zaleznosci od zrodla zdarzenia
		//przyjecie litery okreslajacej atrybut
        if (source == chinButton) {
            index = 0;
            c = 'c';
        } else if (source == glassesButton) {
            index = 1;
            c = 'g';
        } else if (source == hairButton) {
            index = 2;
            c = 'h';
        } else if (source == teethButton) {
            index = 3;
            c = 't';
        }
        
        //jezeli przycisk zostal odznaczony mozna litere usunac
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            c = '-';
        }

        //wprowadzenie zmiany w lancuchu atrybutow
        choices.setCharAt(index, c);
		//zaladowanie wlasciwego obrazka
        updatePicture();
    }

	/** metoda ladujaca z pliku wlasciwy obrazek 
	 */
    protected void updatePicture() {
        //zaladowanie wlasciwej ikony
        ImageIcon icon = createImageIcon(
                                    "images/geek/geek-"
                                    + choices.toString()
                                    + ".gif");
        pictureLabel.setIcon(icon);
        //podpowiedz obrazka
        pictureLabel.setToolTipText(choices.toString());
        //zabezpieczenie przed brakiem pliku z obrazkiem
        if (icon == null) {
            pictureLabel.setText("Missing Image");
        } else {
            pictureLabel.setText(null);
        }
    }

    /**  metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka dostepu do pliku
     *	@return ImageIcon lub null gdy podana zla sciezke 
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CheckBoxDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

  /**	utworzenie i pokazanie GUI 
     *	dla zapewnienia bezpieczenstwa ponizsza metoda 
     *	powinna byc wywolywana z watku rozsylajacego zdarzenia
     */
    private static void createAndShowGUI() {        
        //ustawienie ladnego wygladu okien
        JFrame.setDefaultLookAndFeelDecorated(true);

		//utworzenie i przygotowanie okna
        JFrame frame = new JFrame("CheckBoxDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        JComponent newContentPane = new CheckBoxDemo();
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
