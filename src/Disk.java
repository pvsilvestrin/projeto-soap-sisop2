import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * User: paulo
 * Date: 6/17/12
 * Time: 11:30 PM
 */
class Disk extends Thread
{
    private IntController hint;
    private Memory mem;
    
    private String fileName;
    private int[] diskImage;
    private int diskSize;

    // Here go the disk interface registers
    private int address;
    private int writeData;
    private int[] readData;
    private int readSize;
    private int operation;
    private int errorCode;
    // and some codes to get the meaning of the intterface
    // you can use the codes inside the kernel, like: disk.OPERATION_READ
    public final int OPERATION_READ = 0;
    public final int OPERATION_WRITE = 1;
    public final int OPERATION_LOAD = 2;
    public final int ERRORCODE_SUCCESS = 0;
    public final int ERRORCODE_SOMETHING_WRONG = 1;
    public final int ERRORCODE_ADDRESS_OUT_OF_RANGE = 2;
    public final int ERRORCODE_MISSING_EOF = 3;
    public final int BUFFER_SIZE = 128;
    public final int END_OF_FILE = 0xFFFFFFFF;

    public Disk(IntController i, Memory m, int size, String fileName)
    {
        hint = i;
        mem = m;

        // remember diskSize and create disk memory
        this.fileName = fileName;
        this.diskSize = size;
        diskImage = new int[size];
        readData = new int[BUFFER_SIZE];
        readSize = 0;
    }
    public synchronized void run()
    {
        try {load(fileName);} catch (IOException e){}
        while (true)
        {
            // wait for some request comming from the processor
            try {wait();} catch (InterruptedException e) {e.printStackTrace();}


            // Processor requested: now I have something to do...
            //for (int i=0; i < 20; ++i)
            //{
            // sleep just 50 ms which is one disc turn here
            try {sleep(100);} catch (InterruptedException e){}
            System.err.println("disk made a turn");

            if (address < 0 || address >= diskSize)
                errorCode = ERRORCODE_ADDRESS_OUT_OF_RANGE;
            else {
                errorCode = ERRORCODE_SUCCESS;

                switch(operation) {
                    case OPERATION_READ:
                        System.err.println("OPERATION_READ");
                        readSize = 1;
                        readData[0] = diskImage[address];
                        break;
                    case OPERATION_WRITE:
                        System.err.println("OPERATION_WRITE");
                        diskImage[address] = writeData;
                        break;
                    case OPERATION_LOAD:
                        System.err.println("OPERATION_LOAD");
                        int diskIndex = address;
                        int bufferIndex = 0;
                        while (diskImage[diskIndex] != END_OF_FILE) {
                            System.err.println(".");
                            readData[bufferIndex] = diskImage[diskIndex];
                            mem.write(Memory.MMU_DISK_1, bufferIndex, diskImage[diskIndex]);
                            ++diskIndex;
                            ++bufferIndex;
                            if (bufferIndex >= BUFFER_SIZE || diskIndex >= diskSize) {
                                errorCode = ERRORCODE_MISSING_EOF;
                                break;
                            }
                        }
                        readSize = bufferIndex;
                        break;
                }
            }

            //hint.P();
            hint.set(5);
            //}
        }

    }

    public synchronized void roda(int address)    {
        this.address = address;
        notify();
    }

    public void load(String filename) throws IOException
    {
        FileReader f = new FileReader(filename);
        StreamTokenizer tokemon = new StreamTokenizer(f);
        int bytes[] = new int[4];
        int tok = tokemon.nextToken();
        for (int i=0; tok != StreamTokenizer.TT_EOF && (i < diskSize); ++i)
        {
            for (int j=0; tok != StreamTokenizer.TT_EOF && j<4; ++j)
            {
                if (tokemon.ttype == StreamTokenizer.TT_NUMBER )
                    bytes[j] = (int) tokemon.nval;
                else
                if (tokemon.ttype == StreamTokenizer.TT_WORD )
                    bytes[j] = (int) tokemon.sval.charAt(0);
                else
                    System.out.println("Unexpected token at disk image!");
                tok = tokemon.nextToken();
            }
            diskImage[i] = ((bytes[0]&255)<<24) | ((bytes[1]&255)<<16) |
                    ((bytes[2]&255)<<8) | (bytes[3]&255);
            System.out.println("Parsed "+bytes[0]+" "+bytes[1]+" "
                    +bytes[2]+" "+bytes[3]+" = "+diskImage[i]);
        }
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getReadData(){
        return readData[0];
    }

    public int getReadSize() {
        return readSize;
    }
}