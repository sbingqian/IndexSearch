import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

public class SplitFileTask {

	public static void splitFile(File theFile, int lineSize) {
		
		String lastLineContent = "";
		long ErrCount = 0l;
		try (LineIterator it = FileUtils.lineIterator(theFile, "UTF-8")) {
			long i = 0l;
			int fileSuffix = 1;
			FileWriter fw = new FileWriter(new File("D:\\Lucene_Dir\\splitFiles\\" + fileSuffix + ".txt"));;
			while (it.hasNext()) {
				i++;
				if (i % lineSize == 0) {
					fileSuffix ++;
					System.out.println("拆分至" + fileSuffix + "个文件");
					fw = new FileWriter(new File("D:\\Lucene_Dir\\splitFiles\\" + fileSuffix + ".txt"));;
				}
				
				String s = it.nextLine();
	
				if (StringUtils.isEmpty(lastLineContent) && s.split("	").length < 5) {
					ErrCount++;
					lastLineContent = s;
					i--;
					continue;
				} else {
					fw.write(lastLineContent + s + "\r\n");
					fw.flush();
					lastLineContent = "";
				}
				
			}

			System.out.println("文件处理完成 : ErrCount :" + ErrCount + " 总行数 ：" + i );
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
