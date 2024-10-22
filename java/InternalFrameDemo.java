import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.awt.event.*;
import java.awt.*;

/** Przyklad demonstrujacy uzycie komponentu JInternalFrame
 *  InternalFrameDemo.java wymaga nastepujacych plikow:
 *   MyInternalFrame.java
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class InternalFrameDemo extends JFrame
                               implements ActionListener {
    /** panel aplikacji */
    JDesktopPane desktop;

	/** konstruktor bezparametryczny */
    public InternalFrameDemo() {
    	//tytul okna
        super("InternalFrameDemo");

        //glowne okno bedzie mialo odstep 50 pikseli od granic ekranu
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                  screenSize.width  - inset*2,
                  screenSize.height - inset*2);

        //przygotowanie interfejsu uzytkownika
        desktop = new JDesktopPane(); //specjalny panel z warstwami
        createFrame(); //utworzenie pierwszego okienka
        setContentPane(desktop);
        setJMenuBar(createMenuBar());

        //szybkie przesuwanie okien
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }

    /** metoda tworzaca menu aplikacji
     *	@return pasek menu
     */
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //utworzenie jednego menu
        JMenu menu = new JMenu("Document");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        //pierwszy element menu
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //drugi element menu
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }

    /** obsluga zdarzen generowanych przez menu
     *	@param e zdarzenie typu ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
    	//utworzenie nowego okna
        if ("new".equals(e.getActionCommand())) { 
            createFrame();
        //wyjscie z aplikacji
        } else { 
            quit();
        }
    }

    /** utworzenie nowego okna wewnetrznego */
    protected void createFrame() {
        MyInternalFrame frame = new MyInternalFrame();
        frame.setVisible(true); 
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }

    /** opuszczenie aplikacji */
    protected void quit() {
        System.exit(0);
    }

    /**	utworzenie i pokazanie GUI 
     *	dla zapewnienia bezpieczenstwa ponizsza metoda 
     *	powinna byc wywolywana z watku rozsylajacego zdarzenia   
     */
    private static void createAndShowGUI() {
         //ustawienie ladnego wygladu okien
        JFrame.setDefaultLookAndFeelDecorated(true);

		//utworzenie i przygotowanie okna
        InternalFrameDemo frame = new InternalFrameDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //wyswietlenie okna    
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
