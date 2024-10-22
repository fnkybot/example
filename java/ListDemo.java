import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class ListDemo extends JPanel
                      implements ListSelectionListener {
    /** lista z nazwiskami pracownikow*/
    private JList list;
    /** model listy */
    private DefaultListModel listModel;

	/** zatrudnianie nowych pracownikow */
    private static final String hireString = "Hire";
    /** zwalnianie pracownikow */
    private static final String fireString = "Fire";
    /** przycisk do zwalniania pracownikow */    
    private JButton fireButton;
    /** pole tekstowe na nazwe pracownika do zatrudnienia */
    private JTextField employeeName;

	/** konstruktor bezparametryczny */
    public ListDemo() {
        super(new BorderLayout());

		//dodanie elementow do domyslnego modelu listy
        listModel = new DefaultListModel();
        listModel.addElement("Alan Sommerer");
        listModel.addElement("Alison Huml");
        listModel.addElement("Kathy Walrath");
        listModel.addElement("Lisa Friendly");
        listModel.addElement("Mary Campione");
        listModel.addElement("Sharon Zakhour");

        //utworzenie listy i umieszczenie w panelu przewijalnym
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton hireButton = new JButton(hireString);
        HireListener hireListener = new HireListener(hireButton);
        hireButton.setActionCommand(hireString);
        hireButton.addActionListener(hireListener);
        hireButton.setEnabled(false);

        fireButton = new JButton(fireString);
        fireButton.setActionCommand(fireString);
        fireButton.addActionListener(new FireListener());

        employeeName = new JTextField(10);
        employeeName.addActionListener(hireListener);
        employeeName.getDocument().addDocumentListener(hireListener);
        String name = listModel.getElementAt(
                              list.getSelectedIndex()).toString();

        //utworzenie panelu z rozkladem BoxLayout
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(fireButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(employeeName);
        buttonPane.add(hireButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

	/** klasa sluchacza dla przycisku Fire 
	 */
    class FireListener implements ActionListener {
    	/** obsluga zdarzenia typu ActionEvent
    	 *	@param e nadchodzace zdarzenie
    	 */
        public void actionPerformed(ActionEvent e) {
            //metoda moze byc wywolana tylko gdy istnieje 
            //zaznaczony element, wystarczy usunac
            //to co jest zaznaczone
            int index = list.getSelectedIndex();
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) { 
            //brak elementow nie mozna zwalniac wiecej
                fireButton.setEnabled(false);

            } else { 
            	//gdy usunieto ostatni element
                if (index == listModel.getSize()) {
                    //zaznaczenie jego poprzednika
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    /** sluchacz dla pola tekstowego i przycisku Hire */
    class HireListener implements ActionListener, DocumentListener {
        /** true gdy przycisk Hire wlaczony */
        private boolean alreadyEnabled = false;
        /** przycisk do zatrudniania nowych pracownikow */
        private JButton button;

        /** konstruktor
         *	@param przycisk Hire 
         */
        public HireListener(JButton button) {
            this.button = button;
        }

        /** nasluch dla interfejsu ActionListener
         */
        public void actionPerformed(ActionEvent e) {
            String name = employeeName.getText();

            //gdy uzytkownik wpisal powtarzajaca sie nazwe
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                employeeName.requestFocusInWindow();
                employeeName.selectAll();
                return;
            }
			
			//indeks wybranego elementu
            int index = list.getSelectedIndex(); 
            //jesli brak wybranego wstawienie na poczatek
            if (index == -1) { 
                index = 0;
            } else {	//wstawienie na kolejna po zaznaczonej pozycji
                index++;
            }

            listModel.insertElementAt(employeeName.getText(), index);
            //mozna rowniez dodac na koniec listy:
            //listModel.addElement(employeeName.getText());

            //wyczyszczenie pola tekstowego
            employeeName.requestFocusInWindow();
            employeeName.setText("");

            //wybranie nowego elementu i pokazanie go
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        /** metoda sprawdza rownosc napisow
         *  mozna uzyc bardziej wyrafinowanych metod np sprawdzac
         *	biale znaki i wielkosc liter
         *	@param name testowany napis
         *	@retrun true gdy lista zawiera podany napis
         */        
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        /** nasluch dla interfejsu DocumentListener
         *	obsluguje dopisanie czegos w pole tekstowe
         */
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        /** nasluch dla interfejsu DocumentListener
         *	obsluguje wykasowanie znakow z pola tekstowego
         */
         public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        /** nasluch dla interfejsu DocumentListener
         *	obsluguje zmiane tekstu w polu tekstowym
         */
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        /** wlaczenie przycisku */
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

		/** sprawdza czy pole tekstowe jest puste
		 *	w razie potrzeby wylacza przycisk
		 *	@return true gdy pole tekstowe puste
		 */
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    /** metoda nasluchu dla ListSelectionListener
     *	obsluga zmiany wybranego elementu listy
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
            //brak wybranego elementu wylaczenie przycisku Fire
                fireButton.setEnabled(false);

            } else {
            //jest wybrany element wlaczenie przycisku Fire
                fireButton.setEnabled(true);
            }
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
        JFrame frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        JComponent newContentPane = new ListDemo();
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
