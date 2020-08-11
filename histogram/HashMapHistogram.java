package il.ac.tau.cs.sw1.ex8.histogram;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**************************************
 *  Add your code to this class !!!   *
 **************************************/
public class HashMapHistogram<T extends Comparable<T>> implements IHistogram<T>{

	
	private Map<T, Integer> histo;
	private int size;
	
	public HashMapHistogram() {
		histo = new HashMap<T,Integer>();
		size = 0;
	}
	
	@Override
	public Iterator<T> iterator() {
		HashMapHistogramIterator<T> iter = new HashMapHistogramIterator<T>(this.histo);
		return iter;
	}

	@Override
	public void addItem(T item) {
		if (histo.containsKey(item)) {
			histo.put(item, histo.get(item)+1);
		}
		else {
			histo.put(item, 1);
			size++;
		}
		
	}

	@Override
	public void removeItem(T item) throws IllegalItemException {
		if (histo.containsKey(item)) {
			if (histo.get(item) == 1) {
				histo.remove(item);
			}
			else {
				histo.put(item, histo.get(item) - 1);
			}
		}
		else {
			throw new IllegalItemException();
		}
		
	}

	@Override
	public void addItemKTimes(T item, int k) throws IllegalKValueException {
		if (k > 0) {
			if (histo.containsKey(item)) {
				histo.put(item, histo.get(item)+k);
			}
			else {
				histo.put(item, k);
				size++;
			}
		}
		else {
			throw new IllegalKValueException(k);
		}
		
	}

	@Override
	public void removeItemKTimes(T item, int k) throws IllegalItemException, IllegalKValueException {
		if (histo.containsKey(item)) {
			if (k <= 0 || k > histo.get(item)) {
				throw new IllegalKValueException(k);
			}
			
			else if (k == histo.get(item)){
				histo.remove(item);
				size--;
			}
			
			else {
				histo.put(item, histo.get(item) - k);
			}
		}
		
		else {
			throw new IllegalItemException();
		}
		
	}

	@Override
	public int getCountForItem(T item) {
		if (histo.containsKey(item)) {
			return histo.get(item);
		}
		else {
			return 0;
		}
	}

	@Override
	public void addAll(Collection<T> items) {
		for (T item : items) {
			this.addItem(item);
		}
		
	}

	@Override
	public void clear() {
		histo.clear();
		size = 0;
		
	}

	@Override
	public Set<T> getItemsSet() {
		return histo.keySet();
	}

	@Override
	public void update(IHistogram<T> anotherHistogram) {
		Iterator<T> iter = anotherHistogram.iterator();
		while(iter.hasNext()) {
			T item = iter.next();
			int k = anotherHistogram.getCountForItem(item);
			try {
				this.addItemKTimes(item, k);
			} catch (IllegalKValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int size() {
		return size;
	}

	
}
