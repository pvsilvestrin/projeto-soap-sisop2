/**
 * User: paulo
 * Date: 7/10/12
 * Time: 5:14 PM
 */
public class FileDescriptor {
    public static final int MODE_WRITE = 1;
    public static final int MODE_READ = 0;

    private int diskNumber;
    private int address;
    private int mode;

    private static int FileDescriptorNumber = 2;


    public FileDescriptor(int diskNumber, int address, int mode) {
        this.diskNumber = diskNumber;
        this.address = address;
        this.mode = mode;
    }

    public int getDiskNumber() {
        return diskNumber;
    }

    public void setDiskNumber(int diskNumber) {
        this.diskNumber = diskNumber;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static int getFileDescriptorNumber(){
        return FileDescriptorNumber++;
    }

    public void incAddress() {
        address++;
    }
}
