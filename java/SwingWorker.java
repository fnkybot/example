import javax.swing.SwingUtilities;

/**
 * 	abstrakcyjna nadklasa dla tworzenia specjalizowanych watkow 
 *	wykonujacych czasochlonne operacje
 *	po wykonaniu metody construct wykonywana jest metoda finished
 *	instrukcje i przyklady mozna znalezc na stronie www
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/uiswing/misc/threads.html">
 * http://java.sun.com/docs/books/tutorial/uiswing/uiswing/misc/threads.html</a>
 */
public abstract class SwingWorker {
	/** @see: getValue(), setValue() */
    private Object value;  

    /** 
     *	wewnetrzna klasa utrzymujaca referencje do obecnego watku roboczego
     */
    private static class ThreadVar {
    	/** watek */
        private Thread thread;
        /** konstruktor kopiujacy */
        ThreadVar(Thread t) { thread = t; }
        /** metoda zwracajaca referencje do przechowywanego watku 
         *	@return przechowywany watek 
         */
        synchronized Thread get() { return thread; }
        /**	metoda usuwajaca przechowywany watek
         */
        synchronized void clear() { thread = null; }
    }

	/** watek roboczy */
    private ThreadVar threadVar;

    /** 
     *	odczytanie wartosci zwroconej przez watek roboczy
     *	zwraca null gdy nie watek nie zostal jeszcze skonstruowany
     *	@return wartosc zwrocona przez watek 
     */
    protected synchronized Object getValue() { 
        return value; 
    }

    /** 
     *	ustawienie wartosci generowanej przez watek roboczy     	
     *	@param x nowa wartosc generowana przez watek roboczy
     */
    private synchronized void setValue(Object x) { 
        value = x; 
    }

    /** 
     *	oblicznienie wartosci zwracanej przez metode <code>get</code>
     */
    public abstract Object construct();

    /**
     *	metoda wywolywana przez watek rozsylajacy zdarzenia
     *	po zakonczeniu metody <code>construct</code>
     */
    public void finished() {
    }

    /**
     *	metoda wymuszajaca przerwanie dzialania watku roboczego
     */
    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }

    /**
     *	metoda zwracajaca wartosc zwaracana przez metode <code>construct</code>
     *	zwraca null gdy watek jest konstruowany lub przerwany przed 
     *	wygenerowaniem wartosci
     *      
     *	@return wartosc zwaracana przez metode <code>construct</code>     
     */
    public Object get() {
        while (true) {  
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            }
            catch (InterruptedException e) {
            	//propagacja wyjatku
                Thread.currentThread().interrupt(); 
                return null;
            }
        }
    }


    /**
     *	konstruktor bezargumentowy 
     *	rozpoczyna watek ktory wywola metode <code>construct</code> 
     *	i zostanie zakonczony
     */
    public SwingWorker() {
    	/**  wewnetrzny anonimowy watek wywolujacy metode 
    	 *	<code>finished</code> 
    	 */
        final Runnable doFinished = new Runnable() {
           public void run() { finished(); }
        };
        
		/**  wewnetrzny anonimowy watek wywolujacy metode 
    	 *	<code>construct</code> i przypisujacy polu <code>value</code>
    	 *	zwracana przez nia wartosc po czy wywolujacy metode 
    	 *	<code>finished</code>
    	 */        
        Runnable doConstruct = new Runnable() { 
            public void run() {
                try {
                    setValue(construct());
                }
                finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    /** rozpoczecie watku roboczego     
     */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}
