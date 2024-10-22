/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

/** przyklad demonstrujacy uzycie dynamicznie zmienianego drzewa 
 *	DynamicTreeDemo.java wymaga nastepujacych plikow:
 *   DynamicTree.java
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class DynamicTreeDemo extends JPanel 
                             implements ActionListener {
    /** przyrostek dla nowego wezla */
    private int newNodeSuffix = 1;
    /** tekst komendy */
    private static String ADD_COMMAND = "add";
    /** tekst komendy */
    private static String REMOVE_COMMAND = "remove";
    /** tekst komendy */
    private static String CLEAR_COMMAND = "clear";
    
    /** panel - drzewo dynamiczne
     *	 patrz: DynamicTree.java 	*/
    private DynamicTree treePanel;

	/** konstruktor bezparametryczny */
    public DynamicTreeDemo() {
        super(new BorderLayout());
        
        //utworzenie komponentow
        treePanel = new DynamicTree();
        populateTree(treePanel);

        JButton addButton = new JButton("Add");
        addButton.setActionCommand(ADD_COMMAND);
        addButton.addActionListener(this);
        
        JButton removeButton = new JButton("Remove");
        removeButton.setActionCommand(REMOVE_COMMAND);
        removeButton.addActionListener(this);
        
        JButton clearButton = new JButton("Clear");
        clearButton.setActionCommand(CLEAR_COMMAND);
        clearButton.addActionListener(this);

        //rozmieszczenie komponentow
        treePanel.setPreferredSize(new Dimension(300, 150));
        add(treePanel, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(addButton);
        panel.add(removeButton);
        panel.add(clearButton);
        add(panel, BorderLayout.LINE_END);
    }

	/** metoda budujaca drzewo
	 *	@param treePanel panel drzewa dynamicznego
	 */
    public void populateTree(DynamicTree treePanel) {
    	//nazwy wezlow
        String p1Name = new String("Parent 1");
        String p2Name = new String("Parent 2");
        String c1Name = new String("Child 1");
        String c2Name = new String("Child 2");

		//utworzenie struktury drzewa
        DefaultMutableTreeNode p1, p2;

        p1 = treePanel.addObject(null, p1Name);
        p2 = treePanel.addObject(null, p2Name);

        treePanel.addObject(p1, c1Name);
        treePanel.addObject(p1, c2Name);

        treePanel.addObject(p2, c1Name);
        treePanel.addObject(p2, c2Name);
    }
    
    /** obsluga zdarzenia akcji generowanego przez przyciski
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (ADD_COMMAND.equals(command)) {            
            //przycisnieto przycisk Add
            treePanel.addObject("New Node " + newNodeSuffix++);
        } else if (REMOVE_COMMAND.equals(command)) {
            //przycisnieto przycisk Remove
            treePanel.removeCurrentNode();
        } else if (CLEAR_COMMAND.equals(command)) {
            //przycisnieto przycisk Clear
            treePanel.clear();
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
        JFrame frame = new JFrame("DynamicTreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        DynamicTreeDemo newContentPane = new DynamicTreeDemo();
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
