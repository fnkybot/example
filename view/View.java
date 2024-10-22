package pl.polsl.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import pl.polsl.model.*;

/**
 *
 * @author Ania Glodek
 * @version 1.0
 * 
 */
public class View {
    private JFrame frame;
    private JPanel resultPanel;
    private JLabel description;
    private Model model;
    public JButton countButton;
    public JTextField firstNumber;
    public JTextField secondNumber;
    public JLabel resultVal;
    
    public View(Model model) {
        this.model = model;
        prepareGUI();
    }
    public void updateFirstNumber(){
        this.firstNumber.setText(Double.toString((Double)model.getFirstNumber()));
    }
    public void updateSecondNumber(){
        this.secondNumber.setText(Double.toString((Double)model.getSecondNumber()));
    }
    public void updateResultVal(){
        this.resultVal.setText(Double.toString((Double)model.getResultVal()));
    }
    
    private void prepareGUI(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("App");
        resultPanel = new JPanel();
        firstNumber = new JTextField();
        secondNumber = new JTextField(); 
        countButton = new JButton ("Count"); 
        description = new JLabel("Sum:");
        resultVal = new JLabel("0");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(150, 150));
        frame.setLayout(new GridLayout(4,1,5,5));
        frame.setResizable(false);
        resultPanel.setLayout(new GridLayout(1,2));
        
        frame.add(firstNumber);
        frame.add(secondNumber);
        frame.add(countButton);
        frame.add(resultPanel);
        resultPanel.add(description);
        resultPanel.add(resultVal);
        frame.setVisible(true);
    }
   
}
