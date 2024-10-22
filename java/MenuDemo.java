import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;


/** przyklad implementacji menu, dzialanie elementow menu zapewnia 
 *	implementacja sluchaczy
 *	MenuDemo.java wymaga nastepujacych plikow:
 *   images/middle.gif
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class MenuDemo implements ActionListener, ItemListener {
    
    /** pole tekstowe reagujace na wybor elementow menu */
    JTextArea output;
    /** panel przewijalny */
    JScrollPane scrollPane;
    /** pomocniczy symbol nowej linii */
    String newline = "\n";

	/** metoda tworzaca i dodajaca elementy menu 
	 *	@return utworzony pasek menu 
	 */
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        //przycisk radiobutton jako element menu
        JRadioButtonMenuItem rbMenuItem;
        //przycisk checkbox jako element menu
        JCheckBoxMenuItem cbMenuItem;

        //utworzenie paska menu
        menuBar = new JMenuBar();

        //zbudowanie pierwszego menu
        menu = new JMenu("A Menu");        
        //skrot klawiatury ALT+A
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);

        //dodanie kilku elementow JMenuItem
        menuItem = new JMenuItem("A text-only menu item",
                                 KeyEvent.VK_T);
        //zamiast ponizszego uzyty konstruktor
        //menuItem.setMnemonic(KeyEvent.VK_T); 
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        menuItem.addActionListener(this);
        menu.add(menuItem);
		
		//z ikona
        ImageIcon icon = createImageIcon("images/middle.gif");
        menuItem = new JMenuItem("Both text and icon", icon);
        menuItem.setMnemonic(KeyEvent.VK_B);
        menuItem.addActionListener(this);
        menu.add(menuItem);
		
		//sama ikona
        menuItem = new JMenuItem(icon);
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.addActionListener(this);
        menu.add(menuItem);
		
		//grupa przyciskow radiobutton
        menu.addSeparator();
        ButtonGroup group = new ButtonGroup();
		
        rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Another one");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        menu.add(rbMenuItem);

        //przyciski checkbox
        menu.addSeparator();
        cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
        cbMenuItem.setMnemonic(KeyEvent.VK_C);
        cbMenuItem.addItemListener(this);
        menu.add(cbMenuItem);

        cbMenuItem = new JCheckBoxMenuItem("Another one");
        cbMenuItem.setMnemonic(KeyEvent.VK_H);
        cbMenuItem.addItemListener(this);
        menu.add(cbMenuItem);

        //podmenu
        menu.addSeparator();
        submenu = new JMenu("A submenu");
        submenu.setMnemonic(KeyEvent.VK_S);
		
		//elementy podmenu
        menuItem = new JMenuItem("An item in the submenu");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        submenu.add(menuItem);

        menuItem = new JMenuItem("Another item");
        menuItem.addActionListener(this);
        submenu.add(menuItem);
        menu.add(submenu);

        //drugie, puste menu w pasku
        menu = new JMenu("Another Menu");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");
        menuBar.add(menu);

        return menuBar;
    }

    /** metoda tworzaca panel aplikacji 
     *	@return utworzony panel
     */
    public Container createContentPane() {
        //utworzenie panelu
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

		//utworzenie przewijalnego pola tekstowego
        output = new JTextArea(5, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);

        //dodanie przewijalnego pola tekstowego do panelu
        contentPane.add(scrollPane, BorderLayout.CENTER);

        return contentPane;
    }

	/** metoda obslugujaca zdarzenie typu ActionEvent 
	 *	@param e nadchodzace zdarzenie ActionEvent
	 */
    public void actionPerformed(ActionEvent e) {
    	//zrodlo zdarzenia
        JMenuItem source = (JMenuItem)(e.getSource());
        //wypisanie co spowodowalo zdarzenie
        String s = "Action event detected."
                   + newline
                   + "    Event source: " + source.getText()
                   + " (an instance of " + getClassName(source) + ")";
        output.append(s + newline);
        //ustawienie karetki na koniec tekstu
        output.setCaretPosition(output.getDocument().getLength());
    }

    /** metoda obslugujaca zdarzenie typu ItemEvent 
     *	(generowane przez przyciski checkbox)
     *	@param e nadchodzace zdarzenie ItemEvent
	 */    
    public void itemStateChanged(ItemEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());
        String s = "Item event detected."
                   + newline
                   + "    Event source: " + source.getText()
                   + " (an instance of " + getClassName(source) + ")"
                   + newline
                   + "    New state: "
                   + ((e.getStateChange() == ItemEvent.SELECTED) ?
                     "selected":"unselected");
        output.append(s + newline);
        output.setCaretPosition(output.getDocument().getLength());
    }

    /** zwraca nazwe klasy bez informacji o pakiecie 
     *	@param o obiekt ktorego klase trzeba odczytac
     *	@return napis zawierajacy nazwe klasy
     */
    protected String getClassName(Object o) {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex+1);
    }

    /** metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MenuDemo.class.getResource(path);
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
        JFrame frame = new JFrame("MenuDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        MenuDemo demo = new MenuDemo();
        frame.setJMenuBar(demo.createMenuBar());
        frame.setContentPane(demo.createContentPane());

        //wyswietlenie okna
        frame.setSize(450, 260);
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