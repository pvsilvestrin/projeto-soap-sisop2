/**
 * User: paulo
 * Date: 6/18/12
 * Time: 11:53 AM
 */
class Kernel
{
    // Access to hardware components, including the processor
    private IntController hint;
    private Memory memory;
    private ConsoleListener console;
    private Timer timer;
    private Disk disc;
    private Processor processor;
    private int running = 0;
    private ProcessList readyList;
    private ProcessList diskList;
    private GraphicsSurface graphicsSurface;

    // In the constructor goes initialization code
    public Kernel(IntController i, Memory m, ConsoleListener c, Timer t, Disk d, Processor p, GraphicsSurface graphicsSurface)
    {
        hint = i;
        memory = m;
        console = c;
        timer = t;
        disc = d;
        processor = p;
        this.graphicsSurface = graphicsSurface;

        readyList = new ProcessList ();
        diskList = new ProcessList ();

        readyList.pushBack(new ProcessDescriptor(456));
        readyList.getBack().setPC(0);
        readyList.pushBack( new ProcessDescriptor(457) );
        readyList.getBack().setPC(30);
    }
    // Each time the kernel runs it have access to all hardware components
    public void run(int hardwareInterrupNumber)
    {
        ProcessDescriptor aux = null;
        // This is the entry point: must check what happened
        System.err.println("Kernel called for int "+hardwareInterrupNumber);
        // save context
        readyList.getFront().setPC(processor.getPC());
        readyList.getFront().setReg(processor.getReg());
        switch(hardwareInterrupNumber)
        {
            case 2:
                aux = readyList.popFront();
                readyList.pushBack(aux);
                graphicsSurface.changeProcess(readyList.getFront().getPID());
                System.err.println("CPU runs: "+readyList.getFront().getPID());
                break;
            case 5: // DISK HW INT
                aux = diskList.popFront();
                readyList.pushBack(aux);
                break;
            case 15: // HW INT console
                System.err.println("Operator typed " + console.getLine());
                break;
            case 36: // GET SW INT
                aux = readyList.popFront();
                diskList.pushBack(aux);
                disc.roda();
                break;
            case 46:
                System.err.println("Call Print");
            default:
                System.err.println("Unknown...");
        }
        // restore context
        processor.setPC(readyList.getFront().getPC());
        processor.setReg(readyList.getFront().getReg());
    }
}