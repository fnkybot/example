package pl.polsl.model;

/**
 *
 * @author Ania Glodek
 * @version 1.0
 * 
 */
public class Model {
    private double firstNumber;
    private double secondNumber;
    private double resultVal;
    
    public Model(){
        this.firstNumber = 0;
        this.secondNumber = 0;
        this.resultVal = 0;
    }
    
    public void setFirstNumber(double firstNumber){
        this.firstNumber = firstNumber;
    }
    public void setSecondNumber(double secondNumber){
        this.secondNumber = secondNumber;
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
        this.resultVal = this.firstNumber + this.secondNumber;
    }
}