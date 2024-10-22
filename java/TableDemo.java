import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Przyklad uzycia komponentu JTable identyczny z SimpleTableDemo oprocz
 * dostosowania modelu tabeli.
 *
 * TableDemo.java nie wymaga dodatkowych plikow
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class TableDemo extends JPanel {

    public TableDemo() {
        //wybrany GridLayout
        super(new GridLayout(1, 0));

        //utworzenie tabeli o dostosowanym modelu
        JTable table = new JTable(new MyTableModel());
        CellRenderer cellRenderer = new CellRenderer();
        table.setColumnSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(1).setPreferredWidth(20);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));

        //utworzenie panelu przewijalnego i dodanie do niego tabeli
        JScrollPane scrollPane = new JScrollPane(table);

        //dodanie przewijalnego panelu do panelu aplikacji
        add(scrollPane);
    }

    /**
     * wewnetrzna klasa definiujaca model tabeli
     */
    class MyTableModel extends AbstractTableModel {

        private final String[] columnNames = {"State", "Parameter"};
        private final Object[][] data;

        MyTableModel() {
            data = new Object[7][2];
            for (int i = 0; i < data.length; i++) {
                if (i % 2 == 1) {
                    data[i][0] = false;
                } else {
                    data[i][0] = true;
                }
                data[i][1] = "parameter #" + i;
            }

        }

        /**
         * @return ilosc kolumn w tabeli
         */
        public int getColumnCount() {
            return columnNames.length;
        }

        /**
         * @return ilosc pozycji w tabeli
         */
        public int getRowCount() {
            return data.length;
        }

        /**
         * @return tytul kolumny o numerze <code>col</code>
         * @param col nr kolumny
         */
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

    }

    /**
     * utworzenie i pokazanie GUI dla zapewnienia bezpieczenstwa ponizsza metoda
     * powinna byc wywolywana z watku rozsylajacego zdarzenia
     */
    private static void createAndShowGUI() {
        //ustawienie ladnego wygladu okien
        JFrame.setDefaultLookAndFeelDecorated(true);

        //utworzenie i przygotowanie okna
        JFrame frame = new JFrame("TableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie i przygotowanie panelu
        TableDemo newContentPane = new TableDemo();
        //panel widoczny
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        //wyswietlenie okna
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * metoda glowna
     *
     * @param args pomijany
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

class CellRenderer extends DefaultTableCellRenderer {

    ImageIcon redLight = null;
    ImageIcon greenLight = null;

    public CellRenderer() {
        this.redLight = createImageIcon("images/button-icon-png-21047.png");
        this.greenLight = createImageIcon("images/button-icon-png-21071.png");
    }

    @Override
    public void setValue(Object value) {
        System.out.println(value);
        if ((Boolean) value) {
            setIcon(greenLight);
        } else {
            setIcon(redLight);
        }
    }

//    public Component getTableCellRendererComponent(JTable table, Object value,
//            boolean isSelected, boolean hasFocus,
//            int row, int column) {
//        System.out.println(value);
//        if ((Boolean) value) {
//            setIcon(greenLight);
//        } else {
//            setIcon(redLight);
//        }
//
//        //setText((String)value);
//        setVisible(true);
//        setOpaque(true);
//
//        if (hasFocus || isSelected) {
//            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
//        } else {
//            setBorder(null);
//        }
//
//        return this;
//    }
    static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ButtonHtmlDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
