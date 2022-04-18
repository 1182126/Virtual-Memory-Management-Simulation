import java.util.ArrayList;
import java.util.Collections;
/* 
 * THIS CLASS REPRESENT THE READY QUEUE IN THE SYSTEM, IT HAS AN ARRAY LIST THAT HOLDS ALL PROCESSES IN THE SYSTEM
 * CALLED ALLPROCESSES AND ANOTHER ARRAY LIST THAT HOLDS ALL PROCESSES THAT DONE EXECUTING ON CPU AND WILL BE PRINTED
 * TO USER IN A CHART CALLED CHART.
 * THIS CLASS INCLUDES AN INNER CLASS THAT IS USED TO REPRESENT THE PROCESSES THAT DONE EXECUTING ON CPU AND WILL BE 
 * SORTED ON START OF EXECUTION TIME. IT IMPLEMENTS THE COMPARABLE INTERFACE TO OVERRIDE THE COMPARETO METHOD SO THAT
 * TO SORT PROCESSES ON THEIR START TIME OF EXECUTION. 
 * 
 */
public class READY_TO_RUN {
	ArrayList<PROCESS> allProcesses;///ARRAY LIST THAT HOLDS ALLPROCESSES PROCESSES READ FROM INPUT FILE.
	ArrayList<chartObject> chart;  //ARRAY LIST THAT REPRESENT CHART OF CPU BURST TIMES.
	 
//CONSTRUCTOR THAT INITIALIZE THE TWO ARRAY LIST OF THE READYQUEUE CLASS.
	public READY_TO_RUN() {
		this.allProcesses = new ArrayList<PROCESS>();
		chart = new ArrayList<chartObject>();
	}

//THIS  METHOD ADDS A NEW PROCESS TO THE READY QUEUE, ARRAY LIST ALLPROCESSES
	public void addNewProcess(PROCESS process) {
		this.allProcesses.add(process);
	}

	
//THIS METHOD RETURNS A STRING THAT DISPLAYS THE INFORMATION OF EACH PROCESS IN THE READY QUEUE.
	public String toString() {

		String processesInfo = "";
		for (PROCESS process : allProcesses) {
			processesInfo += process.pid + " " + process.arriveTime + " " + process.burstTime + " "
					 + "\n";
		}

		return processesInfo;
	}

	
	 
	 
//THIS METHOD GETS THE SUM OF BURST TIMES FOR ALL THE PROCESSES IN THE ALLPROCESSES ARRAY LIST.
	public int getFullTime() {
		int sum = 0;
		for (PROCESS process : allProcesses) {
			sum += process.burstTime;
		}
		return sum;
	}
	

	 
//THIS METHOD BUILDS A CHART FOR ALL THE PROCESSES THAT DONE EXECUTING ON CPU, IT DISPLAYS THE START AND FINISHED TIMES FOR EACH PROCESS.
	@SuppressWarnings("unchecked")
	void buildChart() {
		for (PROCESS process : allProcesses) {
			for (int i = 0; i < process.timeFinishedWork.size(); i++)
				chart.add(new chartObject(process.pid, process.timeStartWork.get(i),//ADDING A PROCESS TO CHART ARRAY LIST WITH ITS INFORMATION
						process.timeFinishedWork.get(i)));
		}
		Collections.sort(chart);//SORTING ARRAY LIST OF PROCESSES TO PRODUCE CHART, ON START TIME

	}
	
//THIS METHOD PRINTS THE CHART OF PROCESSES TO THE USER.
	void printChart(int[] processesBurstCycles){
		double sumOfPageFaults = 0;
		System.out.println("RESULT:");
		System.out.println("	PID			       TA_TIME 	     WAIT_TIME        PAGE FAULTS");
		System.out.println(" ____________________________________________________________________________________________");
		for (PROCESS process : allProcesses) {
			System.out.println(" 	p" + process.pid + "				" + process.turnaround(processesBurstCycles) / 1000.0 + "		" + process.waitTime(processesBurstCycles) / 1000.0  +  "			" + process.pageFaults);
			sumOfPageFaults += process.pageFaults;
		}
		System.out.println(" ____________________________________________________________________________________________");
		System.out.printf("AVERAGE:		                %f	 %f	    %f\n", this.avgTurnAround(processesBurstCycles) / 1000.0, this.avgWaitArount(processesBurstCycles) / 1000.0 , sumOfPageFaults/allProcesses.size());
		System.out.println(" ____________________________________________________________________________________________");
	} 
	
	String getChartString(int[] processesBurstCycles){
		String str = "";
		str += ("RESULTS:");
		str += 	("CHART:\n");
		for (chartObject processInChartList: chart) {
			str += ("|" +processInChartList.arriveTime);
			for(int i = processInChartList.arriveTime; i < processInChartList.finishTime; i++){
				if(i == (processInChartList.finishTime +processInChartList.arriveTime)  / 2)
					str += ("P" +processInChartList.pid);
				str += (" ");
			}
			str += (processInChartList.finishTime);
			
		}
		str += ("|\n");
		str += ("\n");
		str += ("\n");
		str += ("PID    TA_TIME       WAIT_TIME\n");
		str  += (" ____________________________________________________________________________________________\n");
		for (PROCESS process : allProcesses) {
			str += ("p" + process.pid + "	" + process.turnaround(processesBurstCycles) + " 	 " + process.waitTime(processesBurstCycles)+"\n" );
		}
	return str;
	}
	
	public  double avgTurnAround(int[] processesBurstCycles)
	{
		double sum = 0;
		for (PROCESS p : allProcesses)
		{
			sum += p.turnaround(processesBurstCycles);
		}
		return sum/allProcesses.size();
	}
	public  double avgWaitArount(int[] processesBurstCycles)
	{
		double sum = 0;
		for (PROCESS p : allProcesses)
		{
			sum += p.waitTime(processesBurstCycles);
		}
		return sum/allProcesses.size();
	}
	
	@SuppressWarnings("unused")
	private int cpuUsage ()
	{
		int sum = 0;
		for (PROCESS p : allProcesses)
		{
			sum += p.burstTime;
		}
		return sum;

	}

//INNER CLASS THAT IMPLEMENTS THE COMPARABLE INTERFACE TO SORT THE PROCESSES IN THIS CHART DEPENDING ON START OF BURST TIME FOR EACH PROCESS.
	@SuppressWarnings("rawtypes")
	class chartObject implements Comparable {
		int pid;																	
		int arriveTime;																
		int finishTime;																

		
		  
		 
//CONSTRUCTOR TO INITIALIZE THE PROCESS THAT IS ADDED TO THE ARRAY LIST OF CHART.
		public chartObject(int pid, int arriveTime, int finishTime) {
			this.pid = pid;
			this.arriveTime = arriveTime;
			this.finishTime = finishTime;
		}

		
// OVERRIDING THE COMPARETO METHOD SO THAT TO SORT THE PROCESSES ACCORDING TO THEIR START TIME ON CPU.		 
		@Override
		public int compareTo(Object processInChartList) {

			return (int) (arriveTime - ((chartObject) processInChartList).arriveTime);
		}
		
		
//TOSTRING METHOD TO RETURN INFORMATION OF EACH PROCESS IN THE CHART ARRAY LIST.		 
		public String toString(){
			return "pid = " + pid + " start time = " + arriveTime + " finish time = " + finishTime;
		}
	}
	public boolean areAllProcessFinished(){
		for (PROCESS process : allProcesses)
		{
			if(!process.memoryTrace.isFinishedPages())
				return false;
			
		}
		return true;
	}
	
}
