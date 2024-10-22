package pl.polsl.Controller;

import java.awt.event.ActionEvent;
import pl.polsl.view.*;
import pl.polsl.model.*;
import javax.swing.AbstractAction;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author Ania Glodek
 * @version 1.0
 * 
 */
public class Controller  implements PropertyChangeListener{
    private Model model;
    private View view;
    
    public Controller() {
        this.model = new Model();
        this.view = new View(model);
        this.model.addListener(this);
        this.viewEvent();
    }
    
    private void viewEvent(){
        view.countButton.setAction(new AbstractAction("Count"){
            @Override
            public void actionPerformed(ActionEvent arg0){
                // Tutaj powinniśmy robić parsowanie danych i sprawdzenie ich poprawności
                model.setFirstNumber(Double.parseDouble(view.firstNumber.getText()));
                model.setSecondNumber(Double.parseDouble(view.secondNumber.getText()));
                model.calculate();
            }
        });
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        
        if("firstNumber".equalsIgnoreCase(propName)){
            view.updateFirstNumber();
        }
        else if ("secondNumber".equalsIgnoreCase(propName)){
            view.updateSecondNumber();
        }
        else if ("resultVal".equalsIgnoreCase(propName)){
            view.updateResultVal();
        }
    } 
}
