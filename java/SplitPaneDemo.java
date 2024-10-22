import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

/** Przyklad demonstrujacy uzycie komponentow JSplitPane
 *	SplitPaneDemo.java wymaga nastepujacych plikow:
 *	 imagenames.properties i wymienione w nim obrazki
 *	klasa SplitPaneDemo nie jest komponentem graficznym
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class SplitPaneDemo implements ListSelectionListener {
	
    /** wektora zawierajacy nazwy obrazkow */
    private Vector imageNames;
    /** JLabel zawierajacy pokazywany obrazek */
    private JLabel picture;
    /** lista obrazkow do wyboru */
    private JList list;
    /** "panel podzielny" */
    private JSplitPane splitPane;

    /** konstruktor bezparametryczny */
    public SplitPaneDemo() {
        //wczytanie nazw obrazkow z pliku imagenames.properties
        ResourceBundle imageResource;
        try {
        	//odczytanie nazw z pliku do wektora
            imageResource = ResourceBundle.getBundle("imagenames");
            String imageNamesString = imageResource.getString("images");
            imageNames = parseList(imageNamesString);
        } catch (MissingResourceException e) {
            handleMissingResource(e);
        }

        //utworzenie listy zawierajacej nazwy obrazkow 
        //i dodanie jej do panelu z suwakami
        list = new JList(imageNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(list);

        //przygotowanie obrazka i dodanie do panelu 
        ImageIcon firstImage = createImageIcon("images/" +
                                     (String)imageNames.firstElement());
        if (firstImage != null) {
            picture = new JLabel(firstImage);
        } else {
            picture = new JLabel((String)imageNames.firstElement());
        }
        JScrollPane pictureScrollPane = new JScrollPane(picture);

        //utworzenie obiektu JSplitPane i dodanie do niego 
        //powyzszych paneli
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   listScrollPane, pictureScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        //ustawienie rozmiarow minimalnych dla komponentow
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        pictureScrollPane.setMinimumSize(minimumSize);

        //ustawienie rozmiaru preferowanego
        splitPane.setPreferredSize(new Dimension(400, 200));
    }

    /** metoda uzywana przez SplitPaneDemo2
	 *	@return lista obrazkow
	 */
    public JList getImageList() {
        return list;
    }

	/** metoda uzywana przez SplitPaneDemo2
	 *	@return panel splitPane 
	 */
    public JSplitPane getSplitPane() {
        return splitPane;
    }

	/** obsluga zdarzenia zmiany wybranego elementu w liscie 
	 *	@param e zdarzenie
	 */
    public void valueChanged(ListSelectionEvent e) {
    	//nie obsluguj gdy zdarzenie jest jednym z serii wielu
        if (e.getValueIsAdjusting())
            return;

		//dostep do aktualnie wybranego elementu i ustawienie
		//odpowiedniego obrazka i tekstu
        JList theList = (JList)e.getSource();
        if (theList.isSelectionEmpty()) {
            picture.setIcon(null);
            picture.setText(null);
        } else {
            int index = theList.getSelectedIndex();
            ImageIcon newImage = createImageIcon("images/" +
                                     (String)imageNames.elementAt(index));
            picture.setIcon(newImage);
            if (newImage != null) {
                picture.setText(null);
            } else {
                picture.setText("Image not found: "
                                + (String)imageNames.elementAt(index));
            }
        }
    }

	/** metoda rozdzielajaca nazwy plikow z obrazkami rozdzielone spacja
	 *	na osobne napisy
	 *	@return wektor napisow zawierajacy nazwy obrazkow
	 *	@param theStringList napis zawierajacy nazwy plikow obrazkow
	 *	rozdzielone spacja
	 */
    protected static Vector parseList(String theStringList) {
        Vector v = new Vector(10);
        StringTokenizer tokenizer = new StringTokenizer(theStringList, " ");
        while (tokenizer.hasMoreTokens()) {
            String image = tokenizer.nextToken();
            v.addElement(image);
        }
        return v;
    }

    /** metoda wywolywana gdy nie mozna bylo odnalezc pliku
     *	z nazwami obrazkow 
     *	@param e wyjatek - brak pliku
     */
    private void handleMissingResource(MissingResourceException e) {
        System.err.println();
        System.err.println("Can't find the properties file " +
                           "that contains the image names.");
        System.err.println("Its name should be imagenames.properties, " +
                           "and it should");
        System.err.println("contain a single line that specifies " +
                           "one or more image");
        System.err.println("files to be found in a directory " +
                           "named images.  Example:");
        System.err.println();
        System.err.println("    images=Bird.gif Cat.gif Dog.gif");
        System.err.println();
        throw(e);  //propagacja wyjatku
        //mozna uzyc exit(1), ale wtedy znika konsola w usludze 
        //Jaba Web Start a w ten sposob mozna zobaczyc komunikaty
    }

    /** metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SplitPaneDemo.class.getResource(path);
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
        JDialog.setDefaultLookAndFeelDecorated(true);

        //utworzenie i przygotowanie okna aplikacji
        JFrame frame = new JFrame("SplitPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SplitPaneDemo splitPaneDemo = new SplitPaneDemo();
        frame.getContentPane().add(splitPaneDemo.getSplitPane());

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