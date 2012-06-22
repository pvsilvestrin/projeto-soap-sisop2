/**
 * User: paulo
 * Date: 6/17/12
 * Time: 11:30 PM
 */
class Timer extends Thread
{
    private IntController hint;
    public Timer(IntController i)
    {
        hint = i;
    }
    public void run()
    {
        while (true)
        {
            try {
                sleep(500);  // half second
                //hint.P();
                hint.set(2);
            }
            catch (InterruptedException e){}
            System.err.println("timer");
        }
    }
}