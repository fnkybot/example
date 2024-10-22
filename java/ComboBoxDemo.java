import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**	Przyklad uzycia komponentu JComboBox
 * 	ComboBoxDemo.java wymaga nastepujacych plikow:
 *   images/Bird.gif
 *   images/Cat.gif
 *   images/Dog.gif
 *   images/Rabbit.gif
 *   images/Pig.gif
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class ComboBoxDemo extends JPanel
                          implements ActionListener {
	/** obrazek */                          	
    JLabel picture;

	/** konstruktor */
    public ComboBoxDemo() {
    	//ustawienie rozkladu BorderLayout (konstruktor klasy JPanel)
        super(new BorderLayout());
		
		//napisy dla kazdego wyswietlanego zwierzaka
        String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };

        //utworzenie combobox'a i ustawienie indeksu na 4
        //indeksy rozpoczynaja sie od 0, wiec ustawiona zostaje swinka
        JComboBox petList = new JComboBox(petStrings);
        petList.setSelectedIndex(4);
        petList.addActionListener(this);
        petList.setRenderer(new MyComboBoxRenderer());

        //wyswietlenie obrazka
        picture = new JLabel();
        picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
        picture.setHorizontalAlignment(JLabel.CENTER);
        updateLabel(petStrings[petList.getSelectedIndex()]);
        picture.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        //ustawienie rozmiaru najwiekszego obrazka "na sztywno"
        //prawdziwy program powinien to obliczac
        picture.setPreferredSize(new Dimension(177, 122+10));

        //rozmieszczenie komponentow
        add(petList, BorderLayout.PAGE_START);
        add(picture, BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    /** nasluch combo box'a 
     *	@param e nadchodzace zdarzenie od combobox'a 
     */
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        String petName = (String)cb.getSelectedItem();
        updateLabel(petName);
    }

	/** metoda wyswietlajaca wlasciwy obrazek
	 *	@param name napis z nazwa zwierzaka (i pliku z jego obrazkiem)
	 */
    protected void updateLabel(String name) {
        ImageIcon icon = createImageIcon("images/" + name + ".gif");
        picture.setIcon(icon);
        picture.setToolTipText("A drawing of a " + name.toLowerCase());
        if (icon != null) {
            picture.setText(null);
        } else {
            picture.setText("Image not found");
        }
    }
class MyComboBoxRenderer extends BasicComboBoxRenderer {
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      if (isSelected) {
        setBackground(list.getSelectionBackground());
        setForeground(list.getSelectionForeground());
        if (-1 < index) {
          list.setToolTipText("" +index);
        }
      } else {
        setBackground(list.getBackground());
        setForeground(list.getForeground());
      }
      setFont(list.getFont());
      setText((value == null) ? "" : value.toString());
      return this;
    }
  }
    /**	metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ComboBoxDemo.class.getResource(path);
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
        JFrame frame = new JFrame("ComboBoxDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        JComponent newContentPane = new ComboBoxDemo();
        //panel widoczny
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);

        //wyswietlenie okna
        frame.pack();
        frame.setVisible(true);
    }
	
	/** metoda glowna 

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
