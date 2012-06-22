import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * User: paulo
 * Date: 6/20/12
 * Time: 6:45 PM
 */
public class GraphicWindow extends JFrame
{
    private ConsoleListener ml;
    private JTextField line;

    public GraphicWindow(MyWin relative, GraphicsSurface graphicsSurface)
    {
        super("Disk and Processor Usage");

        add(graphicsSurface);
        setLocation(100, 81);
        setSize(800,400);
        setVisible(true);
    }

    public ConsoleListener getListener() {
        return ml;
    }
}