/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */

import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/** klasa hermetyzujaca panel z dynamicznie zmienianym drzewem
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class DynamicTree extends JPanel {
    /** wezel korzen drzewa */
    protected DefaultMutableTreeNode rootNode;
    /** model drzewa */
    protected DefaultTreeModel treeModel;
    /** komponent drzewa */
    protected JTree tree;
    /** narzedzia awt (uzyte do wywolania sygnalu alarmu) */
    private Toolkit toolkit = Toolkit.getDefaultToolkit();

	/** konstruktor bezparametryczny */
    public DynamicTree() {
        super(new GridLayout(1,0));
        
        //standardowa obsluga drzewa
        rootNode = new DefaultMutableTreeNode("Root Node");
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());

        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    /** usuniecie wszystkich wezlow oprocz korzenia
     */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    /** usuniecie obecnie wybranego wezla
     */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        } 

        //alarm gdy nie bylo wybranego wezla badz byl to korzen
        toolkit.beep();
    }

    /**	metoda dodajaca nowy wezel do aktualnie wybranego
	 *	@param child wstawiany wezel
	 *	@return wstawiony wezel
	 */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
                         (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

	/**	metoda dodajaca nowy wezel
	 *	@param parent wezel rodzic dla wstawianego
	 *	@param child wstawiany wezel
	 *	@return wstawiony wezel
	 */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child) {
        return addObject(parent, child, false);
    }

	/**	metoda dodajaca nowy wezel
	 *	@param parent wezel rodzic dla wstawianego
	 *	@param child wstawiany wezel
	 *	@param shouldBeVisible true gdy wezel ma byc widoczny
	 *	@return wstawiony wezel
	 */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child, 
                                            boolean shouldBeVisible) {
        //nowy wezel
        DefaultMutableTreeNode childNode = 
                new DefaultMutableTreeNode(child);

        //gdy bez przodka ustaw przodka na korzen drzewa
        if (parent == null) {
            parent = rootNode;
        }

        //uwzglednienie wezla w modelu drzewa
        treeModel.insertNodeInto(childNode, parent, 
                                 parent.getChildCount());

        //pokaz jezeli wezel powinien byc widoczny dla uzytkownika
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    /** klasa sluchacza dla interfejsu TreeModelListener
     */
    class MyTreeModelListener implements TreeModelListener {
    	
    	/** obsluga zdarzenia zmiany (np. nazwy) wezla */
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)
                     (e.getTreePath().getLastPathComponent());                     
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)
                       (node.getChildAt(index));
            } catch (NullPointerException exc) {}

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }
        
        /** brak obslugi */
        public void treeNodesInserted(TreeModelEvent e) {}
        /** brak obslugi */
        public void treeNodesRemoved(TreeModelEvent e) {}
        /** brak obslugi */
        public void treeStructureChanged(TreeModelEvent e) {}
    }
}
