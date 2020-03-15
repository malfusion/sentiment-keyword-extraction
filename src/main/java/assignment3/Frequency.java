package assignment3;

public class Frequency<T> implements Comparable<Frequency>{
		
		T key;
		int freq;
		
		public Frequency(T key, int freq) {
			this.key = key;
			this.freq= freq;
		}
		
		public T getKey() {
			return key;
		}
		
		public int getFreq() {
			return freq;
		}
	    

		public int compareTo(Frequency o) {
			return Integer.compare(this.getFreq(), o.getFreq());
		}

}
