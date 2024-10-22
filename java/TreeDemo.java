import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;

/** Przyklad demonstrujacy uzycie komponentu JTree
 *	w formie drzewa przedstawione sa odnosniki do 'ksiazek'
 *	zapisanych w osobnych plikach html
 * 	TreeDemo.java wymaga nastepujacych plikow:
 *    TreeDemoHelp.html
 *    arnold.html
 *    bloch.html
 *    chan.html
 *    jls.html
 *    swingtutorial.html
 *    tutorial.html
 *    tutorialcont.html
 *    vm.html 
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class TreeDemo extends JPanel
                      implements TreeSelectionListener {
    /** panel wyswietlajacy html */
    private JEditorPane htmlPane;
    /** komponent - drzewo */
    private JTree tree;
    /** odnosnik do html zawierajacego pomoc aplikacji */
    private URL helpURL;
    /** true gdy tryb odpluskwiania */
    private static boolean DEBUG = false;

    /** opcjonalne zmiany sylu linii, mozliwe wartosci:
     *	"Angled" (domyslna), "Horizontal", "None" */     
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
        
    /** opcjonalne ustawienie look and feel */
    private static boolean useSystemLookAndFeel = false;

    public TreeDemo() {
        super(new GridLayout(1,0));

        //utworzenie wezlow glowny i podwezly
        DefaultMutableTreeNode top =
            new DefaultMutableTreeNode("The Java Series");
        createNodes(top);

        //utworzenie drzewa z mozliwoscia wyboru 1 elementu
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //nasluch zmiany wybranego elementu
        tree.addTreeSelectionListener(this);

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        //utworzenie panelu przewijalnego i dodanie do niego drzewa
        JScrollPane treeView = new JScrollPane(tree);

        //utworzenie panelu wyswietlajacego html
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        //zaladowanie tekstu pomocy
        initHelp();
        //przewijalny
        JScrollPane htmlView = new JScrollPane(htmlPane);

        //dodanie paneli przewijalnych do panelu z podzialem
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100); 	//XXX: nie dziala w pewnych
        									//wersjach Swing, bug 4101306
        //obejscie bledu 4101306:
        //treeView.setPreferredSize(new Dimension(100, 100)); 

        splitPane.setPreferredSize(new Dimension(500, 300));

        //dodanie panelu z podzialem do panelu biezacej klasy
        add(splitPane);
    }

    /** nasluch dla interfejsu TreeSelectionListener 
     *	obsluga zdarzenia zmiany wybranego elementu drzewa 
     */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           tree.getLastSelectedPathComponent();

        if (node == null) return;
		
		//wyluskanie obiektu przechowywanego w wezle
        Object nodeInfo = node.getUserObject();
        //jesli wezel jest lisciem drzewa wyswietlenie
        //odpowiedniej ksiazki
        if (node.isLeaf()) {
            BookInfo book = (BookInfo)nodeInfo;
            displayURL(book.bookURL);
            if (DEBUG) {
                System.out.print(book.bookURL + ":  \n    ");
            }
        } else {
        	//jesli nie lisc wyswietlenie pomocy
            displayURL(helpURL); 
        }
        if (DEBUG) {
            System.out.println(nodeInfo.toString());
        }
    }

    /** klasa hermetyzujaca informacje dotyczace pojedynczej 'ksiazki'
     */
    private class BookInfo {
    	/** tytul ksiazki */
        public String bookName;
        /** odnosnik do html ksiazki */
        public URL bookURL;

        /** konstruktor 
         *	@param book tytul ksiazki
         *	@param filename nazwa pliku html zawierajacego tekst ksiazki 
         */
        public BookInfo(String book, String filename) {
            bookName = book;
            bookURL = TreeDemo.class.getResource(filename);
            if (bookURL == null) {
                System.err.println("Couldn't find file: "
                                   + filename);
            }
        }

        /** przedefiniowana metoda klasy Object jej implementacja
         *	jest konieczna do wyswietlenia w widoku drzewa
         *	zadanej informacji - tu tytulu ksiazki
         *	@return tytul ksiazki 
         */
        public String toString() {
            return bookName;
        }
    }

    /** metoda wyswietlajaca w panelu html pomocy
     */
    private void initHelp() {
        String s = "TreeDemoHelp.html";
        helpURL = TreeDemo.class.getResource(s);
        if (helpURL == null) {
            System.err.println("Couldn't open help file: " + s);
        } else if (DEBUG) {
            System.out.println("Help URL is " + helpURL);
        }

        displayURL(helpURL);
    }

    /** metoda wyswietlajaca w panelu html ksiazki 
     *	@param url odnosnik do pliku html ksiazki
     */
    private void displayURL(URL url) {
        try {
            if (url != null) {
                htmlPane.setPage(url);
            } else { //null==url
		htmlPane.setText("File Not Found");
                if (DEBUG) {
                    System.out.println("Attempted to display a null URL.");
                }
            }
        } catch (IOException e) {
            System.err.println("Attempted to read a bad URL: " + url);
        }
    }

	/** utworzenie wezlow drzewa
	 *	@param top glowny wezel drzewa
	 */
    private void createNodes(DefaultMutableTreeNode top) {
        //ksiazki pogrupowane sa w kategorie
        //dodanie calego zbioru po jednej ksiazce do kolejnej kategorii
        
        DefaultMutableTreeNode category = null;        
        DefaultMutableTreeNode book = null;

		//pierwsza kategoria ksiazek
        category = new DefaultMutableTreeNode("Books for Java Programmers");
        top.add(category);

        //Tutorial Javy - pierwsza ksiazka
        book = new DefaultMutableTreeNode(new BookInfo
            ("The Java Tutorial: A Short Course on the Basics",
            "tutorial.html"));
        category.add(book);

        //Tutorial c.d. - druga
        book = new DefaultMutableTreeNode(new BookInfo
            ("The Java Tutorial Continued: The Rest of the JDK",
            "tutorialcont.html"));
        category.add(book);

        //JFC Swing Tutorial
        book = new DefaultMutableTreeNode(new BookInfo
            ("The JFC Swing Tutorial: A Guide to Constructing GUIs",
            "swingtutorial.html"));
        category.add(book);

        //Bloch
        book = new DefaultMutableTreeNode(new BookInfo
            ("Effective Java Programming Language Guide",
	     "bloch.html"));
        category.add(book);

        //Arnold/Gosling
        book = new DefaultMutableTreeNode(new BookInfo
            ("The Java Programming Language", "arnold.html"));
        category.add(book);

        //Chan
        book = new DefaultMutableTreeNode(new BookInfo
            ("The Java Developers Almanac",
             "chan.html"));
        category.add(book);

		//kolejna kategoria ksiazek
        category = new DefaultMutableTreeNode("Books for Java Implementers");
        top.add(category);

        //VM
        book = new DefaultMutableTreeNode(new BookInfo
            ("The Java Virtual Machine Specification",
             "vm.html"));
        category.add(book);

        //Language Spec
        book = new DefaultMutableTreeNode(new BookInfo
            ("The Java Language Specification",
             "jls.html"));
        category.add(book);
    }
        
    /**	utworzenie i pokazanie GUI 
     *	dla zapewnienia bezpieczenstwa ponizsza metoda 
     *	powinna byc wywolywana z watku rozsylajacego zdarzenia   
     */
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //ustawienie ladnego wygladu okien
        JFrame.setDefaultLookAndFeelDecorated(true);

        //utworzenie i przygotowanie okna
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        TreeDemo newContentPane = new TreeDemo();
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