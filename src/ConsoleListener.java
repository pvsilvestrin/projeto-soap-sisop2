import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: paulo
 * Date: 6/20/12
 * Time: 11:25 AM
 */
public class ConsoleListener  implements ActionListener
{
    // Console is an intelligent terminal that reads an entire command
    // line and then generates an interrupt. It should provide a method
    // for the kernel to read the command line.

    private IntController hint;
    private String l;
    public void setInterruptController(IntController i)
    {
        hint = i;
    }

    public void actionPerformed(ActionEvent e)
    {
        l = e.getActionCommand();
        JTextField line = (JTextField)e.getSource();
        line.setText("");
        // Here goes the code (2 lines) that generates an interrupt
        //hint.P();
        hint.set(15);
    }

    public String getLine()
    {
        return l;
    }
}