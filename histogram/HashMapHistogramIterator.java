package il.ac.tau.cs.sw1.ex8.histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;


/**************************************
 *  Add your code to this class !!!   *
 **************************************/
public class HashMapHistogramIterator<T extends Comparable<T>> implements Iterator<T>{
	
	private Map<T, Integer> map;
	private int index;
	private ArrayList<T> keys_list;
	
	public HashMapHistogramIterator(Map<T, Integer> map1) {
		map = map1;
		index = 0;
		keys_list = new ArrayList<T>( map.keySet());
		Collections.sort(keys_list, new ComparatorMap(map));
	}
	
	
	@Override
	public boolean hasNext() {
		if (index < keys_list.size()) {
			return true;
		}
		return false;
	}

	@Override
	public T next() {
		T value = keys_list.get(index);
		index++;
		return value;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException(); //no need to change this
	}
	
	public class ComparatorMap implements Comparator<T>{
		
		private Map<T,Integer> map;
		
		public ComparatorMap(Map<T,Integer> map) {
			this.map = map;
		}
		
		@Override
		public int compare(T key1, T key2) {
			if (this.map.get(key1) < this.map.get(key2)) {
				return 1;
			}
			else if (this.map.get(key1) > this.map.get(key2)) {
				return -1;
			}
			else {
				return key1.compareTo(key2);
			}
		}
		
	}
}
