/**
 * User: paulo
 * Date: 6/17/12
 * Time: 11:29 PM
 */
public class Memory {
    public final static int MMU_PROCESSOR_1 = 0;
    public final static int MMU_DISK_1 = 1;
    private TrowMemoryExeption trowMemoryExeption;
    private IntController hint;
    private int[] word;
    private int size;
    private int limitRegister[];
    private int baseRegister[];

    public Memory(IntController hint, int s){
        this.hint = hint;
        this.size = s;
        this.word = new int[s];
        limitRegister = new int[4];
        baseRegister = new int[4];
        //Init process Idle
        baseRegister[MMU_PROCESSOR_1] = 0;
        init(MMU_PROCESSOR_1,0,'J','P','A',0);

        trowMemoryExeption = new TrowMemoryExeption(hint);
        trowMemoryExeption.start();
    }

    public void setLimitRegister(int device, int val) { limitRegister[device] = val; }
    public void setBaseRegister(int device, int val) { baseRegister[device] = val; }

    // Here goes some specifi methods for the kernel to access memory
    // bypassing the MMU (do not add base register or test limits)
    public int superRead(int address) { return word[address]; }
    public void superWrite(int address, int data) { word[address] = data; }


    public synchronized void init(int device, int address, int a, int b, int c, int d) {
        //word[address] = (a << 24)+(b<<16)+(c<<8)+d;
            write(device ,address, (a << 24) + (b << 16) + (c << 8) + d);
    }

    public synchronized int read(int device, int address) {
        if (address >= 128) {
            //hint.set(3);
            //trowMemoryExeption.invalidMemoryAccess();
            return -1;
        }
        else return word[address + (baseRegister[device]*128)];
    }
    public synchronized void write(int device, int address, int data) {
        if (address >= 128) trowMemoryExeption.invalidMemoryAccess();//hint.set(3);
        else word[address + (baseRegister[device]*128)] = data;
    }

    public class TrowMemoryExeption extends Thread {
        private IntController hint;

        public TrowMemoryExeption(IntController hint) {
            this.hint = hint;
        }


        @Override
        public synchronized void run() {
            while(true){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hint.set(3);
            }
        }

        public synchronized void invalidMemoryAccess() {
            notify();
        }
    }
}
