import javax.swing.SpinnerModel;
import javax.swing.SpinnerListModel;

/**
 *	Przyklad uzywany przez programy typu SpinnerDemo.
 *	Definiuje podklase modelu SpinnerListModel dzialajaca z 
 *	tablica obiektow i okresla cykliczne przewijanie listy
 *	(nastepny i poprzedni element w spinnerze nie sa nigdy null).
 *	Pozwala rowniez na opcjonalne polaczenie z podlaczonym modelem 
 *	spinnera, odswiezajac go gdy wystapi cykl.
 *	Programy SpinnerDemo uzywaja CyclingSpinnerListModel w spinnerze
 *	zawierajacym nazwy miesiecy, ktory jest (w SpinnerDemo3) polaczony
 *	z spinnerem zawierajacym numery lat, co powoduje np. przy 
 *	zmianie miesiaca z December na January inkrementacje roku.
 *
 */
public class CyclingSpinnerListModel extends SpinnerListModel {
  	
  	/** krancowa wartosc z tablicy */
    Object firstValue, lastValue;
    /** model podlaczony do spinnera */
    SpinnerModel linkedModel = null;

	/** konstruktor 
	 *	@param values tablica obiektow przedstawianych w spinnerze 
	 */
    public CyclingSpinnerListModel(Object[] values) {
        super(values);        
        //ustawienie wartosci krancowych
        firstValue = values[0];
        lastValue = values[values.length - 1];
    }

	/** metoda rejestrujaca podlaczony model spinnera 
	 *	@param linkedModel model spinnera
	 */
    public void setLinkedModel(SpinnerModel linkedModel) {
        this.linkedModel = linkedModel;
    }

	/** metoda implemetujaca pobranie kolejnej wartosci 
	 *	z listy obiektow tak by wystepowal cykl
	 *	@return obiekt nastepny 
	 */
    public Object getNextValue() {
        Object value = super.getNextValue();
        if (value == null) {
            value = firstValue;
            if (linkedModel != null) {
                linkedModel.setValue(linkedModel.getNextValue());
            }
        }
        return value;
    }
    
	/** metoda implemetujaca pobranie poprzedniej wartosci 
	 *	z listy obiektow tak by wystepowal cykl
	 *	@return obiekt poprzedni
	 */    
    public Object getPreviousValue() {
        Object value = super.getPreviousValue();
        if (value == null) {
            value = lastValue;
            if (linkedModel != null) {
                linkedModel.setValue(linkedModel.getPreviousValue());
            }
        }
        return value;
    }
}
