package nl.utwente.ir.services.search;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Facet {
	public static final int MAX_FACET_OPTIONS = 5;
	
	private String title;
	private Map<String, Integer> values; // Value, Count

	public Facet(String title) {
		this.title = title;
		this.values = new HashMap<String, Integer>();
	}
	
	public void indent(String value) {
		int current = 0;
		if(!values.containsKey(value)) {
			values.put(value, 0);
		} else {
			current = this.values.get(value).intValue();
		}
		this.values.put(value, ++current);
	}
	
	public void add(String value) {
		values.put(value, 0);
	}
	
	public String getTitle() {
		return title;
	}
	
	public TreeMap<String, Integer> getSortedValues() {
        ValueComparator bvc =  new ValueComparator(values);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        sorted_map.putAll(values);
        return sorted_map;
	}
		
	public String getAllOptions() {
		String output = "";
		output += "<ul class=\"entries\">";
		for(Map.Entry<String, Integer> entry : getSortedValues().entrySet()) {
			if(!entry.getKey().equals("")) {
				output += "<li class=\"item\">";
				if(entry.getValue() > 0) {
					output += "<a id=\"" + entry.getKey() + "\" data-filter=\"" + title + "\">" + entry.getKey() + " (" + entry.getValue() + ")</a>"; 
				} else {
					output += "<a id=\"" + entry.getKey() + "\" data-filter=\"" + title + "\" class=\"muted\">" + entry.getKey() + "</a>";
				}
				output += "</li>";
			}
		}
		output += "</ul>";
		 
		return output; // Hide facet if only one option is available
	}
	
	class ValueComparator implements Comparator<String> {

	    Map<String, Integer> base;
	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        }
	    }
	}
}
