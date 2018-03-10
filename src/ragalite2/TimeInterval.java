package ragalite2;

import java.util.ArrayList;
import java.io.Serializable;

public class TimeInterval implements Serializable {
        
        private int from;
        private int to;
	
	public TimeInterval(int from, int to) {
		this.from = from;
		this.to   = to;
	}

	public String toString() {
		return "(" + from + ", " + to + ")";
	}
	/*
        public static int duration (int from, int to){
                this.from = from;
                this.to = to;
	         return duration = from - to;
        }

                public int getTo(){
                return to;
                }
                public void int setTo(int to){
                this.to = to;
                }
                public int getFrom(){
                return from;
                }
                public void int setFrom{
                this.from = from;
                }
	


        public static void main (String [] args){
          
                 ArrayList<String> DailyRecord  = new ArrayList<>();


        }        
	for (int i = 0; i <24; i++){
		String activity(i) = duration(from, to); 
       		24-= duration;

                 DailyRecord.add(activity(i));
	 }	
	 */
}

	
