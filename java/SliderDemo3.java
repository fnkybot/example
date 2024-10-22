import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.NumberFormatter;
import java.beans.*;

/**	Przyklad demonstrujacy uzycie komponentu JSlider
 * 	SliderDemo3.java wymaga wszystkich plikow znajdujacych sie w	
 *	katalogu images/doggy   
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class SliderDemo3 extends JPanel
                         implements ActionListener,
                                    WindowListener,
                                    ChangeListener,
                                    PropertyChangeListener {    
    /** parametr animacji */
    static final int FPS_MIN = 0;
    /** parametr animacji */
    static final int FPS_MAX = 30;
    /** poczatkowa wartosc fps */
    static final int FPS_INIT = 15;    
    
    /** biezacy nr klatki */
    int frameNumber = 0;
    /** ilosc klatek */
    int NUM_FRAMES = 14;
    /** tablica zawierajaca ikony obrazkow */
    ImageIcon[] images = new ImageIcon[NUM_FRAMES];
    /** opoznienie w animacji */
    int delay;
    /** timer stosowany do animacji */
    Timer timer;
    /** true gdy animacja zatrzymana */
    boolean frozen = false;

    /** etykieta uzywajaca ImageIcon do wyswiatlania obrazkow */
    JLabel picture;

    /** pole sformatowanego tekstu */
    JFormattedTextField textField;

    /** slider do regulacji predkosci animacji */
    JSlider framesPerSecond;

    /** konstruktor bezparametryczny */
    public SliderDemo3() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        delay = 1000 / FPS_INIT;

        //utworzenie etykiety z napisem
        JLabel sliderLabel = new JLabel("Frames Per Second: ", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //utworzenie pola tekstu sformatowanego z klasa formatujaca liczby
        java.text.NumberFormat numberFormat =
            java.text.NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(numberFormat);
        formatter.setMinimum( FPS_MIN);
        formatter.setMaximum(FPS_MAX);
        textField = new JFormattedTextField(formatter);
        textField.setValue(FPS_INIT);
        textField.setColumns(5); //wolne miejsce
        textField.addPropertyChangeListener(this);

        //reakcja na klawisz Enter
        textField.getInputMap().put(KeyStroke.getKeyStroke(
                                        KeyEvent.VK_ENTER, 0),
                                        "check");
        textField.getActionMap().put("check", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!textField.isEditValid()) { //nieprawidlowy tekst
                    Toolkit.getDefaultToolkit().beep();
                    textField.selectAll();
                } else try {                    //tekst prawidlowy
                    textField.commitEdit();     //mozna go uzyc
                } catch (java.text.ParseException exc) { }
            }
        });
        //XXX:	poniewaz ani actionPerformed ani invalidEdit nie zostaja
        //		wywolane w przypadku wpisania blednej wartosci
        //		(np. 100 i Enter) zostala dodana akcja dla klawisza Enter
        //		zastepujaca ActionListener dla pola sformatowanego tekstu

        //utworzenie slidera
        framesPerSecond = new JSlider(JSlider.HORIZONTAL,
                                      FPS_MIN, FPS_MAX, FPS_INIT);
        framesPerSecond.addChangeListener(this);

        //utworzenie etykiet dla podzialki slidera
        framesPerSecond.setMajorTickSpacing(10);
        framesPerSecond.setMinorTickSpacing(1);
        framesPerSecond.setPaintTicks(true);
        framesPerSecond.setPaintLabels(true);
        framesPerSecond.setBorder(
                BorderFactory.createEmptyBorder(0,0,10,0));

        //etykieta wyswietlajaca animacje
        picture = new JLabel();
        picture.setHorizontalAlignment(JLabel.CENTER);
        picture.setAlignmentX(Component.CENTER_ALIGNMENT);
        picture.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(10,10,10,10)));
        updatePicture(0); //wyswietlenie pierwszej klatki

        //utworzenie panelu dla etykiety i pola tekstu
        JPanel labelAndTextField = new JPanel(); //FlowLayout
        labelAndTextField.add(sliderLabel);
        labelAndTextField.add(textField);

        //rozmieszczenie komponentow
        add(labelAndTextField);
        add(framesPerSecond);
        add(picture);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        //ustawienie timera wywolujacego obsluge animacji
        timer = new Timer(delay, this);
        //dwukrotne zatrzymanie animacji w cyklu
        //przez restart timera
        timer.setInitialDelay(delay * 7); 
        timer.setCoalesce(true);
    }

    /** dodanie nasluchu dla zdarzen generowanych przez okno
     *	@param w okno do nasluchu
     */
    void addWindowListener(Window w) {
        w.addWindowListener(this);
    }

    /* obsluga zdarzen okna*/
    public void windowIconified(WindowEvent e) {
        stopAnimation();
    }    
    public void windowDeiconified(WindowEvent e) {
        startAnimation();
    }    
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

    /** obsluga zdarzen slidera */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        int fps = (int)source.getValue();        
        if (!source.getValueIsAdjusting()) { //wskaznik jest juz ustawiony
        	//aktualizacja wyswietlanej wartosci
            textField.setValue(fps); 
            if (fps == 0) {
                if (!frozen) stopAnimation();
            } else {
                delay = 1000 / fps;
                timer.setDelay(delay);
                timer.setInitialDelay(delay * 10);
                if (frozen) startAnimation();
            }
        } else { 
        	//wartosc w trakcie zmian, tylko ustawienie tekstu
            textField.setText(String.valueOf(fps));
        }
    }

    /** obsluga zdarzenia generowanego przez pole tekstowe
     *	wykrywa zmiane wartosci w polu tekstowym
     *	(niekoniecznie ta sama wartosc ktora zwraca getText)
     */
    public void propertyChange(PropertyChangeEvent e) {
        if ("value".equals(e.getPropertyName())) {
            Number value = (Number)e.getNewValue();
            if (framesPerSecond != null && value != null) {
                framesPerSecond.setValue(value.intValue());
            }
        }
    }

	/** wlaczenie animacji */
    public void startAnimation() {
        timer.start();
        frozen = false;
    }

    /** wylaczenie animacji */
    public void stopAnimation() {
        timer.stop();
        frozen = true;
    }

    /** obsluga zdarzenia generowanego przez timer
     */
    public void actionPerformed(ActionEvent e) {
        //aktualizacja numeru wyswietlanej klatki
        if (frameNumber == (NUM_FRAMES - 1)) {
            frameNumber = 0;
        } else {
            frameNumber++;
        }

        //wyswietlenie kolejnego obrazka
        updatePicture(frameNumber); 

		//zatrzymanie 2 razy w cyklu
        if ( frameNumber==(NUM_FRAMES - 1)
          || frameNumber==(NUM_FRAMES/2 - 1) ) {
            timer.restart();
        }
    }

    /** metoda wyswietlajaca na etykiecie odpowiednia klatke animacji
     *	@param frameNum
     */
    protected void updatePicture(int frameNum) {
        //jesli trzeba zaladuj obrazek
        if (images[frameNumber] == null) {
            images[frameNumber] = createImageIcon("images/doggy/T"
                                                  + frameNumber
                                                  + ".gif");
        }

        //ustaw obrazek na zadany
        if (images[frameNumber] != null) {
            picture.setIcon(images[frameNumber]);
        } else { //brak obrazka
            picture.setText("image #" + frameNumber + " not found");
        }
    }

    /** metoda wczytujaca plik graficzny i tworzaca ikone
     *	@param path sciezka do pliku z ikona
     *	@return ImageIcon lub null gdy podano zla sciezke
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SliderDemo3.class.getResource(path);
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
        JFrame frame = new JFrame("SliderDemo3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        SliderDemo3 animator = new SliderDemo3();
        //panel widoczny
        animator.setOpaque(true); 
        frame.setContentPane(animator);

        //wyswietlenie okna
        frame.pack();
        frame.setVisible(true);
        animator.startAnimation(); 
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