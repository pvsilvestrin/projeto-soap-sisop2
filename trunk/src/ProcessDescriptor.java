/**
 * User: paulo
 * Date: 6/18/12
 * Time: 12:36 PM
 */
class ProcessDescriptor
{
    private int PID;
    private int PC;
    private int memoryPart;
    private int[] reg;
    public int getPID() { return PID; }
    public int getPC() { return PC; }
    public void setPC(int i) { PC = i; }
    public int[] getReg() { return reg; }
    public void setReg(int[] r) { reg = r; }

    public ProcessDescriptor(int pid)
    {
        PID = pid;
        PC = 0;
        reg = new int[16];
        memoryPart = 0;
    }

    public int getMemoryPart() {
        return memoryPart;
    }

    public void setMemoryPart(int memoryPart) {
        this.memoryPart = memoryPart;
    }
}