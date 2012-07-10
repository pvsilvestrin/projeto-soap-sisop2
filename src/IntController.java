/**
 * User: paulo
 * Date: 6/18/12
 * Time: 11:25 AM
 */

class IntController
{
    // The interrupt controller component has a private semaphore to maintain
    // interrupt requests coming from all other components. But I could not
    // make the operations on this semaphore automatic, since it cannot do a
    // readyToNext operation while locked in a synchronized method that called P.
    // Other components have then to call P() before calling set() and call
    // readyToNext() before calling reset(). We could also make IntController extends
    // Semaphore instead of having one. Try this out.

    private int number;

    public IntController()		{ }


    //public synchronized void P(){
    //    try {
    //        wait();
    //    } catch (InterruptedException e) {
    //        e.printStackTrace();
    //    }
    //}

    public synchronized void readyToNext() {
        notify();
    }

    public synchronized void set(int n)	{
        try {
            wait();
            number = n;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized int get() {
        int ret = number;
        number = 0;
        //notify();
        return ret;
    }

    //public synchronized void reset() {
    //    number = 0;
    //}
}
