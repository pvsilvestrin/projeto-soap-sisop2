import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: paulo
 * Date: 6/17/12
 * Time: 11:32 PM
 */
public class Soap {
    public final static int DISC_SIZE = 2048;

    public static void main(String args[])
    {
        IntController i	= new IntController();

        // Create window console
        MyWin mw = new MyWin();
        mw.addWindowListener ( new WindowAdapter() {
            public void windowClosing (WindowEvent e) {
                System.exit(0);
            }
        });
        GraphicsSurface graphicsSurface = new GraphicsSurface();
        GraphicWindow graphicWindow = new GraphicWindow(mw, graphicsSurface);

        ConsoleListener c = mw.getListener();
        c.setInterruptController(i);

        Memory m	= new Memory(i,1024);
        Timer t	= new Timer(i);
        Disk d	= new Disk(i,m, DISC_SIZE, "disk.txt");
        Processor p	= new Processor(i,m,c,t,d, graphicsSurface);
        // start all threads
        p.start();
        t.start();
        d.start();
    }
}
