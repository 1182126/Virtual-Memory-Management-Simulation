import java.util.ArrayList;
// CLASS THAT SIMULATES THE MEMORY TRACE, MEMORY ADDRESS ACCESSED BY EACH PROCESS IN THE SYSTEM.
public class TRACE {
	ArrayList<Integer> PPA;//PPA: PROCCESS PAGE ADDRESS
	int reach_page ;
	TRACE(){//MEMORY TRACE CONSTRUCTOR.
		PPA = new ArrayList<Integer>();
		reach_page = 0;
	}
//THIS METHOD TO GET THE NEXT ADDRESS ACCESSED BY THE PROCESS.
	public int getPage(){
		if(reach_page > PPA.size()-1)
			return -1;//PROCESS FINISHED EXECUTING ON CPU
		return PPA.get(reach_page++);
	}
//THIS METHOD CHECKS IF PROCESS DONE EXECUTING ON CPU-->IF # OF PAGES EXECUTED ON CPU EQUALS THE NUMBER OF PAGE REF IN THE MEM_TRACE FILE OF PROCCES.
	public boolean isFinishedPages(){
		return this.reach_page >= (this.PPA.size() - 1) ; 
	}
}
