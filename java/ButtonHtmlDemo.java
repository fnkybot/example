import java.awt.*;
import java.awt.event.*;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

/** Przyklad demonstrujacy uzycie przyciskow JButton
 *	z formatowaniem napisow w jezyku HTML
 * 	ButtonHtmlDemo.java wymaga nastepujacych plikow:
 *   images/right.gif
 *   images/middle.gif
 *   images/left.gif
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class ButtonHtmlDemo extends JPanel
                            implements ActionListener {
    /** przycisk */
    protected JButton b1, b2, b3;

	/** konstruktor */
    public ButtonHtmlDemo() {
    	
    	//przygotowanie ikon
        ImageIcon leftButtonIcon = createImageIcon("images/right.gif");
        ImageIcon middleButtonIcon = createImageIcon("images/middle.gif");
        ImageIcon rightButtonIcon = createImageIcon("images/left.gif");
        
        //utworzenie przycisku opisanego w html z odpowiednia ikona
        b1 = new JButton("<html><center><b><u>D</u>isable</b><br>"
                         + "<font color=#ffffdd>middle button</font>", 
                         leftButtonIcon);
        //ustawienie fontu
        Font font = b1.getFont().deriveFont(Font.PLAIN);
        b1.setFont(font);
        //wycentrowanie napisu w pionie wzgledem ikony
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        //tekst pojawi sie w poziomie poprzedzajac ikone
        //dla naszych ustawien lokalnych LEADING odpowiada LEFT
        b1.setHorizontalTextPosition(AbstractButton.LEADING); 
        //ustawienie skrotu klawiszowego (ALT+D)
        b1.setMnemonic(KeyEvent.VK_D);
        //przycisk bedzie generowal zdarzenie "disable"
        b1.setActionCommand("disable");

		//podobnie jak wyzej
        b2 = new JButton("middle button", middleButtonIcon);
        b2.setFont(font);
        b2.setForeground(new Color(0xffffdd));
        b2.setVerticalTextPosition(AbstractButton.BOTTOM);
        b2.setHorizontalTextPosition(AbstractButton.CENTER);
        b2.setMnemonic(KeyEvent.VK_M);

		//jak wyzej
        b3 = new JButton("<html><center><b><u>E</u>nable</b><br>"
                         + "<font color=#ffffdd>middle button</font>", 
                         rightButtonIcon);
        b3.setFont(font);
        //domyslna orientacja tekstu: CENTER, TRAILING (RIGHT)
        b3.setMnemonic(KeyEvent.VK_E);
        b3.setActionCommand("enable");
        b3.setEnabled(false);

        //ustawienie biezacej klasy jako sluchacza dla klawiszy 1 i 3
        b1.addActionListener(this);
        b3.addActionListener(this);
		
		//dodanie podpowiedzi
        b1.setToolTipText("Click this button to disable the middle button.");
        b2.setToolTipText("This middle button does nothing when you click it.");
        b3.setToolTipText("Click this button to enable the middle button.");

        //dodanie komponentow do kontenera (tu jest nim klasa ButtonHtmlDemo)
        //z uzyciem domyslnego rozkladu FlowLayout
        add(b1);
        add(b2);
        add(b3);
    }
	
	/** metoda obslugujaca zdarzenie typu ActionEvent 
	 *	@param e nadchodzace zdarzenie ActionEvent
	 */
    public void actionPerformed(ActionEvent e) {
    	//podjecie akcji w zaleznosci od wygenerowanego zdarzenia
        if ("disable".equals(e.getActionCommand())) {
            b2.setEnabled(false);
            b1.setEnabled(false);
            b3.setEnabled(true);
        } else {
            b2.setEnabled(true);
            b1.setEnabled(true);
            b3.setEnabled(false);
        }
    }

    /** metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ButtonHtmlDemo.class.getResource(path);
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
        JFrame frame = new JFrame("ButtonHtmlDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//utworzenie i przygotowanie panelu
        ButtonHtmlDemo newContentPane = new ButtonHtmlDemo();
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
