import java.util.ArrayList;
/*
 THIS CLASS INCLUDES METHOD TO CHECK IF A PROCESS START EXECUTING ON THE CPU AND A METHOD TO CHECK IF THE PROCESS END EXECUTION.
  IT ALSO INCLUDES METHODS TO CALCULATE WAITING, AND TURN AROUND TIMES WHICH WILL BE USED TO COMPARE
 THE DIFFERENT CPU SCHEDULING ALGORITHMS. METHOD ISINREADYQUEUE CHECK IF THE PROCESS IS READY AND WAITING IN READY QUEUE TO BE ASSIGNED
 TO THE CPU.METHOD REMAININGTIME CHECK IF THE PROCESS END ITS EXECUTION ON CPU, FINISH ITS BURST TIME. 
 */
public class PROCESS implements Runnable{
	
	public int pid;	//PID OF PROCESS																												
	public double arriveTime;//ARRIVAL TIME OF PROCESS																							
	public double burstTime ;//BURST TIME OF PROCESS								    													    	
	public double timeFinished;//TIME FINISHED FROM THE BURST, SUBTRACTED FROM THE BURST TIME							
	public int size;
	public ArrayList<Integer> timeStartWork;//ARRAY LIST TO HOLD THE START TIME FOR EACH PROCESS FROM EACH CPU-BURST CYCLE	    
	public ArrayList<Integer> timeFinishedWork;//ARRAY LIST TO HOLD FINISHED TIME FOR EACH PROCESS FORM EACH CPU-BURST CYCLE		
	public TRACE memoryTrace ;
	int pageFaults = 0;
	private int quantime;
	private PHYSICAL_MEM physicalMemory;
	private int cycle;
	
	
	

	//CONSTRUCTOR
	public PROCESS (int pid, int arriveTime, int burstTime , int size) {
		this.pid = pid;
		this.arriveTime = arriveTime;
		this.burstTime = burstTime;
		this.size = size ;
		this.timeStartWork= new ArrayList<Integer>();//CREATING ARRAY LIST THAT WILL HOLD START TIME OF EACH CPU-BURST CYCLE	
		this.timeFinishedWork =new ArrayList<Integer>();//CREATING ARRAY LIST THAT WILL HOLD FINISH TIME OF EACH CPU-BURST CYCLE
		this.memoryTrace = new TRACE();
	}
	
//THIS METHOD CHECKS IF THE PROCESS ARRIVED TO SYSTEM, READY QUEUE OR NOT. RETURNS TRUE IF NOT ARRIVED TO SYSTEM YET. 
	public boolean checkArrival(double currentTime){
		return this.arriveTime <= currentTime;
	}
	
	
	
//THIS METHOD CHECKS IF THE PROCESS HAS FINISHED ITS EXECUTION
	public boolean isFinished(double currentTime){
		return this.burstTime == this.timeFinished;
	}
	
	
//THIS METHOD CHECKS IF THE PROCESS IS IN THE READY QUEUE BY CHECKING IF THE PROCESS NOT FINISHED ITS EXECUTION AND IF ARRIVED TO THE SYS.	 	
	public boolean isInReadyQueue(double currentTime){
		return !isFinished(currentTime) && checkArrival(currentTime);
	}
	
	

//THIS METHOD CALCULATES REMAINING TIME FOR  PROCESS TO EXECUTE ON THE CPU,IT SUBTRACT FINISHED TIME FROM THE BURST TIME THEN RETURNS IT.
	public double RemainingTime(){
		if (burstTime - timeFinished == 0)
			return Integer.MAX_VALUE;
		return burstTime - timeFinished;
	}
	
//METHOD TO CALCULATE TURN AROUND TIME FOR THE PROCESS, TURN AROUND TIME IS THE TIME FROM THE MOMENT THE PROCESS ARRIVES TO THE SYSTEM 
//TO THE MOMENT OF FINISHING ITS EXECUTION INCLUDING THE WAIT TIME THAT THE PROCESS IS WAITING FOR THE CPU OR DOING I/O. TURN AROUND TIME=WAITING TIME + BURST TIME-ARRIVE TIME
	public double turnaround(int[] processesBurstCycles){
		return (this.timeFinishedWork.get(timeFinishedWork.size()-1) + processesBurstCycles[this.pid - 1]) - this.arriveTime;
	}
	
	
//THIS METHOD CALCULATES WAITING TIME
	public double waitTime(int[] processesBurstCycles){
		return this.turnaround(processesBurstCycles) - (this.burstTime + processesBurstCycles[this.pid - 1]);
	}

@Override
public void run() {
	// TODO Auto-generated method stub
	for (int i = 0; i < quantime; i++){
		if(this.memoryTrace.reach_page < this.size){
			this.timeStartWork.add(this.physicalMemory.tot_cycles);//ADD THE TIME THE PROCESS ENTERED THE CPU
			int pageNumber = this.memoryTrace.getPage();//ADDRESS IN VIRTUAL MEMORY
			physicalMemory.addPageToMemory(this.pid,pageNumber );
			cycle = this.physicalMemory.tot_cycles;
			this.timeFinishedWork.add(cycle); //ADD THE TIME THE PROCESS OUT FROM THE CPU
			System.out.println(" TOTAL CYCLES UNTIL NOW=>" + cycle);
			System.out.println(" ____________________________________________________________________________________________");
			
		}
		
	}
	
	synchronized (Thread.currentThread()) {

	Thread.currentThread().notify();
	}

}
//GETTERS AND SETTERS
public int getCycle() {
	return cycle;
}

public void setPhysicalMemory(PHYSICAL_MEM physicalMemory) {
	this.physicalMemory = physicalMemory;
}

public void setCycle(int cycle) {
	this.cycle = cycle;
}	
public void setQuantime(int quantime) {
	this.quantime = quantime;
}



}
