import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalysisMainClass {

	static ExecutorService executor = Executors.newFixedThreadPool(1);
	
	public static void main(String args[]) {
		
//		SplitFileTask.splitFile(new File("D:\\Lucene_Dir\\t_electronic_shop_goods.txt"), 100000);
		
		File path = new File("D:\\Lucene_Dir\\splitFiles");
		String[] filenNames = path.list();
		
		int porcessLimt = 19;
		int fileCnt = 0;
		for(String fileName : filenNames) {
			fileCnt++;
			if (fileCnt > porcessLimt) {
				break;
			}
			AnalysisMainClass.executor.execute(new DocFileCreateTask(new File(path, fileName)));
		}
		
		AnalysisMainClass.executor.shutdown();
	}
}
