/** uzycie klasy SwingWorker do wykonania czasochlonnego zadania 
 *	na potrzeby przykladow:
 *   ProgressBarDemo.java
 *   ProgressBarDemo2.java
 *   ProgressMonitorDemo
 * <br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4">
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4</a>
 */
public class LongTask {
	/** dlugosc zadania w nieokreslonych jednostkach */
    private int lengthOfTask;
    /** wykonana czesc zadania */
    private int current = 0;
    /** true gdy zadanie skonczone */
    private boolean done = false;
    /** true gdy zadanie anulowano */
    private boolean canceled = false;
    /** napis zawierajacy informacje o stanie zadania */
    private String statMessage;

	/** konstruktor bezparametrowy
	 *	obliczenie dlugosci zadania
	 *	prawdziwy program wyznaczalby np. ilosc bajtow do odczytu
	 */
    public LongTask() {
        lengthOfTask = 1000;
    }

    /** metoda wywolywana z ProgressBarDemo by rozpoczac zadanie
     */
    public void go() {
        final SwingWorker worker = new SwingWorker() {
            public Object construct() {
                current = 0;
                done = false;
                canceled = false;
                statMessage = null;
                return new ActualTask();
            }
        };
        worker.start();
    }

    /**	metoda wywolywana z ProgressBarDemo dla sprawdzenia 
     *	jak wielkie jest zadanie do wykonania     
     *	@return dlugosc zadania
     */
    public int getLengthOfTask() {
        return lengthOfTask;
    }

    /**	metoda wywolywana z ProgressBarDemo dla sprawdzenia 
     *	jak wiele zosalo wykonane
     *	@return ilosc wykonanych jednostek
     */
    public int getCurrent() {
        return current;
    }

	/** zatrzymanie zadania */
    public void stop() {
        canceled = true;
        statMessage = null;
    }

    /**	metoda wywolywana z ProgressBarDemo dla sprawdzenia czy
     *	zadanie zostalo wykonane
     *	@return true gdy zadanie wykonane
     */
    public boolean isDone() {
        return done;
    }

    /**	metoda zwracajaca aktualna informacje o stanie zadania
     *	lub null gdy brak wiadomosci
     *	@return napis zawierajacy informacje
     */
    public String getMessage() {
        return statMessage;
    }

    /**	wewnetrzna klasa reprezentujaca wlasciwe czasochlonne zadanie
     */
    class ActualTask {
    	/** konstruktor bezparametrowy
    	 */
        ActualTask() {
        	//symulacja czasochlonnego zadania przez 
        	//generowanie losowego postepu zadania co sekunde.
            while (!canceled && !done) {
                try {
                    //uspienie watku na sekunde
                    Thread.sleep(1000); 
                    //dodanie pewnego postepu
                    current += Math.random() * 100; 
                    //otrzymanie pozadanego rozkladu zmiennej losowej
                    //metoda eliminacji 
                    if (current >= lengthOfTask) {
                        done = true;
                        current = lengthOfTask;
                    }
                    //przygotowanie informacji o stanie zadania
                    statMessage = "Completed " + current +
                                  " out of " + lengthOfTask + ".";
                } catch (InterruptedException e) {
                    System.out.println("ActualTask interrupted");
                }
            }
        }
    }
}
