package mju_avengers.please_my_fridge.dictionary;

import java.util.ArrayList;
import java.util.HashMap;

public class Hashmap<K, V> {

    HashMap<K, ArrayList<V>> map = new HashMap<>();

    public ArrayList<V> get(K key) {
        return map.get(key);
    }

    public void put(K key, V value) {
        ArrayList<V> existingValues = get(key);
        if (existingValues == null) existingValues = new ArrayList<>();
        existingValues.add(value);
        map.put(key, existingValues);
    }

}
