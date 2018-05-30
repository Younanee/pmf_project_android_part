package mju_avengers.please_my_fridge.dictionary;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Processing {

	public Boolean exist = false;
	public List<String> results = new ArrayList<String>();
	public String[] str;

	public Processing(String[] str) {
		this.str = str;
	}
	public List<String> getGredients() {
		for(int q = 0; q < str.length; q++) {
			String line = str[q];
			if (line==null) break;
			for(int i = 0; i < Constants.special.length; i++){
				if(line.contains(Constants.special[i])){
					for(int j = 0; j < Constants.replacement.length; j++){
						if(line.contains(Constants.replacement[j])){
							results.add(Constants.replacement[j]);
							exist = true;
							break;
						}
					}
					if(exist == false){
						results.add(Constants.special[i]);
					}
					break;
				}
			}
			for(int k = 0; k < Constants.components.length; k++){
				if(line.contains(Constants.components[k])){
					results.add(Constants.components[k]);
					break;
				}
			}
		}
		List<String> deduped = results.stream().distinct().collect(Collectors.<String>toList());
		return deduped;
	}

}
