import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;

/** Przyklad demonstrujacy uzycie paneli z warstwami JLayeredPane
 * 	LayeredPaneDemo2.java wymaga nastepujacych plikow:
 *	 images/dukeWaveRed.gif.
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class LayeredPaneDemo2 extends JPanel
                              implements ActionListener,
                                         MouseMotionListener {
    /** nazwy warstw */
    private String[] layerStrings = { "Yellow (0)", "Magenta (1)",
                                      "Cyan (2)",   "Red (3)",
                                      "Green (4)",  "Blue (5)" };
 	/** kolory warstw */                                     
    private Color[] layerColors = { Color.yellow, Color.magenta,
                                    Color.cyan,   Color.red,
                                    Color.green,  Color.blue };
	
	/** panel z warstwami */
    private JLayeredPane layeredPane;
    /** obrazek z Djukiem */
    private JLabel dukeLabel;
    /** przycisk do wlaczania Djuka na wierzchu warstwy */
    private JCheckBox onTop;
    /** wybor warstwy Djuka */
    private JComboBox layerList;

    /** komendy zdarzen akcji */
    private static String ON_TOP_COMMAND = "ontop";
    private static String LAYER_COMMAND = "layer";

	/** konstruktor bezparametryczny */
    public LayeredPaneDemo2()    {    	
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // utworzenie ikony Djuka 
        final ImageIcon icon = createImageIcon("images/dukeWaveRed.gif");

        //utworzenie i przygotowanie panelu z warstwami
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(300, 310));
        layeredPane.setBorder(BorderFactory.createTitledBorder(
                                    "Move the Mouse to Move Duke"));
		//sledzenie ruchow myszy                                    
        layeredPane.addMouseMotionListener(this);

        //dodanie 6 kolorowych etykiet do kolejnych warstw panelu
        layeredPane.setLayout(new GridLayout(2,3));
        for (int i = 0; i < layerStrings.length; i++) {
            JLabel label = createColoredLabel(layerStrings[i],
                                              layerColors[i]);
            layeredPane.add(label, i);
        }

        //utworzenie i dodanie obrazka z Djukiem do warstw panelu
        dukeLabel = new JLabel(icon);
        if (icon == null) {
            System.err.println("Duke icon not found; using black rectangle instead.");
            dukeLabel.setOpaque(true);
            dukeLabel.setBackground(Color.BLACK);
        }
        layeredPane.add(dukeLabel, 2, 0);

        //dodanie panelu z warstwami i panelu zawierajacego
        //komponenty sterujace do panelu biezacej klasy
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createControlPanel());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(layeredPane);
    }

    /** metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = LayeredPaneDemo2.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /** metoda tworzaca colorowa etykiete 
     *	@param text napis na etykiecie
     *	@param kolor etykiety
     *	@return utworzona etykieta 
     */
    private JLabel createColoredLabel(String text,
                                      Color color) {
        JLabel label = new JLabel(text);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.black);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setPreferredSize(new Dimension(140, 140));
        return label;
    }

    /** metoda tworzaca panel zawierajacy checkbox i combobox
     *	do wyboru polozenia i warstwy Djuka
     *	@return utworzony panel
     */
    private JPanel createControlPanel() {
        onTop = new JCheckBox("Top Position in Layer");
        onTop.setSelected(true);
        onTop.setActionCommand(ON_TOP_COMMAND);
        onTop.addActionListener(this);

        layerList = new JComboBox(layerStrings);
        layerList.setSelectedIndex(2);    //warstwa cyan 
        layerList.setActionCommand(LAYER_COMMAND);
        layerList.addActionListener(this);

        JPanel controls = new JPanel();
        controls.add(layerList);
        controls.add(onTop);
        controls.setBorder(BorderFactory.createTitledBorder(
                                 "Choose Duke's Layer and Position"));
        return controls;
    }

    /** obsluga zdarzenia przesuniecia myszy
     *	obrazek Djuka przesuwa sie za kursorem
     */
    public void mouseMoved(MouseEvent e) {
        dukeLabel.setLocation(e.getX()-dukeLabel.getWidth()/2,
                              e.getY()-dukeLabel.getHeight()/2);
    }
    
    /** brak obslugi przeciagania myszy */
    public void mouseDragged(MouseEvent e) {} 

    /**	obsluga zdarzen checkboxa i comboboxa 
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

		//przesuniecie Djuka na przod lub tyl warstwy
        if (ON_TOP_COMMAND.equals(cmd)) {
            if (onTop.isSelected())
                layeredPane.moveToFront(dukeLabel);
            else
                layeredPane.moveToBack(dukeLabel);

        //zmiana warstwy Djuka
        } else if (LAYER_COMMAND.equals(cmd)) {
            int position = onTop.isSelected() ? 0 : 1;
            layeredPane.setLayer(dukeLabel,
                                 layerList.getSelectedIndex(),
                                 position);
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
        JFrame frame = new JFrame("LayeredPaneDemo2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        JComponent newContentPane = new LayeredPaneDemo2();
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
