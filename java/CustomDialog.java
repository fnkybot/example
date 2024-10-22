
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;

/**
 * przyklad uzywany przez DialogDemo.java niezamykajace sie automatycznie okno
 * dialogowe z sprawdzaniem poprawnosci danych wprowadzonych do pola tekstowego
 */
class CustomDialog extends JDialog
        implements ActionListener,
        PropertyChangeListener {

    /**
     * napis wprowadzony przez uzytkownika
     */
    private String typedText = null;
    /**
     * pole tekstowe do wprowadzenia napisu
     */
    private JTextField textField;
    /**
     * aplikacja uzywajaca dialogu
     */
    private DialogDemo dd;

    /**
     * poprawny napis ktory nalezy wprowadzic
     */
    private String magicWord;
    /**
     * panel
     */
    private JOptionPane optionPane;

    /**
     * napis dla przyciskow
     */
    private String btnString1 = "Enter";
    /**
     * napis dla przyciskow
     */
    private String btnString2 = "Cancel";

    /**
     * @return null gdy wprowadzono niewlasciwy tekst lub wlasciwy napis
     * wprowadzony przez uzytkownika
     */
    public String getValidatedText() {
        return typedText;
    }

    /**
     * konstruktor okna dialogowego
     */
    public CustomDialog(Frame aFrame, String aWord, DialogDemo parent) {
        super(aFrame, true);
        dd = parent;

        //odpowiedz duzymi literami 
        magicWord = aWord.toUpperCase();
        //tytul okna dialogowego
        setTitle("Quiz");

        textField = new JTextField(10);

        //utworzenie tablic z wyswietlanym tekstem i komponentami
        String msgString1 = "What was Dr. SEUSS's real last name?";
        String msgString2 = "(The answer is \"" + magicWord
                + "\".)";
        Object[] array = {msgString1, msgString2, textField};

        //tablica napisow dla przyciskow
        Object[] options = {btnString1, btnString2};

        //utworzenie panelu zawierajacego komponenty
        optionPane = new JOptionPane(array,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);

        //biezace okno dialogowe wyswietli utworzony panel
        setContentPane(optionPane);

        //obsluga zamkniecia okna
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                /*
                 *	zamiast bezposrednio zamykac okno 
                 *	mozna zmienic wlasciwosc panelu
                 */
                optionPane.setValue(JOptionPane.CLOSED_OPTION);
            }
        });

        //pole tekstowe otrzymuje focus jako pierwsze
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                textField.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        textField.addActionListener(this);

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
    }

    /**
     * metoda obslugujaca zdarzenia pola tekstowego
     *
     * @param e zdarzenie od pola tekstowego
     */
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }

    /**
     * metoda reagujaca na zmiane stanu panelu
     *
     * @param e zdarzenie zmiany stanu panelu
     */
    public void propertyChange(PropertyChangeEvent e) {
        //zmieniona wlasciwosc 
        String prop = e.getPropertyName();

        //dla widzialnego okna dialogowego 
        if (isVisible()
                //czy panel jest zrodlem zmian
                && (e.getSource() == optionPane)
                //czy zmienione wlasnosci to wprowadzenie nowej wartosci
                && (JOptionPane.VALUE_PROPERTY.equals(prop)
                || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignorowanie wyczyszczenia pola tekstowego
                return;
            }

            //wyczyszczenie wartosci panelu
            //gdyby zabraklo tego po kolejnym nacisnieciu 
            //przycisku nie nastapiloby wygenerowanie zdarzenia
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            //nacisnieto przycisk wprowadzajacy tekst
            if (btnString1.equals(value)) {
                typedText = textField.getText();
                String ucText = typedText.toUpperCase();
                if (magicWord.equals(ucText)) {
                    //tekst sie zgadza, mozna ukryc dialog
                    clearAndHide();
                } else {
                    //tekst sie nie zgadza
                    //wyswietlenie okna powiadamiajacego o bledzie
                    textField.selectAll();
                    JOptionPane.showMessageDialog(
                            CustomDialog.this,
                            "Sorry, \"" + typedText + "\" "
                            + "isn't a valid response.\n"
                            + "Please enter "
                            + magicWord + ".",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                    typedText = null;
                    textField.requestFocusInWindow();
                }
            } else { //zamkniecie okna lub przycisk cancel
                dd.setLabel("It's OK.  "
                        + "We won't force you to type "
                        + magicWord + ".");
                typedText = null;
                clearAndHide();
            }
        }
    }

    /**
     * metoda resetujaca i ukrywajaca okno dialogowe
     */
    public void clearAndHide() {
        textField.setText(null);
        setVisible(false);
    }
}
