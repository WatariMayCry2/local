import java.net.*;
import java.util.*;
import java.io.*;
public class Fetch{
	static final String INPUT_DIR_STRING = "Sample Input";
	static final String OUTPUT_DIR_STRING = "Sample Output";
	Fetch(){}
	public static void main(String[] args){
		try{
			String[] dirs = {INPUT_DIR_STRING, OUTPUT_DIR_STRING};
			for(int i = 0; i < dirs.length; i++){
				File tmpDir = new File("./" + dirs[i]);
				if(!tmpDir.exists()){
					tmpDir.mkdir();
				}
				String[] multiTestFiles = tmpDir.list();
				for(int j = 0; j < multiTestFiles.length; j++){
					File tmpFile = new File("./" + dirs[i] + "/" + multiTestFiles[j]);
					tmpFile.delete();
				}
			}
			Scanner sc = new Scanner(System.in);
			URL url = new URL(sc.next());
			URLConnection uc = url.openConnection();
			uc.connect();
			BufferedReader bf = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String read;
			int count = 0;
			boolean readnow = false;
			int inputType = 0;//1:in 2:out
			StringBuilder sb = new StringBuilder("");
			while((read = bf.readLine()) != null){
				if(!readnow && (read.contains(INPUT_DIR_STRING) || read.contains(OUTPUT_DIR_STRING))){
					if(read.contains(INPUT_DIR_STRING)){
						inputType = 1;
						count++;
					}else{
						inputType = 2;
					}
					readnow = true;
					String replaceMae = "<h3>" + ((inputType == 1) ? INPUT_DIR_STRING : OUTPUT_DIR_STRING) + " " + count + "</h3><pre>";
					read = read.replace(replaceMae, "");
					sb.append(read + "\n");
					continue;
				}
				if(readnow){
					if(read.contains("</pre>")){
						readnow = false;
						File testFile = new File("./" + ((inputType == 1) ? INPUT_DIR_STRING : OUTPUT_DIR_STRING) + "/" + count + ".txt");
						testFile.createNewFile();
						FileWriter fileWriter = new FileWriter(testFile);
						fileWriter.write(sb.toString());
						fileWriter.close();
						System.out.println("TestCase" + count);
						System.out.println(sb.toString());
						sb = new StringBuilder("");
						inputType = 0;
					}else{
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
