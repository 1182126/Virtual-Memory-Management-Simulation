//THIS CLASS IMPLEMENTS THE ROUND ROBIN CPU SCHEDULING ALGORITHM, IT USES A TIME QUANTUM PROVIDED BY THE USER TO IMPLEMENT THE ALGORITHM.
public class ROUND_ROUBIN  extends SCHEDUALING{
	int quantime;													
	PHYSICAL_MEM physicalMemory ;
	int proc_read_file;
	int[] proc_burst_cycs;
	//CONSTRCTOR
	public ROUND_ROUBIN(READY_TO_RUN all_proc, int quantime, int proc_read_file, int[] proc_burst_cycs,int m) {
		this.done = all_proc.allProcesses;
		this.fullTime = all_proc.getFullTime();
		this.quantime = quantime;
		this.readyQue = all_proc;
		this.proc_read_file = proc_read_file;
		this.proc_burst_cycs = proc_burst_cycs;
		physicalMemory = PHYSICAL_MEM.getInstance(m);
		physicalMemory.readyQ = all_proc;
	}


//THIS METHOD IMPLEMENTS THE ROUND ROUBIN ALGORITHM
	public void RR_Algorithm(){
		PROCESS currentProcess = null ;
		
		for (int cycle = 0; cycle < 1000 * proc_read_file;){
			boolean isCpuIdeal= true;
			for (PROCESS process : done){
				//System.out.println(">>>>>>>>>>>>>>");
				//System.out.println(">>>>>>>>>>>>>>"+cycle);
				//System.out.println(">>>>>>>>>>>>>>");
				//System.out.println("__________________________________________");
				
				if(process.isInReadyQueue(cycle/1000.0 ) && !process.memoryTrace.isFinishedPages() ){
					
					if(currentProcess != process && cycle != 0 && currentProcess != null)
					{
						
						System.out.println(" ____________________________________________________________________________________________");
						System.out.println(" 5 CYCLES ADDED  BY SWITCHING THE PROCESS=> " + currentProcess.pid + " TO THE PROCESS=> " + process.pid);
						System.out.println(" ____________________________________________________________________________________________");
						cycle += 5;
						this.physicalMemory.tot_cycles = cycle ;	
					}
					isCpuIdeal = false;
					for (int i = 0; i < quantime; i++){
						currentProcess = process;
						if(currentProcess.memoryTrace.reach_page < process.size){
							currentProcess.timeStartWork.add(this.physicalMemory.tot_cycles);//ADD THE TIME THE PROCESS ENTERED THE CPU
							int pageNumber = currentProcess.memoryTrace.getPage();//ADDRESS IN VIRTUAL MEMORY
							physicalMemory.addPageToMemory(currentProcess.pid,pageNumber );
							cycle = this.physicalMemory.tot_cycles;
							process.timeFinishedWork.add(cycle); //ADD THE TIME THE PROCESS OUT FROM THE CPU
							System.out.println(" TOTAL CYCLES UNTIL NOW=>" + cycle);
							proc_burst_cycs[currentProcess.pid - 1] += 1000;
							System.out.println(" __________________________________________________________________________________________");
							
						}
					}
					currentProcess = process;
					currentProcess.setPhysicalMemory(physicalMemory);
					currentProcess.setCycle(cycle);
					Thread t = new Thread(currentProcess);
					t.start();
					//System.out.println(">>>>>>>>>>>>>>");
					synchronized (t) {
					try {
						t.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
				}
					cycle=currentProcess.getCycle();
				}
			}
			if (physicalMemory.readyQ.areAllProcessFinished()){
				break;
			}
			if(isCpuIdeal)
				cycle =	++this.physicalMemory.tot_cycles;
		}
	}
}