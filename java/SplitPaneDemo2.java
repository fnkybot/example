import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

/** Przyklad demonstrujacy zagniezdzanie komponentow JSplitPane
 *	SplitPaneDemo2.java wymaga nastepujacych plikow:
 *	 SplitPaneDemo.java
 *	 imagenames.properties i wymienione w nim obrazki
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class SplitPaneDemo2 extends JFrame
                            implements ListSelectionListener {
    
    /** tekst w dolnej czesci ekranu */
    private JLabel label;

    /** konstruktor bezparametryczny */
    public SplitPaneDemo2() {
    	//tytul okna
        super("SplitPaneDemo2");

        //utworzenie obiektu SplitPaneDemo
        SplitPaneDemo splitPaneDemo = new SplitPaneDemo();
        JSplitPane top = splitPaneDemo.getSplitPane();
        splitPaneDemo.getImageList().addListSelectionListener(this);

        //ustawienie ramki wewnetrznego JSplitPane na null w celu
        //obejscia bledu oznaczonego Bug #4131528        
        top.setBorder(null);

		//zwykla etykieta tekstowa
        label = new JLabel("Click on an image name in the list.",
                           JLabel.CENTER);

        //utworzenie nowego JSplitPane i umieszczenie w nim "top" oraz
        //etykiety z napisem
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                              top, label);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(180);

        //ustawienie rozmiarow minimalnych dla komponentow
        top.setMinimumSize(new Dimension(100, 50));
        label.setMinimumSize(new Dimension(100, 30));

        //dodanie komponentow do panelu okna
        getContentPane().add(splitPane);
    }

	/** obsluga zdarzenia zmiany wybranego elementu w liscie 
	 *	@param e zdarzenie
	 */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;
		
		//dostep do aktualnie wybranego elementu
        JList theList = (JList)e.getSource();
        if (theList.isSelectionEmpty()) {
            label.setText("Nothing selected.");
        } else {
            int index = theList.getSelectedIndex();
            label.setText("Selected image number " + index);
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
        JFrame frame = new SplitPaneDemo2();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
