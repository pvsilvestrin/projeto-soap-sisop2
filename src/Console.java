/**
 * User: paulo
 * Date: 6/17/12
 * Time: 11:29 PM
 */
class Console extends Thread
{
    private IntController hint;
    public Console(IntController i)
    {
        hint = i;
    }
    public void run()
    {
        while (true)
        {
            try {sleep(2000);} // 2 seconds
            catch (InterruptedException e){}
            System.err.println("console");
        }
    }
}