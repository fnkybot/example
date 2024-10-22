import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/** Przyklad uzycia opcji dekoracji okna, pozycjonowania okien
 * 	oraz zmiany ikony okna
 * 	FrameDemo2.java is wymaga pliku:
 *  	images/FD.jpg.
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class FrameDemo2 extends WindowAdapter
                        implements ActionListener {
	/** ostatnia lokalizacja nowego okna */                        	
    private Point lastLocation = null;
    /** maksymalna szerokosc ekranu */
    private int maxX = 500;
    /** maksymalna wysokosc ekranu */
    private int maxY = 500;

    /** domyslny przycisk glownego okna */
    private static JButton defaultButton = null;

    /** stala dla komend zdarzen */
    protected final static String NO_DECORATIONS = "no_dec";
    /** stala dla komend zdarzen */
    protected final static String LF_DECORATIONS = "laf_dec";
    /** stala dla komend zdarzen */
    protected final static String WS_DECORATIONS = "ws_dec";
    /** stala dla komend zdarzen */
    protected final static String CREATE_WINDOW = "new_win";
    /** stala dla komend zdarzen */
    protected final static String DEFAULT_ICON = "def_icon";
    /** stala dla komend zdarzen */
    protected final static String FILE_ICON = "file_icon";
    /** stala dla komend zdarzen */
    protected final static String PAINT_ICON = "paint_icon";
    
    /** true gdy kolejne tworzone okno nie ma miec dekoracji */
    protected boolean noDecorations = false;

    /** true gdy dla kolejnego tworzonego okna nalezy uzyc setIconImage */
    protected boolean specifyIcon = false;

    /** true gdy dla kolejnego tworzonego okna nalezy rysowac ikone */
    protected boolean createIcon = false;

    /** konstruktor - poczatkowa inicjalizacja 
     */
    public FrameDemo2() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        maxX = screenSize.width - 50;
        maxY = screenSize.height - 50;
    }
    
    /** utworzenie i pokazanie nowego obiektu MyFrame 
     */
    public void showNewWindow() {
        JFrame frame = new MyFrame();
        
        //przypadek okna bez dekoracji
        //UWAGA: w przypadku gdy nie jest konieczna funkcjonalnosc
        //JFrame dla okna bez dekoracji wystarczajace sa klasy
        //Window lub JWindow
        if (noDecorations) {
            frame.setUndecorated(true);
        }

		//ustawienie polozenia okna
        if (lastLocation != null) {
        	//przesun okno o 40 pixeli w prawo i w dol
            lastLocation.translate(40, 40);
            if ((lastLocation.x > maxX) || (lastLocation.y > maxY)) {
                lastLocation.setLocation(0, 0);
            }
            frame.setLocation(lastLocation);
        } else {
            lastLocation = frame.getLocation();
        }

        //wywolanie setIconImage ustawia wyswietlana ikone dla okna 
        //zminimalizowanego, wiekszosc systemow uzywa jej rowniez 
        //w dekoracji okna
        if (specifyIcon) {
            if (createIcon) {
            	//uzycie narysowanej ikony
                frame.setIconImage(createFDImage()); 
            } else {
            	//pobranie ikony z pliku
                frame.setIconImage(getFDImage());    
            }
        }

        //pokazanie okna
        frame.setSize(new Dimension(170, 100));
        frame.setVisible(true);
    }

   /** utworzenie kontrolek glownego okna
    *	@return JComponent pojemnik zawierajcy kontrolki
    */
    protected JComponent createOptionControls() {
        JLabel label1 = new JLabel("Decoration options for subsequently created frames:");
        ButtonGroup bg1 = new ButtonGroup();
        JLabel label2 = new JLabel("Icon options:");
        ButtonGroup bg2 = new ButtonGroup();

        //utworzenie przyciskow
        JRadioButton rb1 = new JRadioButton();
        rb1.setText("Look and feel decorated");
        rb1.setActionCommand(LF_DECORATIONS);
        rb1.addActionListener(this);
        rb1.setSelected(true);
        bg1.add(rb1);
        //
        JRadioButton rb2 = new JRadioButton();
        rb2.setText("Window system decorated");
        rb2.setActionCommand(WS_DECORATIONS);
        rb2.addActionListener(this);
        bg1.add(rb2);
        //
        JRadioButton rb3 = new JRadioButton();
        rb3.setText("No decorations");
        rb3.setActionCommand(NO_DECORATIONS);
        rb3.addActionListener(this);
        bg1.add(rb3);
        //
        //
        JRadioButton rb4 = new JRadioButton();
        rb4.setText("Default icon");
        rb4.setActionCommand(DEFAULT_ICON);
        rb4.addActionListener(this);
        rb4.setSelected(true);
        bg2.add(rb4);
        //
        JRadioButton rb5 = new JRadioButton();
        rb5.setText("Icon from a JPEG file");
        rb5.setActionCommand(FILE_ICON);
        rb5.addActionListener(this);
        bg2.add(rb5);
        //
        JRadioButton rb6 = new JRadioButton();
        rb6.setText("Painted icon");
        rb6.setActionCommand(PAINT_ICON);
        rb6.addActionListener(this);
        bg2.add(rb6);

        //dodanie wszystkiego do pojemnika
        Box box = Box.createVerticalBox();
        box.add(label1);        
        //wolne miejce
        box.add(Box.createVerticalStrut(5)); 
        box.add(rb1);
        box.add(rb2);
        box.add(rb3);
        
        //wolne miejsce
        box.add(Box.createVerticalStrut(15)); 
        box.add(label2);
        //wolne miejsce
        box.add(Box.createVerticalStrut(5)); 
        box.add(rb4);
        box.add(rb5);
        box.add(rb6);

        //dodanie pustego miejsca wokol zawartosci okna
        box.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        return box;
    }
    
    /** utworzenie przycisku glownego okna
     *  @return JComponent pojemnik zawierajacy przycisk
     */
    protected JComponent createButtonPane() {
        JButton button = new JButton("New window");
        button.setActionCommand(CREATE_WINDOW);
        button.addActionListener(this);
        //pozniej uzyty do utworzenia domyslnego przycisku
        defaultButton = button; 
        
        //wycentrowanie przycisku i otoczenie wolnym miejscem
        //uzyty domyslny rozklad FlowLayout
        JPanel pane = new JPanel(); 
        pane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        pane.add(button);

        return pane;
    }

    /** ujscie zdarzen generowanych przez przyciski    
     *	@param e nadchodzace zdarzenie ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        //obsluga przycisku New window 
        if (CREATE_WINDOW.equals(command)) {
            showNewWindow();
        
        //obsluga pierwszej grupy radiobuttonow
        } else if (NO_DECORATIONS.equals(command)) {
            noDecorations = true;
            JFrame.setDefaultLookAndFeelDecorated(false);
        } else if (WS_DECORATIONS.equals(command)) {
            noDecorations = false;
            JFrame.setDefaultLookAndFeelDecorated(false);
        } else if (LF_DECORATIONS.equals(command)) {
            noDecorations = false;
            JFrame.setDefaultLookAndFeelDecorated(true);

        //obsluga drugiej grupy radiobuttonow
        } else if (DEFAULT_ICON.equals(command)) {
            specifyIcon = false;
        } else if (FILE_ICON.equals(command)) {
            specifyIcon = true;
            createIcon = false;
        } else if (PAINT_ICON.equals(command)) {
            specifyIcon = true;
            createIcon = true;
        }
    }
    
    /** metoda tworzaca namalowany obrazek ikony 
     *	@return Image namalowany obraz 
     */
    protected static Image createFDImage() {
        //utworzenie obrazka 16x16
        BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);

        //namalowanie czerwonego owalu na czarnym tle
        Graphics g = bi.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 15, 15);
        g.setColor(Color.RED);
        g.fillOval(5, 3, 6, 6);

        //zwolnienie zajetej pamieci
        g.dispose();

        //zwrocenie obrazka
        return bi;
    }

    /** wczytanie obrazka ikony z pliku
     *	@return Image obrazek lub null gdy brak pliku
     */
    protected static Image getFDImage() {
        java.net.URL imgURL = FrameDemo2.class.getResource("images/FD.jpg");
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            return null;
        }
    }

    /**
     * utworzenie i pokazanie GUI 
     *	dla zapewnienia bezpieczenstwa ponizsza metoda 
     *	powinna byc wywolywana z watku rozsylajacego zdarzenia 
     */
    private static void createAndShowGUI() {
        //uzycie Java look and feel.
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }

        //ustawienie ladnego wygladu okien
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        //stworzenie instancji klasy JFrame
        JFrame frame = new JFrame("FrameDemo2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie panelu
        FrameDemo2 demo = new FrameDemo2();

        //dodanie komponentow do panelu
        Container contentPane = frame.getContentPane();
        contentPane.add(demo.createOptionControls(),
                        BorderLayout.CENTER);
        contentPane.add(demo.createButtonPane(),
                        BorderLayout.PAGE_END);
        frame.getRootPane().setDefaultButton(defaultButton);

        //wyswietlenie okna
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setVisible(true);
    }

    /** metoda glowna - rozpoczecie demonstracji
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

	/** wewnetrzna klasa okienka JFrame z nasluchem zdarzen
	 */
    class MyFrame extends JFrame implements ActionListener {

        //utworzenie okna z przyciskiem
        public MyFrame() {
            super("A window");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            //przycisk pozwala na zamkniecie okna bez dekoracji
            JButton button = new JButton("Close window");
            button.addActionListener(this);

            //umieszczenie przycisku w poblizu dolu okna
            Container contentPane = getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane,
                                                BoxLayout.PAGE_AXIS));
			//wypelnienie wolnego miejsca
            contentPane.add(Box.createVerticalGlue()); 
            contentPane.add(button);
            //wycentrowanie poziome
            button.setAlignmentX(Component.CENTER_ALIGNMENT); 
            //wolne miejsce
            contentPane.add(Box.createVerticalStrut(5)); 
        }

        //Make the button do the same thing as the default close operation
        //(DISPOSE_ON_CLOSE).
        /** ujscie zdarzenia od przycisku 
         *	wykonuje domyslna operacje zamkniecia (DISPOSE_ON_CLOSE)
         *  @param e nadchodzace zdarzenie ActionEvent
         */
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }
}
