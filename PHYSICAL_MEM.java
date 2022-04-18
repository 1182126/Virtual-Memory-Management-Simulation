//IN THIS CLASS WE SIMULATED THE PHYSICAL MEMORY
public class PHYSICAL_MEM {
	
	public static int mem_size =8;
	int index_we_pointed_at = 0;
	MemoryObject[] circularQ = new MemoryObject[mem_size];
	READY_TO_RUN readyQ ;
	
	private static PHYSICAL_MEM instance;
	int tot_cycles =0;
	private PHYSICAL_MEM(int m) {
		this.mem_size=m;
		for (int i = 0; i < circularQ.length; i++){
			circularQ[i]= new MemoryObject();
		}
	}
	public static PHYSICAL_MEM getInstance(int m) {
		if(instance == null)
			instance =new PHYSICAL_MEM (m);
		return instance;
	}
	
	/*
	 THIS METHOD DO THE FOLLOWING:
	 ADDS A PROCCES TO MEMORY
	 CHECKS IF THE PAGE REQUIRED BY CPU IN MEMORY: YES--> REFRESH THE REFERENCE BIT, NO-->GET FROM DISK.
	 CHECKS THE MEMORY: IF THE MEMORY FULL-->CALL PAGE REPLACEMENT ALGO, NOT FULL-->FIND FIRST EMPTY PAGE TO ADD INTO IT.
	 CYCLES: ADDS 300 TO GET PAGE FROM DISK, 1 TO ACCESS MEMORY. 
	 */
	public void addPageToMemory(int pid, int page_num){
		System.out.printf(" REQUESTED PAGE => %s FOR PROCESS=> %d\n" , Integer.toHexString(page_num) , pid);
		int index;
		int page_place = 0;
		if ((index = search(pid, page_num)) > -1){//PAGE IN MEMORY				   		
			System.out.println(" IN MEMORY ALREADY!");//PRINT STATEMENT
			circularQ[index].refrenceBit = 1;//REFRESH						
			this.tot_cycles +=1;//INCREMENT THE COUNTER
			page_place = index;
		}
		else if (index == -1){//PAGE NOT IN MEMORY											     
			this.tot_cycles += 300;	//TOTAL CYCLES INCREASED BY 300 FOR PRENGING IT FROM DISK								
			this.readyQ.allProcesses.get(pid -1).pageFaults++;//INCREMENT THE COUNTER			

			if (this.isFull()){																		
				System.out.println(" PAGE NOT IN MEMORY & MEMORY FULL.");
				int indexToReplaceWith = SecondChance();
				setMemory(pid, page_num, indexToReplaceWith);
				index_we_pointed_at = (++index_we_pointed_at)%mem_size;
				page_place = indexToReplaceWith;
			}
			else{//STILL HAVE SPACE																		
				System.out.println(" PAGE NOT IN MEMORY BUT MEMORY NOT FULL.");
				int emptyIndex = this.getFirstEmptyIndex();
				circularQ[emptyIndex].pid = pid;
				circularQ[emptyIndex].page_num = page_num;
				circularQ[emptyIndex].refrenceBit = 1;
				page_place = emptyIndex;
				
			}
		}
		this.printMemory(page_place);
		System.out.println(" ____________________________________________________________________________________________");
		
	}

	
	//THIS METHOD SERCHES IN MEMORY FOR THE REQUIRED PAGE
	public int search(int pid, int page_num){
		for (int i = 0; i < circularQ.length; i++){
			if (circularQ[i].pid == pid
					&& circularQ[i].page_num == page_num)
				return i;
		}return -1;
	}

	
	//THIS METHOD CHECKS IF THE MEMORY IS FULL
	public boolean isFull(){
		for (MemoryObject memoryObject : circularQ)
			if(memoryObject.refrenceBit == -1) //SPACE NOT FULL
				return false;
		return true; //SPACE FULL	
	}
	
	
	//THIS METHOD GETS THE 1ST EMPTY PAGE IN THE MEMORY
	public int getFirstEmptyIndex(){
		for (int i = 0; i < circularQ.length; i++){
			if (circularQ[i].refrenceBit == -1)
				return i;
		}return -1;
	}
	
