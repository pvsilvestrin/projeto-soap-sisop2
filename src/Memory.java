/**
 * User: paulo
 * Date: 6/17/12
 * Time: 11:29 PM
 */
public class Memory {
    private int[] word;
    private int size;

    public Memory(int s){
        this.size = s;
        this.word = new int[s];

        init(0,'X','M',0,10);
        init(1,'X','D',0,10);
        init(2,'S','M',0,10);
        init(3,'J','P','A',0);
        init(30,'L','D',0,10);
        init(31,'L','D',0,10);
        init(32, 'I', 'N', 'T', 46);
        init(33,'J','P','A',30);
    }

    public synchronized void init(int address, int a, int b, int c, int d) {
        word[address] = (a << 24)+(b<<16)+(c<<8)+d;
    }

    public synchronized int read(int address) {
        return word[address];
    }
    public synchronized void write(int address, int data) {
        word[address] = data;
    }
}
