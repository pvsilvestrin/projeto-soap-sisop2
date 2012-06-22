import javax.swing.*;
import java.awt.*;

/**
 * User: paulo
 * Date: 6/20/12
 * Time: 11:24 AM
 */
class MyWin extends JFrame
{
    private ConsoleListener ml;
    private JTextField line;

    public MyWin()
    {
        super("Console");
        Container c = getContentPane();
        c.setLayout(new FlowLayout());

        line = new JTextField(60);
        line.setEditable(true);
        c.add(line);

        ml = new ConsoleListener();
        line.addActionListener(ml);

        setSize(800,80);
        setLocation(100,0);
        setVisible(true);
    }

    public ConsoleListener getListener() {
        return ml;
    }

}