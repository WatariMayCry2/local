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
				File sampleDir = new File("./" + dirs[i]);
				if(!sampleDir.exists()){
					sampleDir.mkdir();
				}
				String[] sampleFiles = sampleDir.list();
				for(int j = 0; j < sampleFiles.length; j++){
					File tmpFile = new File("./" + dirs[i] + "/" + sampleFiles[j]);
					tmpFile.delete();
				}
			}
			Scanner sc = new Scanner(System.in);
			URLConnection uc = new URL(sc.next()).openConnection();
			uc.connect();
			BufferedReader bf = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String read;
			int count = 0;
			boolean isWriting = false;
			int inputType = 0;//1:in 2:out
			StringBuilder sb = new StringBuilder("");
			while((read = bf.readLine()) != null){
				//myout(read);
				if(!isWriting && (read.contains(INPUT_DIR_STRING) || read.contains(OUTPUT_DIR_STRING))){
					if(read.contains(INPUT_DIR_STRING)){
						inputType = 1;
						count++;
					}else{
						inputType = 2;
					}
					isWriting = true;
					read = read.substring(read.lastIndexOf("<pre>") + 5);
					sb.append(read + "\n");
					continue;
				}
				if(isWriting){
					if(read.contains("</pre>")){
						isWriting = false;
						inputType = 0;
						File testFile = new File("./" + ((inputType == 1) ? INPUT_DIR_STRING : OUTPUT_DIR_STRING) + "/" + count + ".txt");
						testFile.createNewFile();
						FileWriter fileWriter = new FileWriter(testFile);
						fileWriter.write(sb.toString());
						fileWriter.close();
						myout("TestCase" + count);
						myout(sb.toString());
						sb = new StringBuilder("");
					}else{
						sb.append(read + "\n");
					}
				}
			}
			bf.close();
		}catch(IOException e){
			myout("invalid URL or irregular file control.");
		}
	}
	static void myout(Object t){
		System.out.println(t);
	}
}
