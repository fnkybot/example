package pl.polsl.view;

import java.io.PrintWriter;
import pl.polsl.model.*;


/**
 *
 * @author Ania Glodek
 * @version 1.0
 * 
 */
public class View {
    private Model model;
    
    public View(Model model) {
        this.model = model;
    }
    
    public void printHtmlTemplateStart(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Results</title>");
        out.println("</head>");
        out.println("<body>");
    }
    public void printResult(PrintWriter out){
        out.println("<table>");
        out.println("<tr>");
        out.println("<td>" + model.getResultVal()+ "</td>");
        out.println("</tr>");
    }
    public void printHtmlTemplateEnd(PrintWriter out) {
        out.println("</body>");
        out.println("</html>");
    }
    public String getFirstNumber(String firstNumber, PrintWriter out) {
        return firstNumber;
    }
    public String getSecondNumber(String secondNumber, PrintWriter out) {
        return secondNumber;
    }
}
