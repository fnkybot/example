import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**	Przyklad demonstrujacy uzycie komponentu JRadioButton
 *  RadioButtonDemo.java wymaga nastepujacych plikow:
 *   images/Bird.gif
 *   images/Cat.gif
 *   images/Dog.gif
 *   images/Rabbit.gif
 *   images/Pig.gif
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class RadioButtonDemo extends JPanel
                             implements ActionListener {
	/** napisy uzywane do generacji nazwy pliku z obrazkiem 
	 *	oraz do podpisania przyciskow */
    static String birdString = "Bird";
    static String catString = "Cat";
    static String dogString = "Dog";
    static String rabbitString = "Rabbit";
    static String pigString = "Pig";

	/** wyswietlany obrazek */
    JLabel picture;

	/** konstruktor
	 */
    public RadioButtonDemo() {    
    	//ustawienie rozkladu BorderLayout (konstruktor klasy JPanel)
        super(new BorderLayout());

        //utworzenie przycisku JRadioButton
        JRadioButton birdButton = new JRadioButton(birdString);
        //przypisanie skrotu klawiaturowego
        birdButton.setMnemonic(KeyEvent.VK_B);
        //przypisanie zdarzenia jakie ma byc generowane przez przycisk
        birdButton.setActionCommand(birdString);
        //wybranie przycisku
        birdButton.setSelected(true);

        JRadioButton catButton = new JRadioButton(catString);
        catButton.setMnemonic(KeyEvent.VK_C);
        catButton.setActionCommand(catString);

        JRadioButton dogButton = new JRadioButton(dogString);
        dogButton.setMnemonic(KeyEvent.VK_D);
        dogButton.setActionCommand(dogString);

        JRadioButton rabbitButton = new JRadioButton(rabbitString);
        rabbitButton.setMnemonic(KeyEvent.VK_R);
        rabbitButton.setActionCommand(rabbitString);

        JRadioButton pigButton = new JRadioButton(pigString);
        pigButton.setMnemonic(KeyEvent.VK_P);
        pigButton.setActionCommand(pigString);

        //zgrupowanie przyciskow
        ButtonGroup group = new ButtonGroup();
        group.add(birdButton);
        group.add(catButton);
        group.add(dogButton);
        group.add(rabbitButton);
        group.add(pigButton);

        //zarejestrowanie sluchacza (biezaca klasa)
        birdButton.addActionListener(this);
        catButton.addActionListener(this);
        dogButton.addActionListener(this);
        rabbitButton.addActionListener(this);
        pigButton.addActionListener(this);

        //przypisanie wlasciwego obrazka
        picture = new JLabel(createImageIcon("images/"
                                             + birdString
                                             + ".gif"));

        //ustawienie rozmiaru najwiekszego obrazka "na sztywno"
        //prawdziwy program powinien to obliczac        
        picture.setPreferredSize(new Dimension(177, 122));


        //ulozenie przyciskow w kolumnie
        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(birdButton);
        radioPanel.add(catButton);
        radioPanel.add(dogButton);
        radioPanel.add(rabbitButton);
        radioPanel.add(pigButton);

        add(radioPanel, BorderLayout.LINE_START);
        add(picture, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    /** nasluch dla przyciskow 
     *	@param e nadchodzace zdarzenie od przyciskow
     */
    public void actionPerformed(ActionEvent e) {
        //zmiana obrazka
        picture.setIcon(createImageIcon("images/"
                                        + e.getActionCommand()
                                        + ".gif"));
    }

    /**	metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = RadioButtonDemo.class.getResource(path);
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
        JFrame frame = new JFrame("RadioButtonDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        JComponent newContentPane = new RadioButtonDemo();
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