	//THIS METHOD PRINTS THE MEMORT TRACE
	public void printMemory(int page_place){
		
		System.out.println(" MEMORY PAGE TABLE MAP:");
		System.out.println(" ____________________________________________________________________________________________");
		System.out.println("     PAGE #              PID                REFERENCE BIT   ");
		System.out.println(" ____________________________________________________________________________________________");
		int i =0;
		for (MemoryObject memoryObject : circularQ){
			
			if(i == page_place)
				System.out.print("=>");
			else System.out.print("  ");
			System.out.printf("     %5s            %5d             %5d      "
					+ "   " , memoryObject.page_num==-1 ? -1 :
						Integer.toHexString(memoryObject.page_num),  memoryObject.pid, memoryObject.refrenceBit) ;
			if(i==index_we_pointed_at)
				System.out.println("<=");
			else System.out.println();
			i++;
		}System.out.println(" ____________________________________________________________________________________________");
	}
	
	
	//THIS METHOD RETURNS AN INTEGER INDEX TO REPLACE WITH
	public int SecondChance(){
		while(circularQ[index_we_pointed_at].refrenceBit == 1){
			circularQ[index_we_pointed_at].refrenceBit=0;
			index_we_pointed_at = (++index_we_pointed_at)%2;
		}return index_we_pointed_at;
	}
	
	
	//THIS METHOD CHANGES THE STATE OF A PAGE IN MEMORY
	public void setMemory(int pid , int page_num , int index){
		circularQ[index].pid = pid;
		circularQ[index].page_num = page_num;
		circularQ[index].refrenceBit = 1;
	}
	
	public void Clock(int pid, int page_num){
		System.out.printf(" REQUESTED PAGE=>  %s FOR PROCCES=> %d\n" , Integer.toHexString(page_num) , pid);
		int index;
		int page_place = 0;
		if ((index = search(pid, page_num)) > -1){//PAGE IN THE MEMORY				   		     
			System.out.println(" PAGE IN MEMORY ALREADY.");
			circularQ[index].refrenceBit = 1;//REFRESH					
			this.tot_cycles +=1;
			page_place = index;
		}
		else if (index == -1){//PAGE NOT IN MEMORY											   
			this.tot_cycles += 300;//300 CYCLES TO GET PAGE FROM DISK.
			this.readyQ.allProcesses.get(pid -1).pageFaults++;//INC  # OF PAGE FAULT IN PROCESS

			if (this.isFull()){	//NEEDS REPLACEMENT																	 
				System.out.println(" PAGE NOT IN MEMORY & MEMORY FULL.");
				int indexToReplaceWith = SecondChance();
				setMemory(pid, page_num, indexToReplaceWith);
				index_we_pointed_at = (++index_we_pointed_at)%mem_size;
				page_place = indexToReplaceWith;
			}
			else{ //STILL HAVE SPACE																	 
				System.out.println(" PAGE NOT IN MEMORY BUT MEMORY STILL HAVE SPACE.");
				int emptyIndex = this.getFirstEmptyIndex();
				circularQ[emptyIndex].pid = pid;
				circularQ[emptyIndex].page_num = page_num;
				circularQ[emptyIndex].refrenceBit = 1;
				page_place = emptyIndex;
				
			}
		}
		this.printMemory(page_place);
		System.out.println(" ____________________________________________________________________________________________");
		}
	
}

//THIS CLASS SIMULATES THE MEMORY PAGE
class MemoryObject {
	int page_num;
	int pid;
	int refrenceBit;

	public MemoryObject() {
		this.page_num = -1;
		this.pid = -1;
		this.refrenceBit = -1;
	}
}
