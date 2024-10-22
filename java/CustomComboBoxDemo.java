import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Przyklad uzycia komponentu JComboBox z klasa renderujaca.
 *  CustomComboBoxDemo.java wymaga nastepujacych plikow:
 *   images/Bird.gif
 *   images/Cat.gif
 *   images/Dog.gif
 *   images/Rabbit.gif
 *   images/Pig.gif
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class CustomComboBoxDemo extends JPanel {
	/** tablica obrazkow */
    ImageIcon[] images;
    /** napisy dla kazdego wyswietlanego zwierzaka */
    String[] petStrings = {"Bird", "Cat", "Dog", "Rabbit", "Pig"};

    /** konstruktor
     */
    public CustomComboBoxDemo() {
    	//ustawienie rozkladu BorderLayout (konstruktor klasy JPanel)
        super(new BorderLayout());
       
        //zaladowanie obrazkow i utworzenie tablicy indeksow
        images = new ImageIcon[petStrings.length];
        Integer[] intArray = new Integer[petStrings.length];
        for (int i = 0; i < petStrings.length; i++) {
            intArray[i] = i;
            images[i] = createImageIcon("images/" + petStrings[i] + ".gif");
            if (images[i] != null) {
                images[i].setDescription(petStrings[i]);
            }
        }

        //utworzenie combobox'a i przypisanie obiektu klasy renderujacej
        JComboBox petList = new JComboBox(intArray);
        ComboBoxRenderer renderer= new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(200, 130));        
        petList.setRenderer(renderer);
        petList.setMaximumRowCount(3);

        //rozmieszczenie elementow
        add(petList, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    /**	metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CustomComboBoxDemo.class.getResource(path);
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
        JFrame frame = new JFrame("CustomComboBoxDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        JComponent newContentPane = new CustomComboBoxDemo();
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


	/** wewnetrzna klasa renderujaca Combobox'a 
	 *	dziedziczy wlasciwosci klasy wyswietlajacej tekst i obrazki JLabel
	 *	oraz zapewnia mozliwosc wyswietlania komorek combobox'a	 
	 */
    class ComboBoxRenderer extends JLabel
                           implements ListCellRenderer {
		/** uzywany font */                           	
        private Font uhOhFont;

		/** konstruktor */
        public ComboBoxRenderer() {
            //wlaczenie widocznosci
            setOpaque(true);
            //wycentrowanie
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        /** metoda znajdujaca obrazek i tekst odpowiadajacy
         *	wybranej wartosci 
         *	@return Component wyswietlajacy wlasciwy obrazek z podpisem          
         *	@param list lista elementow do narysowania
         *	@param value wartosc list.getModel().getElementAt(index)
         *	@param index indeks komorki
         *	@param isSelected true gdy dana komorka zostala wybrana
         *	@param cellHasFocus true gdy dana komorka posiada focus
         */
        public Component getListCellRendererComponent(
                                           JList list,
                                           Object value,
                                           int index,
                                           boolean isSelected,
                                           boolean cellHasFocus) {            
            //otrzymanie indeksu wybranego elementu
            //poniewaz parametr index nie zawsze jest prawidlowy
            //nalezy uzyc parametru value
            int selectedIndex = ((Integer)value).intValue();

            //ustaw tla odpowiednie dla zaznaczonych 
            // i niezaznaczonych elementow (takie jak ustawienia z listy)
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            
            //ustaw ikone i podpis, powiadom jesli ikona byla null 
            ImageIcon icon = images[selectedIndex];
            String pet = petStrings[selectedIndex];
            setIcon(icon);
            if (icon != null) {
                setText(pet);
                setFont(list.getFont());
            } else {
                setUhOhText(pet + " (no image available)",
                            list.getFont());
            }

            return this;
        }
        
        //ustaw font i napis gdy brak obrazka
        protected void setUhOhText(String uhOhText, Font normalFont) {
        	//jezeli trzeba utworz font
            if (uhOhFont == null) { 
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }
}
