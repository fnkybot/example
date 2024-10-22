import javax.swing.*;
import java.awt.Color;
import java.awt.Container;
import java.util.Calendar;
import java.util.Date;

/** przyklad uzycia komponentu JSpinner
 *	wymagane pliki:
 *   SpringUtilities.java
 *   CyclingSpinnerListModel.java
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class SpinnerDemo extends JPanel {
    public SpinnerDemo(boolean cycleMonths) {
        super(new SpringLayout());

        String[] labels = {"Month: ", "Year: ", "Another Date: "};
        int numPairs = labels.length;
        Calendar calendar = Calendar.getInstance();
        JFormattedTextField ftf = null;

        //dodanie pierwszej pary label-spinner
        String[] monthStrings = getMonthStrings(); //wczytanie nazw miesiecy
        SpinnerListModel monthModel = null;
        if (cycleMonths) { //dostosowany model spinnera
            monthModel = new CyclingSpinnerListModel(monthStrings);
        } else { //standardowy model
            monthModel = new SpinnerListModel(monthStrings);
        }
        JSpinner spinner = addLabeledSpinner(this,
                                             labels[0],
                                             monthModel);        
        //wyrownanie pola sformatowanego tekstu w spinnerze
        ftf = getTextField(spinner);
        if (ftf != null ) {
            ftf.setColumns(8); //wiecej miejsca niz potrzeba
            ftf.setHorizontalAlignment(JTextField.RIGHT);
        }


        //dodanie drugiej pary label-spinner
        int currentYear = calendar.get(Calendar.YEAR);
        SpinnerModel yearModel = new SpinnerNumberModel(currentYear, //wart. pocz.
                                       currentYear - 100, //min
                                       currentYear + 100, //max
                                       1);                //krok
        //podlaczenie modelu jesli lista cykliczna
        if (monthModel instanceof CyclingSpinnerListModel) {
            ((CyclingSpinnerListModel)monthModel).setLinkedModel(yearModel);
        }
        spinner = addLabeledSpinner(this, labels[1], yearModel);        
        //rok nie ma zawierac separatora po tysiacach
        spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));


        //dodanie trzeciej pary label-spinner
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -100);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 200);
        Date latestDate = calendar.getTime();
        SpinnerModel dateModel = new SpinnerDateModel(initDate,
                                     earliestDate,
                                     latestDate,
                                     Calendar.YEAR);
        spinner = addLabeledSpinner(this, labels[2], dateModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "MM/yyyy"));
 		//wyrownanie pola sformatowanego tekstu w spinnerze
        ftf = getTextField(spinner);
        if (ftf != null ) {
            ftf.setHorizontalAlignment(JTextField.RIGHT);
            ftf.setBorder(BorderFactory.createEmptyBorder(1,1,1,3));
        }
        spinner.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        //Rozmieszczenie na panelu
        SpringUtilities.makeCompactGrid(this,
                                        numPairs, 2, //wiersze,kolumny
                                        10, 10,        //initX, initY
                                        6, 10);       //xPad, yPad
    }

    /**	metoda dostepu do pola tekstowego spinnera
     * 	@return pole sformatowanego tekstu edytora w spinnerze badz 
     *	null gdy nie jest to edytor typu JSpinner.DefaultEditor
     *	@param spinner wybrany spinner 
     */
    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: "
                               + spinner.getEditor().getClass()
                               + " isn't a descendant of DefaultEditor");
            return null;
        }
    }

    /**	metoda uzyskujaca tablice wypelniona nazwami miesiecy
     *	@return tablica napisow zawierajaca nazwy miesiecy 
     */
    static protected String[] getMonthStrings() {
    	//DateFormatSymbols zwraca dodatkowa pusta wartosc w 
    	//tablicy nazw miesiecy, nalezy ja usunac
        String[] months = new java.text.DateFormatSymbols().getMonths();
        int lastIndex = months.length - 1;

        if (months[lastIndex] == null
           || months[lastIndex].length() <= 0) { //ostatni element pusty
            String[] monthStrings = new String[lastIndex];
            System.arraycopy(months, 0,
                             monthStrings, 0, lastIndex);
            return monthStrings;
        } else { //ostatni element niepusty
            return months;
        }
    }

	/**	metoda dodajaca do wskazanego kontenera spinner 
	 *	o okreslonej etykiecie i modelu
	 *	@return skonstruowany spinner
	 *	@param c kontener ktory ma zawierac spinnera
	 *	@param label etykieta spinnera
	 *	@param model model spinnera
	 */
    static protected JSpinner addLabeledSpinner(Container c,
                                                String label,
                                                SpinnerModel model) {
        JLabel l = new JLabel(label);
        c.add(l);

        JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);

        return spinner;
    }

    /**	utworzenie i pokazanie GUI 
     *	dla zapewnienia bezpieczenstwa ponizsza metoda 
     *	powinna byc wywolywana z watku rozsylajacego zdarzenia   
     */
    private static void createAndShowGUI() {        
        //ustawienie ladnego wygladu okien
        JFrame.setDefaultLookAndFeelDecorated(true);

		//utworzenie i przygotowanie okna
        JFrame frame = new JFrame("SpinnerDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        JComponent newContentPane = new SpinnerDemo(false);
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
