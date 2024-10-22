import javax.swing.*;
import javax.swing.SpringLayout;
import java.awt.*;

/**	Metody narzedziowe sluzace do tworzenia rozkladow 
 *	formatek i siatek opartych na SpringLayout
 *	uzywane przez rozne programy np. SpringBox, SpringCompactGrid.
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class SpringUtilities {
    /**
     *	metoda wypisujaca na stdout minimalny, preferowany i maksymalny
     *	rozmiar komponentu
     *	@param c wskazany komponent
     */
    public static void printSizes(Component c) {
        System.out.println("minimumSize = " + c.getMinimumSize());
        System.out.println("preferredSize = " + c.getPreferredSize());
        System.out.println("maximumSize = " + c.getMaximumSize());
    }

    /**	metoda rozmieszczajaca w siatce pierwsze <code>rows</code> 
     *	rzedow w <code>cols</code> kolumnach komponentow zawartych w 
     *	kontenerze <code>parent</code>. Kazdy komponent jest tak duzy
     *	jak preferowany rozmiar najwiekszego z komponentow. Rozmiar kontenera
     *	jest dostosowany by pomiescic wszystkie potomne komponenty.
     *
     *	@param parent kontener zawierajacy rozmieszczane komponenty
     * 	@param rows ilosc wierszy
     * 	@param cols ilosc kolumn
     * 	@param initialX poczatkowa wspolrzedna x siatki
     * 	@param initialY poczatkowa wspolrzedna y siatki
     * 	@param xPad odleglosc pozioma miedzy komorkami
     * 	@param yPad odleglosc pionowa miedzy komorkami
     */
    public static void makeGrid(Container parent,
                                int rows, int cols,
                                int initialX, int initialY,
                                int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeGrid must use SpringLayout.");
            return;
        }

        Spring xPadSpring = Spring.constant(xPad);
        Spring yPadSpring = Spring.constant(yPad);
        Spring initialXSpring = Spring.constant(initialX);
        Spring initialYSpring = Spring.constant(initialY);
        int max = rows * cols;
        
        //Obliczenie maksymalnej szzerokosci/wysokosci tak by
        //wszystkie komorki mialy ten sam rozmiar
        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).
                                    getWidth();
        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).
                                    getWidth();
        for (int i = 1; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                            parent.getComponent(i));

            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
        }

        //zatwierdzenie nowej szerokosci/dlugosci dla komponentow
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                            parent.getComponent(i));

            cons.setWidth(maxWidthSpring);
            cons.setHeight(maxHeightSpring);
        }

        //dopasowanie granic x/y wszystkich komorek tak by 
        //byly rozmieszczone w siatce
        SpringLayout.Constraints lastCons = null;
        SpringLayout.Constraints lastRowCons = null;
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                                 parent.getComponent(i));
            if (i % cols == 0) { //nowy wiersz 
                lastRowCons = lastCons;
                cons.setX(initialXSpring);
            } else { //pozycja x zalezy od poprzedniego komponentu
                cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST),
                                     xPadSpring));
            }

            if (i / cols == 0) { //pierwszy wiersz
                cons.setY(initialYSpring);
            } else { //pozycja y zalezy od poprzedniego wiersza
                cons.setY(Spring.sum(lastRowCons.getConstraint(SpringLayout.SOUTH),
                                     yPadSpring));
            }
            lastCons = cons;
        }

        //okreslenie rozmiaru kontenera-rodzica
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH,
                            Spring.sum(
                                Spring.constant(yPad),
                                lastCons.getConstraint(SpringLayout.SOUTH)));
        pCons.setConstraint(SpringLayout.EAST,
                            Spring.sum(
                                Spring.constant(xPad),
                                lastCons.getConstraint(SpringLayout.EAST)));
    }

    /** metoda pomocnicza dla makeCompactGrid.
     *	zwraca granice okreslonego parametrami komponentu */
    private static SpringLayout.Constraints getConstraintsForCell(
                                                int row, int col,
                                                Container parent,
                                                int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**	metoda rozmieszczajaca w siatce pierwsze <code>rows</code> 
     *	rzedow w <code>cols</code> kolumnach komponentow zawartych w 
     *	kontenerze <code>parent</code>. Kazdy komponent jest tak szeroki
     *	jak preferowany rozmiar najszerszego z komponentow w kolumnie,
     *	podobnie wyznaczana jest wysokosc dla kazdego wiersza.
     *	Rozmiar kontenera jest dostosowany by pomiescic 
     *	wszystkie potomne komponenty.
     *
     *	@param parent kontener zawierajacy rozmieszczane komponenty
     * 	@param rows ilosc wierszy
     * 	@param cols ilosc kolumn
     * 	@param initialX poczatkowa wspolrzedna x siatki
     * 	@param initialY poczatkowa wspolrzedna y siatki
     * 	@param xPad odleglosc pozioma miedzy komorkami
     * 	@param yPad odleglosc pionowa miedzy komorkami
     */
    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

		//wyrownanie komorek w kazdej kolumnie i nadanie im rownej szerokosci
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                                   getConstraintsForCell(r, c, parent, cols).
                                       getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //wyrownanie komorek w kazdym wierszu i nadanie im rownej wysokosci
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                                    getConstraintsForCell(r, c, parent, cols).
                                        getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //okreslenie rozmiaru kontenera-rodzica
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }
}
