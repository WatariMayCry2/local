import java.net.*;
import java.util.*;
import java.io.*;
public class Fetch{
	static final String MULTI_TEST_DIR_STRING = "./multiTest";
	Fetch(){}
	public static void main(String[] args){
		try{
			File multiTestDir = new File(MULTI_TEST_DIR_STRING);
			String[] multiTestFiles = multiTestDir.list();
			for(int i = 0; i < multiTestFiles.length; i++){
				File tmpFile = new File(MULTI_TEST_DIR_STRING + multiTestFiles[i]);
				tmpFile.delete();
			}
			Scanner sc = new Scanner(System.in);
			URL url = new URL(sc.next());
			URLConnection uc = url.openConnection();
			uc.connect();
			BufferedReader bf = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String read;
			int count = 0;
			boolean readnow = false;
			StringBuilder sb = new StringBuilder("");
			while((read = bf.readLine()) != null){
				if(!readnow && read.contains("Sample Input")){
					readnow = true;
					count++;
					read = read.replace("<h3>Sample Input " + count + "</h3><pre>", "");
					System.out.println(read);
					sb.append(read + "\n");
					continue;
				}
				if(readnow){
					if(read.equals("</pre>")){
						readnow = false;
						File testFile = new File("./multiTest/" + count + ".txt");
						testFile.createNewFile();
						FileWriter fileWriter = new FileWriter(testFile);
						fileWriter.write(sb.toString());
						fileWriter.close();
						sb = new StringBuilder("");
					}else{
						System.out.println(read);
						sb.append(read + "\n");
					}
				}
			}
			bf.close();
		}catch(IOException e){
			System.out.println("invalid URL or irregular file control.");
		}
	}
}
