import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

/** przyklad uzycia komponentu JTabbedPane
 *	TabbedPaneDemo.java wymaga nastepujacych plikow:
 *   images/middle.gif
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class TabbedPaneDemo extends JPanel {
    public TabbedPaneDemo() {
    	//rozklad siatkowy
        super(new GridLayout(1, 1));
		
		//panel z zakladkami
        JTabbedPane tabbedPane = new JTabbedPane();
        //ikona z pliku
        ImageIcon icon = createImageIcon("images/middle.gif");

		//utworzenie przykladowego panelu z napisem i dodanie go 
		//do przedzialu z uzyciem skrotu klawiaturowego ALT+1
        JComponent panel1 = makeTextPanel("Panel #1");
        tabbedPane.addTab("Tab 1", icon, panel1,
                          "Does nothing");//podpowiedz
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("Tab 2", icon, panel2,
                          "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = makeTextPanel("Panel #3");
        tabbedPane.addTab("Tab 3", icon, panel3,
                          "Still does nothing");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent panel4 = makeTextPanel(
                "Panel #4 (has a preferred size of 410 x 50).");
		//preferowany rozmiar
		panel4.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Tab 4", icon, panel4,
                          "Does nothing at all");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        //dodanie panelu z przedzialami do panelu biezacej klasy
        add(tabbedPane);
                
        //odkomentowanie ponizszej linii spowoduje wlaczenie
        //przyciskow do przewijania zakladek przedzialow
        //tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

	/** metoda tworzaca panel zawierajacy napis
	 *	@param text napis do wyswietlenia w panelu
	 *	@return utworzony panel
	 */
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /** metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TabbedPaneDemo.class.getResource(path);
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
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        JComponent newContentPane = new TabbedPaneDemo();
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