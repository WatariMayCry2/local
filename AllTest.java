import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.lang.*;
class AllTest{
	static final String INPUT_DIR_STRING = "Sample Input";
	static final String OUTPUT_DIR_STRING = "Sample Output";
	static String[] execCommand = {"java", "Main"};
	AllTest(){}
	public static void main(String[] arg){
		if(!compile()){
			return;
		}

		File testDir = new File("./" + INPUT_DIR_STRING);
		if(!testDir.exists()){
			myout("Get the test case first.");
			return;
		}
		String[] testFiles = testDir.list();
		for(int i = 0; i < testFiles.length; i++){
			int count = i + 1;
			ProcessBuilder pb = new ProcessBuilder(execCommand);
			pb = pb.redirectInput(new File("./" + INPUT_DIR_STRING + "/" + testFiles[i]));
			Process p = null;
			try{
				p = pb.start();
			}catch(IOException e){myout(e);}
			try{
				boolean tl = p.waitFor(2000l, TimeUnit.MILLISECONDS);
				if(!tl){
					myout("[TLE] " + count + " (It will not be processed after that.)");
					break;
				}
			}catch(InterruptedException e){myout(e);}
			
			InputStream is = null;
			int isAns = p.exitValue();
			if(isAns == 0){
				is = p.getInputStream();
			}else{
				myout("[RE] " + count);
				is = p.getErrorStream();
			}
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));
			String result = createString(bf);
			
			String ans = "";
			try{
				BufferedReader ansBf = new BufferedReader(new FileReader(new File("./" + OUTPUT_DIR_STRING + "/" + testFiles[i])));
				ans = createString(ansBf);
			}catch(IOException e){
				if(isAns == 0){
					isAns = -1;
				}
			}
			myout(result);
			if(isAns == 0){
				if(ans.equals(result)){
					myout("[AC] " + count);
				}else{
					myout("[WA] " + count);
					myout(ans);
				}
			}else if(isAns == -1){
				myout("[NO ANS] " + count);
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
	static boolean compile(){
		boolean isOK = false;
		String[] compaileCommand = {"javac", "Main.java"};
		ProcessBuilder pb = new ProcessBuilder(compaileCommand);
		Process p = null;
		try{
			p = pb.start();
		}catch(IOException e){myout(e);}
		try{
			p.waitFor();
		}catch(InterruptedException e){myout(e);}
		if(p.exitValue() != 0){
			String compaileError = createString(new BufferedReader(new InputStreamReader(p.getErrorStream())));
			myout("[CE] (The program will not run.)");
			myout(compaileError);
		}else{
			isOK = true;
		}
		if(p != null){
			p.destroy();
		}
		return isOK;
	}
}
