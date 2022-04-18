
import java.util.ArrayList;

//THIS CLASS IS USED TO SCHEDULE THE PROCESSES AND PRINT THE CHART TO THE USER
public class SCHEDUALING {
	ArrayList<PROCESS> done;//THIS MEANS THAT ALL PROCESSES ARE DONE
	int fullTime;
	READY_TO_RUN readyQue;
	
	public void printChart(int[] proc_burst_cyc){
		readyQue.buildChart();
		readyQue.printChart(proc_burst_cyc);
		
	}

}
