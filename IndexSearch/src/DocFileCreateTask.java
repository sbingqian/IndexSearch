import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.google.gson.Gson;

public class DocFileCreateTask implements Runnable {

	static HashMap<String, FileWriter> fwMap = new HashMap<String, FileWriter>();

	File processFile;

	public DocFileCreateTask(File processFile) {
		this.processFile = processFile;
	}

	@Override
	public void run() {

		Date dateTime = new Date();
		
		try (LineIterator it = FileUtils.lineIterator(processFile, "UTF-8")) {
			long i = 0l;
			while (it.hasNext()) {
				i++;
				String line = it.nextLine();
				String sLine = line.replaceAll("\"", "");
				String contents[] = sLine.split("	");
				if (contents.length == 30) {
					List<String> list = Arrays.asList(contents);
					writeFile(list, 0);
				} else {
					Pattern p1 = Pattern.compile("\"(.*?)\"");
					Matcher m = p1.matcher(line);
					ArrayList<String> list = new ArrayList<String>();
					while (m.find()) {
						list.add(m.group().trim().replace("\"", "") + " ");
					}

					if (list.size() == 30) {
						writeFile(list, 0);
					} else if (list.size() > 30 && list.size() < 40) {
						int j = 1;
						for (; j < list.size(); j++) {
							if ("京东".equals(list.get(j).trim())) {
								break;
							}
						}
						writeFile(list, j - 4);
					} else {
						System.out.println(processFile.getName() + " 文件, line : " + i + " 内容: " + list);
					}
				}
			}

			System.out.println(processFile.getName() + " 运行结束，耗时: "
					+ ((System.currentTimeMillis() - dateTime.getTime()) / 1000) + "秒");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(List<String> list, int offset) throws IOException {

		String fileName = list.get(5 + offset).trim() + ".txt";

		if (fwMap.get(fileName) == null) {
			fwMap.put(fileName, new FileWriter(new File("D:\\Lucene_Dir\\docs\\" + fileName)));
		}

		HashMap<String, String> contentMap = new HashMap<String, String>();
		contentMap.put("id", list.get(0).trim());
		contentMap.put("name", list.get(2).trim());
		contentMap.put("level-1", list.get(5 + offset).trim());
		contentMap.put("level-2", list.get(6 + offset).trim());
		contentMap.put("level-3", list.get(7 + offset).trim());
		contentMap.put("brandName", list.get(10 + offset).trim());
		String content = new Gson().toJson(contentMap);
		fwMap.get(fileName).write(content + "\r\n");
		fwMap.get(fileName).flush();
	}

}
