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
    private ProcessDescriptor idle;
    private GraphicsSurface graphicsSurface;
    private static int processIdControl = 100;
    private static final int BLOCK_FREE = 0;
    private static final int BLOCK_OCCUPIED = 1;
    private static int memoryBlockControl[];

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
        memoryBlockControl = new int[8];

        readyList = new ProcessList ();
        diskList = new ProcessList ();

        idle = new ProcessDescriptor(getProcessId());
        idle.setMemoryPart(1);
        idle.setPC(0);
        graphicsSurface.addProcessIdle(idle.getPID());

        //readyList.pushBack(new ProcessDescriptor(456));
        //readyList.getBack().setPC(0);
        //readyList.getBack().setMemoryPart(2);
        //readyList.pushBack( new ProcessDescriptor(457) );
        //readyList.getBack().setPC(0);
        //readyList.getBack().setMemoryPart(1);
    }
    // Each time the kernel runs it have access to all hardware components
    public void run(int hardwareInterrupNumber)
    {
        ProcessDescriptor aux = null;
        // This is the entry point: must check what happened
        System.err.println("Kernel called for int " + hardwareInterrupNumber);
        // save context
        if (!readyList.isEmpty()) {
            readyList.getFront().setPC(processor.getPC());
            readyList.getFront().setReg(processor.getReg());
        }
        switch(hardwareInterrupNumber)
        {
            case 2:
                if(!readyList.isEmpty()) {
                    aux = readyList.popFront();
                    readyList.pushBack(aux);
                    graphicsSurface.changeProcess(readyList.getFront().getPID());
                    System.err.println("CPU runs: "+readyList.getFront().getPID());
                }
                break;
            case 5: // DISK HW INT
                if(!diskList.isEmpty()) {
                    aux = diskList.popFront();
                    readyList.pushBack(aux);
                }
                break;
            case 3:
                System.err.println("Ilegal Memory Access");
                handleProcessExit();
                break;
            case 15: // HW INT console
                handleConsoleInput(console.getLine());
                System.err.println("Operator typed " + console.getLine());

                //readyList.pushBack(new ProcessDescriptor(getProcessId()));
                //readyList.getBack().setPC(0);
                //readyList.getBack().setMemoryPart(1);
                break;
            case 36: // GET SW INT
                aux = readyList.popFront();
                diskList.pushBack(aux);
                disc.roda(0);
                break;

            case 32:
                System.err.println("Process Exit");
                handleProcessExit();
                break;
            case 46:
                System.err.println("Call Print");
                printFunction();
                break;
            default:
                System.err.println("Unknown...");
        }
        // restore context
        if(!readyList.isEmpty()) {
            processor.setPC(readyList.getFront().getPC());
            processor.setReg(readyList.getFront().getReg());
            memory.setBaseRegister(Memory.MMU_PROCESSOR_1,readyList.getFront().getMemoryPart());
        } else { //Init process Idle
            processor.setPC(idle.getPC());
            processor.setReg(idle.getReg());
            memory.setBaseRegister(Memory.MMU_PROCESSOR_1, idle.getMemoryPart());
            graphicsSurface.changeProcess(idle.getPID());
        }
    }

    private void handleProcessExit() {
        ProcessDescriptor aux = readyList.popFront();
        if(aux != null) {
            memoryBlockControl[aux.getMemoryPart()] = BLOCK_FREE;
            System.err.println("EXIT: Process " + aux.getPID());
        }
    }

    private void handleConsoleInput(String line) {
        String inputParams[] = line.split(" ");
        int diskNumber = Integer.parseInt(inputParams[0]);
        int address =  Integer.parseInt(inputParams[1]);
        System.out.println("Disco = " + diskNumber + "Address = " + address);

        int memoryBlock = getMemoryBlock();
        if(memoryBlock != -1) {
            ProcessDescriptor newProcess = new ProcessDescriptor(getProcessId());
            newProcess.setMemoryPart(memoryBlock );
            diskList.pushBack(newProcess);
            memory.setBaseRegister(Memory.MMU_DISK_1, newProcess.getMemoryPart());
            disc.setOperation(disc.OPERATION_LOAD);

            disc.roda(address);
        } else {
            System.err.println("There is no free block in memory.");
        }

    }

    private void printFunction() {
        int RD = processor.getReg()[0];
        int value[] = new int[4];
        // break the 32bit word into 4 separate bytes
        value[0] = RD>>>24;
        value[1] = (RD>>>16) & 255;
        value[2] = (RD>>>8) & 255;
        value[3] = RD & 255;

        // print CPU status to check if it´s ok
        System.err.print(" Print reg 0 ="+value[0]+" "+value[1]+" "+value[2]+" "+value[3]);
    }

    private int getProcessId(){
        return processIdControl++;
    }

    private int getMemoryBlock() {
        for(int i = 2 ; i < 8; i++){
            if(memoryBlockControl[i] == BLOCK_FREE){
                memoryBlockControl[i] = BLOCK_OCCUPIED;
                return i;
            }
        }
        return -1;
    }

    private void initMemoryBlockControl(){
        memoryBlockControl[0] = BLOCK_OCCUPIED;
        memoryBlockControl[1] = BLOCK_OCCUPIED;
        memoryBlockControl[2] = BLOCK_FREE;
        memoryBlockControl[3] = BLOCK_FREE;
        memoryBlockControl[4] = BLOCK_FREE;
        memoryBlockControl[5] = BLOCK_FREE;
        memoryBlockControl[6] = BLOCK_FREE;
        memoryBlockControl[7] = BLOCK_FREE;
    }
}