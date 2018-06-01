package mju_avengers.please_my_fridge.match_persent;

import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import mju_avengers.please_my_fridge.data.FoodComponentsData;
import mju_avengers.please_my_fridge.data.FoodPersentData;

public class MakeMatchRate {
	private ArrayList<String> ids;
	private ArrayList<String> my_fridge;
	private ArrayList<FoodPersentData> priority_list = new ArrayList();
	
	public MakeMatchRate(ArrayList<String> my_fridge) {
		//id는 안먹은 음식들
		//my_fridge는 내 냉장고 식재료들
		this.my_fridge = my_fridge;
	}

	public ArrayList<FoodPersentData> getDirectory(ArrayList<FoodComponentsData> datas) {

		
		ArrayList <Float> Match_rate_list = new ArrayList<Float>();
		for(int i = 0 ; i<datas.size() ; i++){
			int numberofcomponent = 0;
			int numberofmatch = 0;
			float Match_rate = 0;
			for (String component : datas.get(i).component2()) {
				for (String my_component : my_fridge) {
					if (my_component.equals(component)) {
						numberofmatch += 1;
					}
				}
				numberofcomponent += 1;
			}
			Match_rate = (float) numberofmatch / (float) numberofcomponent;
			Match_rate_list.add(Match_rate * 100);
		}

		
		// Make Matching rage dictionary => [[1, 0.0],[2, 18.181818], [3, 100.0]]
		Map<String, Float> Match_rate_with_id = new HashMap<String, Float>();
		for(int i = 0; i < Match_rate_list.size(); i++) {
			Match_rate_with_id.put(datas.get(i).component1(), Match_rate_list.get(i));
		}
		

		
		// Sort dictionary
		ValueComparator bvc = new ValueComparator(Match_rate_with_id);
        TreeMap<String, Float> sorted_map = new TreeMap<String, Float>(bvc);
        sorted_map.putAll(Match_rate_with_id);
        
        // Get a set of the entries
        Set set = sorted_map.entrySet();
     
        // Get an iterator
        Iterator it = set.iterator();
     
        TreeMap<String, Float> cut_map = new TreeMap<String, Float>(bvc);
        
        // Display elements
        for(int i = 0; it.hasNext(); i++) {
        	Map.Entry me = (Map.Entry)it.next();
        	cut_map.put((String)me.getKey(), (float)me.getValue());
        	
        }
        
        // Get a set of the entries
        Set cut_map_set = cut_map.entrySet();
     
        // Get an iterator
        Iterator cut_map_it = cut_map_set.iterator();
        
        while (cut_map_it.hasNext()) {
        	Map.Entry me = (Map.Entry)cut_map_it.next();
        	String key_id = me.getKey().toString();
        	String value_percent;
        	if(me.getValue().toString().equals("NaN")){
				value_percent = "0";
			} else {
        		value_percent = me.getValue().toString();
			}
        	FoodPersentData temp = new FoodPersentData(key_id, value_percent);
        	priority_list.add(temp);
        }

		Log.e("일치율 계산", priority_list.toString());
        return priority_list;
	}
	
	class ValueComparator implements Comparator<String> {
	    Map<String, Float> base;

	    public ValueComparator(Map<String, Float> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with
	    // equals.
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
}

