import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * User: paulo
 * Date: 6/20/12
 * Time: 9:49 PM
 */
public class GraphicsSurface extends JComponent {
    private Map<Integer, Process> processMap;
    private ArrayList<Process> procUsage;

    private int numberOfProcess;
    private final static int BAR_SIZE = 20;
    private final static int INITIAL_X = 10;
    private int barPossition;

    public GraphicsSurface() {
        procUsage = new ArrayList<Process>();
        processMap = new HashMap<Integer, Process>();

        barPossition = 15;
        numberOfProcess = 0;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        g2d.fillRect(0,0,getWidth(),getHeight());

        printProcessUsage(g2d);

        repaint();
    }

    public synchronized void changeProcess(int pId){
        Process process = processMap.get(pId);
        if(process == null){
            process = createNewProcess(pId);
            processMap.put(pId, process);
        }
        procUsage.add(new Process(process));
    }

    public synchronized void addProcessIdle(int pid){
        Process process = new ProcessIdle(pid);
        procUsage.add(process);
        processMap.put(pid, process);
    }



    private Process createNewProcess(int pid){
        Process process = new Process(pid);
        numberOfProcess++;

        return process;
    }

    public synchronized void addProcessTick(){
       // try { wait();} catch (InterruptedException e) {e.printStackTrace();}

        if(!procUsage.isEmpty())
        procUsage.get(procUsage.size()-1).incrementTick();
        //notify();
    }

    private synchronized void printProcessUsage(Graphics2D g2d){
        Set<Integer> processIds = processMap.keySet();
        barPossition = 15;
        for(Integer i : processIds){
            g2d.setColor(processMap.get(i).color);
           g2d.drawString(processMap.get(i).getName(), INITIAL_X, barPossition);
            barPossition = barPossition + 20;
        }

        int lines = 0;
        for(Process prc: procUsage){
            g2d.setColor(prc.color);
            for(int i = 0; i < prc.getTicks(); i++){
                g2d.drawLine(INITIAL_X+lines, barPossition, INITIAL_X+lines, barPossition+BAR_SIZE);
                lines++;
            }
        }
        if(lines > 400) procUsage.remove(0);
        //notify();
    }

    private class Process {
        private int pId;
        public Color color;

        private int ticks;


        private Process(int pId) {
            Random randomColor = new Random();

            this.pId = pId;
            this.color = new Color(randomColor.nextInt(255),randomColor.nextInt(255),randomColor.nextInt(255),255);
            ticks = 0;
        }

        private Process(Process process){
            pId = process.getpId();
            color = process.color;
            ticks = 0;
        }

        public void incrementTick(){
            ticks++;
        }

        public int getTicks() {
            return ticks;
        }

        public int getpId() {
            return pId;
        }

        public String getName(){
            return "Process " + pId;
        }
    }

    private class ProcessIdle extends Process {
        private ProcessIdle(int pId) {
            super(pId);
        }

        private ProcessIdle(Process process) {
            super(process);
        }

        public String getName(){
            return "ProcessIdle";
        }
    }


}

