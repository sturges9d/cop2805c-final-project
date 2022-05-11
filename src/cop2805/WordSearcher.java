/*
 * Class......: COP 2805C-22809
 * Name.......: Stephen Sturges
 * Date.......: 04/21/2022
 * Description: WordSearcher class for COP 2805C, final project.
 */
package cop2805;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WordSearcher {
	List<String> lines = new ArrayList<String>();
	
	public WordSearcher(String fileName) {
		Path path = Paths.get(fileName);
		try	{
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		lines.replaceAll(String::toUpperCase);
	} // End of WordSearcher method.
	
	public List<Integer> SearchLines(String searchString) {
		List<Integer> resultsList = new ArrayList<Integer>();
		searchString = searchString.toUpperCase();
		for(int i = 0; i < lines.size(); i++) {
			String str = lines.get(i);
			if(str.indexOf(searchString) >= 0)
				resultsList.add(i);
		}
		return resultsList;
	} // End of SearchLines method.
} // End of WordSearcher class.
