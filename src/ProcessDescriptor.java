/**
 * User: paulo
 * Date: 6/18/12
 * Time: 12:36 PM
 */
class ProcessDescriptor
{
    private int PID;
    private int PC;
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
    }
}