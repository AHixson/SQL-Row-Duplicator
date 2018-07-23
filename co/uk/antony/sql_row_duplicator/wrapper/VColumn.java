package co.uk.antony.sql_row_duplicator.wrapper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 
 * @author Antony Hixson
 *
 */
public class VColumn {

	private final Set<String> uniqueData;
	
	public VColumn() {
		this.uniqueData = new HashSet<>();
	}
	
	public int size() {
		return uniqueData.size();
	}
	
	public void clear() {
		uniqueData.clear();
	}
	
	public void add(String aString) {
		if (aString != null && !aString.isEmpty()) {
			uniqueData.add(aString);
		}
	}
	
	public String get(int index) {
		String str = null;
		int i = 0;
		if (index >= 0 && index < size()) {
			for (String next : uniqueData) {
				if (i == index) {
					str = next;
					break;
				}
				i++;
			}
		} else {
			throw new IndexOutOfBoundsException("Virtual column does not have an index of " + index);
		}
		return str;
	}
	
	public String getRandom() {
		return get(new Random().nextInt(size()));
	}
	
	public Set<String> getAll() {
		return uniqueData;
	}
}
