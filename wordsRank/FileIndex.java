package il.ac.tau.cs.sw1.ex8.wordsRank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import il.ac.tau.cs.sw1.ex8.histogram.HashMapHistogram;
import il.ac.tau.cs.sw1.ex8.wordsRank.RankedWord.rankType;

/**************************************
 *  Add your code to this class !!!   *
 **************************************/

public class FileIndex {
	
	public static final int UNRANKED_CONST = 30;
	private Map<String, HashMapHistogram<String>> fileMap;
	private Map<String, Map<String, Integer>> rankMap;
	private Map<String, Map<String, Integer>> mapByWord;
	
	public FileIndex() {
		fileMap = new HashMap<String, HashMapHistogram<String>>();
		rankMap = new HashMap<String, Map<String,Integer>>();
		mapByWord = new HashMap<String, Map<String,Integer>>();
	}
	/*
	 * @pre: the directory is no empty, and contains only readable text files
	 */
  	public void indexDirectory(String folderPath){
		//This code iterates over all the files in the folder. add your code wherever is needed

		File folder = new File(folderPath);
		File[] listFiles = folder.listFiles();
		List <String> allWords = new ArrayList<>();
		for (File file : listFiles) {
			// for every file in the folder
			if (file.isFile()) {
				
				HashMapHistogram<String> temp = new HashMapHistogram<>();
				Map<String, Integer> tempRank = new HashMap<>();
				int i = 1;
				List<String> tokens;
				try {
					allWords.addAll(FileUtils.readAllTokens(file));
					tokens = FileUtils.readAllTokens(file);
					temp.addAll(tokens);
					fileMap.put(file.getName(), temp);
					
					Iterator<String> iter = temp.iterator();
					while (iter.hasNext()) {
						tempRank.put(iter.next(), i);
						i++;
					}
					
					rankMap.put(file.getName(), tempRank);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
				
		}
		for (File file : listFiles) {
			if (file.isFile()) {
				int size = rankMap.get(file.getName()).size();
				for (String word: allWords) {
					if (!rankMap.get(file.getName()).containsKey(word.toLowerCase())) {
						rankMap.get(file.getName()).put(word.toLowerCase(), size + UNRANKED_CONST);
					}
				}
			}
		}
		for (String word: allWords) {
			Map<String, Integer> tempMap = new HashMap<>();
			for (File file : listFiles) {
				if (file.isFile()) {
					tempMap.put(file.getName(), rankMap.get(file.getName()).get(word.toLowerCase()));
				}
			}
			mapByWord.put(word.toLowerCase(), tempMap);
		}
		
	}
	
	
  	/*
	 * @pre: the index is initialized
	 * @pre filename is a name of a valid file
	 * @pre word is not null
	 */
	public int getCountInFile(String filename, String word) throws FileIndexException{
		if (fileMap.containsKey(filename)) {
			int counts = fileMap.get(filename).getCountForItem(word.toLowerCase());
			return counts;
		}
		else {
			throw new FileIndexException("File couldn't be found in the folder.");
		}
		
	}
	
	/*
	 * @pre: the index is initialized
	 * @pre filename is a name of a valid file
	 * @pre word is not null
	 */
	public int getRankForWordInFile(String filename, String word) throws FileIndexException{
		if (rankMap.containsKey(filename)) {
			if (rankMap.get(filename).containsKey(word.toLowerCase())) {
				return rankMap.get(filename).get(word.toLowerCase());
			}
			else {
				return fileMap.get(filename).size() + UNRANKED_CONST;
			}
		}
		else {
			throw new FileIndexException("File couldn't be found in the folder.");
		}

	}
	
	/*
	 * @pre: the index is initialized
	 * @pre word is not null
	 */
	public int getAverageRankForWord(String word){
		Map <String, Integer> map = new HashMap<String, Integer>();
		for (String file: rankMap.keySet()) {
			try {
				map.put(file, getRankForWordInFile(file, word.toLowerCase()));
			} catch (FileIndexException e) {
				e.printStackTrace();
			}
		}
		RankedWord wordRank = new RankedWord(word.toLowerCase(), map);
		return wordRank.getRankByType(rankType.average); 
	}
	
	
	public List<String> getWordsWithAverageRankSmallerThanK(int k){
		List<RankedWord> toRet = new ArrayList<>();
		for (String word: mapByWord.keySet()) {
			RankedWord temp = new RankedWord(word.toLowerCase(), mapByWord.get(word.toLowerCase()));
			if (temp.getRankByType(rankType.average) < k) {
				toRet.add(temp);
			}
		}
		Collections.sort(toRet, new RankedWordComparator(rankType.average));
		List <String> toRetFinal = new ArrayList<String>();
		for (RankedWord word: toRet) {
			toRetFinal.add(word.getWord().toLowerCase());
		}
		return toRetFinal; 
	}
	
	public List<String> getWordsWithMinRankSmallerThanK(int k){
		List<RankedWord> toRet = new ArrayList<>();
		for (String word: mapByWord.keySet()) {
			RankedWord temp = new RankedWord(word.toLowerCase(), mapByWord.get(word.toLowerCase()));
			if (temp.getRankByType(rankType.min) < k) {
				toRet.add(temp);
			}
		}
		Collections.sort(toRet, new RankedWordComparator(rankType.average));
		List <String> toRetFinal = new ArrayList<String>();
		for (RankedWord word: toRet) {
			toRetFinal.add(word.getWord().toLowerCase());
		}
		return toRetFinal; 
	}
	
	public List<String> getWordsWithMaxRankSmallerThanK(int k){
		List<RankedWord> toRet = new ArrayList<>();
		for (String word: mapByWord.keySet()) {
			RankedWord temp = new RankedWord(word.toLowerCase(), mapByWord.get(word.toLowerCase()));
			if (temp.getRankByType(rankType.max) < k) {
				toRet.add(temp);
			}
		}
		Collections.sort(toRet, new RankedWordComparator(rankType.average));
		List <String> toRetFinal = new ArrayList<String>();
		for (RankedWord word: toRet) {
			toRetFinal.add(word.getWord().toLowerCase());
		}
		return toRetFinal; 
	}

}
