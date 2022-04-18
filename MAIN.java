/*
 * OS PROJECT:FIRST SEMESTER
 * DR.AHMAD AFANEH
 * DONE BY: HEDAYA, RAND & ARWA.
 * SUNDAY_ 9/1/2022
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JOptionPane;

//MAIN CLASS FOR OUR PROJECT
public class MAIN {
    public static void main(String[] args) throws FileNotFoundException{
        // SELECT THE DATA SOURUCE
    System.out.println("                                        WELCOME TO OPERATING SYSTEMS COURSE PROJECT-2022. ");
	System.out.println("                                           INST: DR. AFANEH - BY:HEDAYA, ARWA & RAND ");
	System.out.println("*********************************************************************************************************************");
	System.out.println("CHOOSE THE DATA RESOURCE: ");
	System.out.println(" 1. FROM A FILE.");
	System.out.println(" 2. RANDOMLY GENERATEED FOR MEMORY TRACES.");
	System.out.print(" YOUR CHOICE: ");
        Scanner in = new Scanner(System.in);
	int dataSource = Integer.parseInt(in.nextLine());
        do{
            if(dataSource > 2 || dataSource < 1){
		System.out.println(" ERROR: THE NUMBER YOU CHOOSED NOT FOUND!!");
		System.out.print(" YOUR CHOICE: ");
		dataSource = Integer.parseInt(in.nextLine());
	    }
	}while(dataSource > 2 || dataSource < 1);
        // IF DATA SOURCE WAS FROM A FILE WE WILL CHECK IF THE FILE EXIST, THEN READ THE DATA                
        // NOTE: THE FILE SHOULD BE IN THE SAME DIRCTORY !!
       
            System.out.println("________________________________________________________________________________________________");
            System.out.println("ENTER THE FILE NAME (data.txt): ");
            String dataF =in.nextLine();
            File file;
            do{     
                file = new File(dataF);
                if (!file.exists()){
                    System.out.println(" ERROR: THE FILE IS NOT FOUND!!");
                    System.out.println(" ENTER THE FILE PATH: ");
                    dataF =in.nextLine();
                }
            }while(!file.exists());
            //---------------------------------------------------------------
            //READ DATA FROM FILE 
            try{
                File f = new File(dataF);
                in = new Scanner(f);
            }
            catch (FileNotFoundException e){
                JOptionPane.showMessageDialog(null, "file not found");
            }
            int n = Integer.parseInt(in.nextLine());// NUMBER OF PROCESSES 
            int m = Integer.parseInt(in.nextLine());// SIZE OF PHYSICAL MEMORY IN FRAMES             
            String s =in.nextLine();// MINIMUM FRAMES PER PROCESS 
            int timeQuantum = 0;
            READY_TO_RUN readyQueu = new READY_TO_RUN();
            int proc_accessed_addrs;
            int[] processesBurstCycles;
            PROCESS temp ;
            int[] processesTimeQuantums = new int [n] ; 
            int processID = 0;
            int processArrivalTime = 0;
            int processBurstTime = 0;
            int processSize = 0;
            String memoryTrace ;
            int totalSize = 0;
            for(int line = 0 ; line < n;){
                processID = in.nextInt();
                processArrivalTime = in.nextInt();
                processBurstTime =in.nextInt();
                processSize = in.nextInt();
                totalSize += processSize;
                memoryTrace = in.nextLine();
                temp = new PROCESS(processID, processArrivalTime, processBurstTime, processSize);
                readyQueu.addNewProcess(temp);
                if(dataSource == 1){
                    String memoeryA [] = memoryTrace.split(",");
                    for(String a : memoeryA){
                        int number = Integer.parseInt(a.replaceFirst("\\s*",""), 16);//PLEASE MAKE SURE THAT THRER IS NO SPACE AT THE END OF THE FILE
                        number = number /(16*16*16);// REMOVE THE LOWEST 12 BIT
                        readyQueu.allProcesses.get(line).memoryTrace.PPA.add(number);
                        processesTimeQuantums[line]++;
                    }
                }else{
                    String memoryA[] = new String[processSize];
                    int max_num = (int)Math.pow(2, 24) -1;
                    for (int i = 0; i < processSize; i++)
                    {
                    	int num = (int) Math.floor(Math.random() * max_num);
                    	String string_num = Integer.toHexString(num);
                    	while(string_num.length() <6)
                    		string_num  = "0" + string_num;
                    		//System.out.println("Memory trace = "+string_num+"from the process ID"+processID+"for try "+i);
                    		int number = Integer.parseInt(string_num, 16);
                    		number = number /(16*16*16);// REMOVE THE LOWEST 12 BIT
                    		//System.out.println("the page number = "+number);
                    		readyQueu.allProcesses.get(line).memoryTrace.PPA.add(number);
                    		processesTimeQuantums[line]++;
                    }
                        
                        }

                line++;
            } 
            // PRINT THE READY QUEUE --> PROCESS THAT COME FIRST WILL ENTER THE QUEUE FIRST (ACCORDING TO ARIVAL TIME)
            //String out = readyQueu.toString();
            //System.out.print(out);
            // SELECT THE PAGE REPLACEMENT ALGORITHM
            System.out.println("________________________________________________________________________________________________");
            System.out.println(" PLEASE, CHOOSE THE # OF ALGORITHM YOU WANT TO RUN NEXT: ");
            System.out.println(" 1. 2ND CHANCE ALGORITHM");
            System.out.println(" 2. CLOCK ALGORITHM ");
            System.out.print(" YOUR CHOICE: ");
            System.out.println("\n__________________________________________________________________________________________________");
            in = new Scanner(System.in);
            int your_choice = in.nextInt();
            do{
		if(your_choice > 2 || your_choice < 1){
            System.out.println(" ERROR!, THE NUMBER YOU CHOOSED NOT FOUND.");
		    System.out.print(" YOUR CHOICE: ");
		    your_choice = in.nextInt();}
	    } while(your_choice > 2 || your_choice < 1);
            processesBurstCycles = new int[processesTimeQuantums.length];
            timeQuantum = sumOfProcessesTimeQuantum(processesTimeQuantums) / processesTimeQuantums.length;
            System.out.println(" QUANTUM TIME = " + timeQuantum);
            System.out.println("__________________________________________________________________________________________________");
            ROUND_ROUBIN round_roubin = new ROUND_ROUBIN(readyQueu, timeQuantum, totalSize, processesBurstCycles,m);
           // System.out.println(Arrays.toString(processesBurstCycles));
            round_roubin.RR_Algorithm();
            round_roubin.printChart(processesBurstCycles);
            System.out.println("TOTAL CPU CYCLES is : " + round_roubin.physicalMemory.tot_cycles);
        }
    
        
    public static int sumOfProcessesTimeQuantum(int[] processesTimeQuantums){
	int s = 0;
	for (int i = 0; i < processesTimeQuantums.length; i++) {
            s += processesTimeQuantums[i];
	}
	return s;            
    }               													
}