import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;

/** Przyklad demonstrujacy uzycie okien dialogowych
 *	DialogDemo.java wymaga nastepujacych plikow:
 *   CustomDialog.java
 *   images/middle.gif
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class DialogDemo extends JPanel {
	/**	etykieta wyswietlana u dolu okna */
    JLabel label;
    /** ikona dla okna dialogowego z pliku */
    ImageIcon icon = createImageIcon("images/middle.gif");
    /** glowne okno aplikacji, rodzic dla okien dialogowych */
    JFrame frame;
    /** napisy podpowiedzi dla poszczegolnych przedzialow (tab) */
    String simpleDialogDesc = "Some simple message dialogs";
    String iconDesc = "A JOptionPane has its choice of icons";
    String moreDialogDesc = "Some more dialogs";
    /** okno dialogowe specjalnej klasy sprawdzajace dane wejsciowe */
    CustomDialog customDialog;

    /** konstruktor tworzacy GUI w panelu okna
     *	@param frame glowne okno aplikacji
     */
    public DialogDemo(JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
        
        //utworzenie okna sprawdzajacego poprawnosc wprowadzanego tekstu
        //poprawny tekst to "geisel"
        customDialog = new CustomDialog(frame, "geisel", this);
        customDialog.pack();

        //utworzenie komponentow
        JPanel frequentPanel = createSimpleDialogBox();
        JPanel featurePanel = createFeatureDialogBox();
        JPanel iconPanel = createIconDialogBox();
        label = new JLabel("Click the \"Show it!\" button"
                           + " to bring up the selected dialog.",
                           JLabel.CENTER);

        //rozmieszczenie komponentow
        Border padding = BorderFactory.createEmptyBorder(20,20,5,20);
        frequentPanel.setBorder(padding);
        featurePanel.setBorder(padding);
        iconPanel.setBorder(padding);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Simple Modal Dialogs", null,
                          frequentPanel,
                          simpleDialogDesc); //tekst podpowiedzi
        tabbedPane.addTab("More Dialogs", null,
                          featurePanel,
                          moreDialogDesc); //tekst podpowiedzi
        tabbedPane.addTab("Dialog Icons", null,
                          iconPanel,
                          iconDesc); //tekst podpowiedzi

        add(tabbedPane, BorderLayout.CENTER);
        add(label, BorderLayout.PAGE_END);
        label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }

    /** zmiana tekstu wyswietlanego na dole glownego okna
     *	@param newText nowy napis do wyswietlenia 
     */
    void setLabel(String newText) {
        label.setText(newText);
    }

    /** metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = DialogDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /** utworzenie panelu zawartego w pierwszym przedziale (tab)
     *	@return utworzony panel
     */
    private JPanel createSimpleDialogBox() {
    	//ilosc przyciskow do wyboru okna dialogowego
        final int numButtons = 4;
        //przyciski radiobutton do wyboru okna dialogowego
        JRadioButton[] radioButtons = new JRadioButton[numButtons];       
        final ButtonGroup group = new ButtonGroup();

		//przycisk show it
        JButton showItButton = null;

		//napisy okreslajace komendy zdarzen od przyciskow
        final String defaultMessageCommand = "default";
        final String yesNoCommand = "yesno";
        final String yeahNahCommand = "yeahnah";
        final String yncCommand = "ync";
        
        //utworzenie przyciskow wyboru okna dialogowego i przypisanie im
        //odpowiednich zdarzen

        radioButtons[0] = new JRadioButton("OK (in the L&F's words)");
        radioButtons[0].setActionCommand(defaultMessageCommand);

        radioButtons[1] = new JRadioButton("Yes/No (in the L&F's words)");
        radioButtons[1].setActionCommand(yesNoCommand);

        radioButtons[2] = new JRadioButton("Yes/No "
                      + "(in the programmer's words)");
        radioButtons[2].setActionCommand(yeahNahCommand);

        radioButtons[3] = new JRadioButton("Yes/No/Cancel "
                           + "(in the programmer's words)");
        radioButtons[3].setActionCommand(yncCommand);
        
        //zgrupowanie radiobuttonow
        for (int i = 0; i < numButtons; i++) {
            group.add(radioButtons[i]);
        }
        radioButtons[0].setSelected(true);
        
        //utworzenie przycisku show it
        showItButton = new JButton("Show it!");
        //obsluga nacisniecia przycisku
        showItButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//komenda zalezna od wybranego przycisku radiobutton
                String command = group.getSelection().getActionCommand();

                //wyswietlenie dialogu z przyciskiem ok
                //oraz zadanym komunikatem
                if (command == defaultMessageCommand) {
                    JOptionPane.showMessageDialog(frame,
                                "Eggs aren't supposed to be green.");

                //wyswietlenie dialogu z przyciskami yes/no
                } else if (command == yesNoCommand) {
                    int n = JOptionPane.showConfirmDialog(
                            frame, "Would you like green eggs and ham?",
                            "An Inane Question", //tytul okna dialogowego
                            JOptionPane.YES_NO_OPTION);//opcje
                    //w zaleznosci od wybranego w dialogu przycisku
                    //zmiana napisu w dole glownego okna                            
                    if (n == JOptionPane.YES_OPTION) {
                        setLabel("Ewww!");
                    } else if (n == JOptionPane.NO_OPTION) {
                        setLabel("Me neither!");
                    } else {
                        setLabel("Come on -- tell me!");
                    }

                //yes/no (innymi slowami)
                } else if (command == yeahNahCommand) {
                	//tablica zawierajaca napisy odpowiedzi 
                	//dla przyciskow w oknie dialogowym
                    Object[] options = {"Yes, please", "No way!"};
                    
                    int n = JOptionPane.showOptionDialog(frame,
                                    "Would you like green eggs and ham?",
                                    "A Silly Question",//tytul okna
                                    JOptionPane.YES_NO_OPTION,//opcje
                                    JOptionPane.QUESTION_MESSAGE,//ikona L&F
                                    null,//zewnetrzna ikona
                                    options,//tablica zaw. tekst opcji 
                                    options[0]);//poczatkowa wartosc                    
                    //zmiana napisu w dole glownego okna                                         
                    if (n == JOptionPane.YES_OPTION) {
                        setLabel("You're kidding!");
                    } else if (n == JOptionPane.NO_OPTION) {
                        setLabel("I don't like them, either.");
                    } else { //gdy zamknieto okno
                        setLabel("Come on -- 'fess up!");
                    }

                //yes/no/cancel (innymi slowami)
                //przyklad prawie jak wyzej  
                } else if (command == yncCommand) {
                    Object[] options = {"Yes, please",
                                        "No, thanks",
                                        "No eggs, no ham!"};
                    int n = JOptionPane.showOptionDialog(frame,
                                    "Would you like some green eggs to go "
                                    + "with that ham?",
                                    "A Silly Question",
                                    JOptionPane.YES_NO_CANCEL_OPTION,//3 opcje
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[2]);
                    if (n == JOptionPane.YES_OPTION) {
                        setLabel("Here you go: green eggs and ham!");
                    } else if (n == JOptionPane.NO_OPTION) {
                        setLabel("OK, just the ham, then.");
                    } else if (n == JOptionPane.CANCEL_OPTION) {
                        setLabel("Well, I'm certainly not going to eat them!");
                    } else {
                        setLabel("Please tell me what you want!");
                    }
                }
                return;
            }
        });

        return createPane(simpleDialogDesc + ":",
                          radioButtons,
                          showItButton);
    }

    /** metoda uzywana przez createSimpleDialogBox i createFeatureDialogBox
     *	do utworzenia panelu zawierajacego opis, pojedyncza kolumne
     *	przyciskow radiobutton i przycisk Show it!
     *	@return utworzony panel
     *	@param description opis
     *	@param radioButtons tablica przyciskow jednokrotnego wyboru
     *	@param showButton przycisk Show it!
     */
    private JPanel createPane(String description,
                              JRadioButton[] radioButtons,
                              JButton showButton) {

        int numChoices = radioButtons.length;
        JPanel box = new JPanel();
        JLabel label = new JLabel(description);

        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        box.add(label);

        for (int i = 0; i < numChoices; i++) {
            box.add(radioButtons[i]);
        }

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(box, BorderLayout.PAGE_START);
        pane.add(showButton, BorderLayout.PAGE_END);
        return pane;
    }

    /** metoda podobna do createPane z tym ze tworzy panel zawierajacy
     *	2 kolumny przyciskow radiobutton
     *	@return utworzony panel
     *	@param description opis
     *	@param radioButtons tablica przyciskow jednokrotnego wyboru
     *	 ilosc przyciskow *musi* byc parzysta
     *	@param showButton przycisk Show it!
     */
     private JPanel create2ColPane(String description,
                                  JRadioButton[] radioButtons,
                                  JButton showButton) {
        JLabel label = new JLabel(description);
        int numPerColumn = radioButtons.length/2;

        JPanel grid = new JPanel(new GridLayout(0, 2));
        for (int i = 0; i < numPerColumn; i++) {
            grid.add(radioButtons[i]);
            grid.add(radioButtons[i + numPerColumn]);
        }

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        box.add(label);
        grid.setAlignmentX(0.0f);
        box.add(grid);

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(box, BorderLayout.PAGE_START);
        pane.add(showButton, BorderLayout.PAGE_END);

        return pane;
    }

    /** utworzenie panelu zawartego w trzecim przedziale (Dialog Icons)
     *	okna dialogowe sa tutaj tworzone za pomoca showMessageDialog
     *	jednakze mozna w podobny sposob dodac ikone do kazdego innego
     *	rodzaju okien dialogowych
     *	@return utworzony panel
     */
    private JPanel createIconDialogBox() {
        JButton showItButton = null;
        
		//przyciski radiobutton
        final int numButtons = 6;
        JRadioButton[] radioButtons = new JRadioButton[numButtons];
        final ButtonGroup group = new ButtonGroup();

		//napisy okreslajace komendy zdarzen od przyciskow
        final String plainCommand = "plain";
        final String infoCommand = "info";
        final String questionCommand = "question";
        final String errorCommand = "error";
        final String warningCommand = "warning";
        final String customCommand = "custom";

		//utworzenie przyciskow i okreslenie zdarzen
        radioButtons[0] = new JRadioButton("Plain (no icon)");
        radioButtons[0].setActionCommand(plainCommand);

        radioButtons[1] = new JRadioButton("Information icon");
        radioButtons[1].setActionCommand(infoCommand);

        radioButtons[2] = new JRadioButton("Question icon");
        radioButtons[2].setActionCommand(questionCommand);

        radioButtons[3] = new JRadioButton("Error icon");
        radioButtons[3].setActionCommand(errorCommand);

        radioButtons[4] = new JRadioButton("Warning icon");
        radioButtons[4].setActionCommand(warningCommand);

        radioButtons[5] = new JRadioButton("Custom icon");
        radioButtons[5].setActionCommand(customCommand);

        for (int i = 0; i < numButtons; i++) {
            group.add(radioButtons[i]);
        }
        radioButtons[0].setSelected(true);

		//przycisk show it oraz jego obsluga
        showItButton = new JButton("Show it!");
        showItButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = group.getSelection().getActionCommand();

                //brak ikony
                if (command == plainCommand) {
                    JOptionPane.showMessageDialog(frame,
                                    "Eggs aren't supposed to be green.",
                                    "A plain message",
                                    JOptionPane.PLAIN_MESSAGE);                
                //ikona-info
                } else if (command == infoCommand) {
                    JOptionPane.showMessageDialog(frame,
                                    "Eggs aren't supposed to be green.",
                                    "Inane informational dialog",
                                    JOptionPane.INFORMATION_MESSAGE);

            //XXX: pytanie z jednym przyciskiem odpowiedzi 
            //XXX: nie jest zazwyczaj zbyt rozsadnym rozwiazaniem
            //XXX: przyklad "Yes/No (w innych slowach)"
            //XXX: ujmuje ta kwestie znacznie lepiej
                            
                //ikona-pytajnik
                } else if (command == questionCommand) {
                    JOptionPane.showMessageDialog(frame,
                                    "You shouldn't use a message dialog "
                                    + "(like this)\n"
                                    + "for a question, OK?",
                                    "Inane question",
                                    JOptionPane.QUESTION_MESSAGE);                
                //ikona-blad
                } else if (command == errorCommand) {
                    JOptionPane.showMessageDialog(frame,
                                    "Eggs aren't supposed to be green.",
                                    "Inane error",
                                    JOptionPane.ERROR_MESSAGE);                
                //ikona-ostrzezenie
                } else if (command == warningCommand) {
                    JOptionPane.showMessageDialog(frame,
                                    "Eggs aren't supposed to be green.",
                                    "Inane warning",
                                    JOptionPane.WARNING_MESSAGE);
                //dowolna ikona
                } else if (command == customCommand) {
                    JOptionPane.showMessageDialog(frame,
                                    "Eggs aren't supposed to be green.",
                                    "Inane custom dialog",
                                    JOptionPane.INFORMATION_MESSAGE,
                                    icon);
                }
            }
        });

        return create2ColPane(iconDesc + ":",
                              radioButtons,
                              showItButton);
    }

    /** utworzenie panelu zawartego w drugim przedziale
     */
    private JPanel createFeatureDialogBox() {
        
        //przyciski radiobutton       
        final int numButtons = 5;
        JRadioButton[] radioButtons = new JRadioButton[numButtons];
        final ButtonGroup group = new ButtonGroup();

        JButton showItButton = null;

		//napisy okreslajace komendy zdarzen od przyciskow
        final String pickOneCommand = "pickone";
        final String textEnteredCommand = "textfield";
        final String nonAutoCommand = "nonautooption";
        final String customOptionCommand = "customoption";
        final String nonModalCommand = "nonmodal";

		//utworzenie przyciskow i okreslenie zdarzen
        radioButtons[0] = new JRadioButton("Pick one of several choices");
        radioButtons[0].setActionCommand(pickOneCommand);

        radioButtons[1] = new JRadioButton("Enter some text");
        radioButtons[1].setActionCommand(textEnteredCommand);

        radioButtons[2] = new JRadioButton("Non-auto-closing dialog");
        radioButtons[2].setActionCommand(nonAutoCommand);

        radioButtons[3] = new JRadioButton("Input-validating dialog "
                                           + "(with custom message area)");
        radioButtons[3].setActionCommand(customOptionCommand);

        radioButtons[4] = new JRadioButton("Non-modal dialog");
        radioButtons[4].setActionCommand(nonModalCommand);

        for (int i = 0; i < numButtons; i++) {
            group.add(radioButtons[i]);
        }
        radioButtons[0].setSelected(true);

        //przycisk show it oraz jego obsluga
        showItButton = new JButton("Show it!");
        showItButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = group.getSelection().getActionCommand();

                //wybor '1 z wielu'
                if (command == pickOneCommand) {
                	//tablica napisow z opcjami do wyboru
                	//najczesciej uzyty zostaje combobox
                    Object[] possibilities = {"ham", "spam", "yam"};
                    String s = (String)JOptionPane.showInputDialog(
                                        frame,
                                        "Complete the sentence:\n"
                                        + "\"Green eggs and...\"",
                                        "Customized Dialog",//tytul
                                        JOptionPane.PLAIN_MESSAGE,
                                        icon,//ikona
                                        possibilities,//opcje do wyboru
                                        "ham");//poczatkowa wartosc

                    //jezeli zwrocono napis zostanie on uzyty w 
                    //komunikacie na dole ekranu
                    if ((s != null) && (s.length() > 0)) {
                        setLabel("Green eggs and... " + s + "!");
                        return;
                    }

                    //w przypadku gdy zwrocony zostal pusty napis/null
                    setLabel("Come on, finish the sentence!");

                //wczytanie tekstu z wejscia uzytkownika
                //wywolane zostanie pole edycyjne
                } else if (command == textEnteredCommand) {
                    String s = (String)JOptionPane.showInputDialog(
                                        frame,
                                        "Complete the sentence:\n"
                                        + "\"Green eggs and...\"",
                                        "Customized Dialog",
                                        JOptionPane.PLAIN_MESSAGE,
                                        icon,
                                        null,//brak opcji do wyboru
                                        "ham");

                    //jezeli zwrocono napis zostanie on uzyty w 
                    //komunikacie na dole ekranu
                    if ((s != null) && (s.length() > 0)) {
                        setLabel("Green eggs and... " + s + "!");
                        return;
                    }

                    //w przypadku gdy zwrocony zostal pusty napis/null
                    setLabel("Come on, finish the sentence!");

                //niezamykajace sie automatycznie okno dialogowe
                //zwykle dialogi zamykane sa po wcisnieciu przycisku
                //nalezacego do okna, mozna jednak zaimplementowac
                //inne dzialanie np. w celu sprawdzenia danych
                //wprowadzanych przez uzytkownika
                } else if (command == nonAutoCommand) {
                    final JOptionPane optionPane = new JOptionPane(
                                    "The only way to close this dialog is by\n"
                                    + "pressing one of the following buttons.\n"
                                    + "Do you understand?",
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION);

                    //sztuczka pozwalajaca na kontrowersyjna praktyke
                    //blokowania mozliwosci zamkniecia okna przez 
                    //dekoracje okna
                    //poniewaz Java Look and Feel nie zaopatrza okien
                    //dialogowych w przycisk zamykajacy okno 
                    //odpowiedzialnosc za jego utworzenie spada
                    //na system
                    JDialog.setDefaultLookAndFeelDecorated(false);

                    //nie mozna uzyc pane.createDialog() gdyz ta metoda
                    //zwraca JDialog ktory jest zamykany 
                    //po przycisnieciu przycisku w oknie dialogowym
                    final JDialog dialog = new JDialog(frame,
                                                 "Click a button",
                                                 true);
                    dialog.setContentPane(optionPane);
                    dialog.setDefaultCloseOperation(
                        JDialog.DO_NOTHING_ON_CLOSE);
                    dialog.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent we) {
                            setLabel("Thwarted user attempt to close window.");
                        }
                    });
                    //zakonczenie sztuczki
                    JDialog.setDefaultLookAndFeelDecorated(true);

                    //sluchacz konieczny do zaimplementowania
                    optionPane.addPropertyChangeListener(
                        new PropertyChangeListener() {
                            public void propertyChange(PropertyChangeEvent e) {
                                String prop = e.getPropertyName();

                                if (dialog.isVisible()
                                 && (e.getSource() == optionPane)
                                 && (JOptionPane.VALUE_PROPERTY.equals(prop))) {                                    
                                    //jezeli cos ma byc sprawdzane 
                                    //przed zamknieciem okna
                                    //nalezy zrobic to tutaj
                                    dialog.setVisible(false);
                                }
                            }
                        });
                    dialog.pack();
                    dialog.setLocationRelativeTo(frame);
                    dialog.setVisible(true);

                    int value = ((Integer)optionPane.getValue()).intValue();
                    if (value == JOptionPane.YES_OPTION) {
                        setLabel("Good.");
                    } else if (value == JOptionPane.NO_OPTION) {
                        setLabel("Try using the window decorations "
                                 + "to close the non-auto-closing dialog. "
                                 + "You can't!");
                    } else {//gdy okno zamknieto za pomoca klawisza ESC
                        setLabel("Window unavoidably closed (ESC?).");
                    }

                //niezamykajace sie automatycznie okno dialogowe 
                //z sprawdzanym polem tekstowym
                //uwaga: jesli nie jest konieczne sprawdzanie
                //poprawnosci wprowadzanych danych 
                //wystarczy showInputDialog
                } else if (command == customOptionCommand) {
                    customDialog.setLocationRelativeTo(frame);
                    customDialog.setVisible(true);

                    String s = customDialog.getValidatedText();
                    if (s != null) {
                        //tekst poprawny
                        setLabel("Congratulations!  "
                                 + "You entered \""
                                 + s
                                 + "\".");
                    }

                //niemodalne okno dialogowe
                } else if (command == nonModalCommand) {
                    //utworzenie okna
                    final JDialog dialog = new JDialog(frame,
                                                       "A Non-Modal Dialog");

                    //dodanie zawartosci okna
                    //konieczny jest przycisk zamykajacy okno
                    //poniewaz niektore L&F nie uwzgledniaja go
                    //w dekoracji okna
                    
                    //etykieta tekstowa                    
                    JLabel label = new JLabel("<html><p align=center>"
                        + "This is a non-modal dialog.<br>"
                        + "You can have one or more of these up<br>"
                        + "and still use the main window.");
                    label.setHorizontalAlignment(JLabel.CENTER);
                    Font font = label.getFont();
                    label.setFont(label.getFont().deriveFont(font.PLAIN,
                                                             14.0f));

                    //przycisk zamkniecia okna
                    JButton closeButton = new JButton("Close");
                    closeButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            dialog.setVisible(false);
                            dialog.dispose();
                        }
                    });
                    
                    //panel dla przycisku close
                    JPanel closePanel = new JPanel();
                    closePanel.setLayout(new BoxLayout(closePanel,
                                                       BoxLayout.LINE_AXIS));
                    closePanel.add(Box.createHorizontalGlue());
                    closePanel.add(closeButton);
                    closePanel.setBorder(BorderFactory.
                        createEmptyBorder(0,0,5,5));

                    //panel z etykieta i powyzszym panelem
                    JPanel contentPane = new JPanel(new BorderLayout());
                    contentPane.add(label, BorderLayout.CENTER);
                    contentPane.add(closePanel, BorderLayout.PAGE_END);
                    contentPane.setOpaque(true);
                    dialog.setContentPane(contentPane);

                    //pokazanie calosci
                    dialog.setSize(new Dimension(300, 150));
                    dialog.setLocationRelativeTo(frame);
                    dialog.setVisible(true);
                }
            }
        });

        return createPane(moreDialogDesc + ":",
                          radioButtons,
                          showItButton);
    }

    /**	utworzenie i pokazanie GUI 
     *	dla zapewnienia bezpieczenstwa ponizsza metoda 
     *	powinna byc wywolywana z watku rozsylajacego zdarzenia   
     */
    private static void createAndShowGUI() {        
        //ustawienie ladnego wygladu okien
        JFrame.setDefaultLookAndFeelDecorated(true);

		//utworzenie i przygotowanie okna
        JFrame frame = new JFrame("DialogDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        DialogDemo newContentPane = new DialogDemo(frame);
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

