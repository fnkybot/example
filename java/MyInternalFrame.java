import javax.swing.JInternalFrame;

import java.awt.event.*;
import java.awt.*;

/** klasa uzywana przez przyklad InternalFrameDemo
 *	reprezentuje wewnetrzne okienko
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class MyInternalFrame extends JInternalFrame {
	/** licznik okienek */
    static int openFrameCount = 0;
    /** polozenie okienka */
    static final int xOffset = 30, yOffset = 30;

	/** konstruktor bezparametryczny */
    public MyInternalFrame() {
        //tytul numerowany wart. licznika
        super("Document #" + (++openFrameCount), 
              true, //zmienny rozmiar 
              true, //mozna zamknac 
              true, //mozna maksymalizowac 
              true);//mozna minimalizowac 

        //tu mozna utworzyc jakis interfejs uzytkownika w okienku

        //ustalenie rozmiarow, mozna tez wywolac metode pack
        setSize(300,300);

        //ustawienie polozenia okna
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
    }
}
