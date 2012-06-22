import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * User: paulo
 * Date: 6/19/12
 * Time: 11:14 AM
 */
class ProcessList
{
    private ArrayList<ProcessDescriptor> processList;
    //private ProcessDescriptor first = null;
    //private ProcessDescriptor last = null;

    public ProcessList() {
        processList = new ArrayList<ProcessDescriptor>();
    }

    public ProcessDescriptor popFront() {
        ProcessDescriptor n;
        if(!processList.isEmpty()) {
            n = processList.remove(0);
            return n;
        }
        return null;
    }

    public void pushBack(ProcessDescriptor n) {
        if(n != null) processList.add(n);
    }

    public ProcessDescriptor getFront() {
        if(processList.isEmpty()) return null;
        return processList.get(0);
    }

    public ProcessDescriptor getBack() {
        if(processList.isEmpty()) return null;
        return processList.get(processList.size()-1);
    }

}
