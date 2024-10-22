
package debicki.teslaapp.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import debicki.teslaapp.model.*;

/**
 * This class is responsible for GUI view
 * 
 * @author Wojciech-Dębicki
 * @version 0.1
 */
public class UiView {
    private JFrame frame;
    private JPanel examplesPanel;
    private JLabel description;
    private TeslaMath teslaMath;
    public JButton countButton;
    public JTextField firstNumber;
    public JTextField secondNumber;
    public JTextField thirdNumber;
    public JLabel firstExampleValue;
    public JLabel secondExampleValue;
    public JLabel thirdExampleValue;
    public JLabel resultVal;
    
    public UiView(TeslaMath teslaMath) {
        this.teslaMath = teslaMath;
        prepareGUI();
    }
    public void updateFirstNumber(){
        this.firstNumber.setText(Double.toString((Double)teslaMath.getFirstNumber()));
    }
    public void updateSecondNumber(){
        this.secondNumber.setText(Double.toString((Double)teslaMath.getSecondNumber()));
    }
    public void updatethirdNumber(){
        this.thirdNumber.setText(Double.toString((Double)teslaMath.getSecondNumber()));
    }
    public void updateResultVal(){
        this.resultVal.setText(Double.toString((Double)teslaMath.getResultVal()));
    }
    
    private void prepareGUI(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("TeslaApp");
        examplesPanel = new JPanel();
        
        firstNumber = new JTextField();
        secondNumber = new JTextField(); 
        thirdNumber = new JTextField(); 
        
        countButton = new JButton ("Check"); 
        
        firstExampleValue = new JLabel("Stock Count 1.1.2012:");
        secondExampleValue = new JLabel("Stock Count 1.1.2013:");
        thirdExampleValue = new JLabel("Stock Count 1.1.2014:");
        
        resultVal = new JLabel("");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 250));
        frame.setLayout(new GridLayout(3, 2));
        frame.setResizable(false);
        
        examplesPanel.setLayout(new GridLayout(3, 2, 3, 3));
        
        examplesPanel.add(firstExampleValue);
        examplesPanel.add(firstNumber);
        
        examplesPanel.add(secondExampleValue);
        examplesPanel.add(secondNumber);
        
        examplesPanel.add(thirdExampleValue);
        examplesPanel.add(thirdNumber);
        
        frame.add(resultVal);
        frame.add(examplesPanel);
        frame.add(countButton);
        //
        
        frame.setVisible(true);
    }
   
}
