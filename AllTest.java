import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.lang.*;
class AllTest{
	static final String INPUT_DIR_STRING = "Sample Input";
	static final String OUTPUT_DIR_STRING = "Sample Output";
	AllTest(){}
	public static void main(String[] arg){
		Runtime rt = Runtime.getRuntime();
		File testDir = new File("./" + INPUT_DIR_STRING);
		if(!testDir.exists()){
			myout("Get the test case first.");
			return;
		}
		String[] testFiles = testDir.list();
		String[] command = {"java", "Main"};
		for(int i = 0; i < testFiles.length; i++){
			int count = i + 1;
			ProcessBuilder pb = new ProcessBuilder(command);
			pb = pb.redirectInput(new File("./" + INPUT_DIR_STRING + "/" + testFiles[i]));
			Process p = null;
			try{
				p = pb.start();
			}catch(IOException e){myout(e);}
			try{
				boolean tl = p.waitFor(new Long("2000"), TimeUnit.MILLISECONDS);
				if(!tl){
					myout("[TLE] " + count + " (It will not be processed after that.)");
					break;
				}
			}catch(InterruptedException e){myout(e);}
			
			InputStream is = null;
			if(p.exitValue() == 0){
				is = p.getInputStream();
			}else{
				myout("[RE] " + count);
				is = p.getErrorStream();
			}
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));
			String result = createString(bf);
			if(p.exitValue() == 0){
				boolean isAns = true;
				String ans = "";
				try{
					BufferedReader ansBf = new BufferedReader(new FileReader(new File("./" + OUTPUT_DIR_STRING + "/" + testFiles[i])));
					ans = createString(ansBf);
				}catch(IOException e){
					isAns = false;
				}
				myout(result);
				if(isAns){
					if(ans.equals(result)){
						myout("[AC] " + count);
					}else{
						myout("[WA] " + count);
						myout(ans);
					}
				}else{
					myout("[NO ANS] " + count);
				}
			}
			myout("-------------------------");
			if(p != null){
				p.destroy();
			}
		}
	}
	static void myout(Object t){
		System.out.println(t);
	}
	static String createString(BufferedReader bf){
		StringBuilder sb = new StringBuilder("");
		String read;
		try{
			while((read = bf.readLine()) != null){
				sb.append(read + "\n");
			}
		}catch(IOException e){}
		return sb.toString();
	}
}
