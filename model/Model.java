package pl.polsl.model;

import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Ania Glodek
 * @version 1.0
 * 
 */
public class Model {
    private SwingPropertyChangeSupport swingPropChangeFirer;
    private double firstNumber;
    private double secondNumber;
    private double resultVal;
    
    public Model(){
        this.firstNumber = 0;
        this.secondNumber = 0;
        this.resultVal = 0;
        swingPropChangeFirer = new SwingPropertyChangeSupport(this);
    }
    
    public void addListener(PropertyChangeListener prop){
        swingPropChangeFirer.addPropertyChangeListener(prop);
    }
    public void setFirstNumber(double firstNumber){
        double oldVal = this.firstNumber;
        this.firstNumber = firstNumber;
        swingPropChangeFirer.firePropertyChange("firstNumber", oldVal, firstNumber);
    }
    public void setSecondNumber(double secondNumber){
        double oldVal = this.secondNumber;
        this.secondNumber = secondNumber;
        swingPropChangeFirer.firePropertyChange("secondNumber", oldVal, secondNumber);
    }
    public double getFirstNumber(){
        return this.firstNumber;
    }
    public double getSecondNumber(){
        return this.secondNumber;
    }
    public double getResultVal(){
        return this.resultVal;
    }
    public void calculate() {
        double oldVal = this.resultVal;
        this.resultVal = this.firstNumber + this.secondNumber;
        swingPropChangeFirer.firePropertyChange("resultVal", oldVal, resultVal);
    }
}
