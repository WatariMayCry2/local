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
		File multiTestDir = new File("./" + INPUT_DIR_STRING);
		if(!multiTestDir.exists()){
			System.out.println("Get the test case first.");
			return;
		}
		String[] inputFiles = multiTestDir.list();
		String[] command = {"java", "Main"};
		for(int i = 0; i < inputFiles.length; i++){
			int count = i + 1;
			ProcessBuilder pb = new ProcessBuilder(command);
			pb = pb.redirectInput(new File("./" + INPUT_DIR_STRING + "/" + inputFiles[i]));
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
			BufferedReader bf = null;
			if(p.exitValue() == 0){
				bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
			}else{
				myout("[RE] " + count);
				bf = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			}
			
			String read;
			StringBuilder result = new StringBuilder("");
			try{
				while((read = bf.readLine()) != null){
					myout(read);
					result.append(read + "\n");
				}
			}catch(IOException e){}
			if(p.exitValue() == 0){
				StringBuilder ans = new StringBuilder("");
				try{
					BufferedReader ansBf = new BufferedReader(new FileReader(new File("./" + OUTPUT_DIR_STRING + "/" + inputFiles[i])));
					while((read = ansBf.readLine()) != null){
						ans.append(read + "\n");
					}
				}catch(IOException e){}
				if(ans.toString().equals(result.toString())){
					myout("[AC] " + count);
				}else{
					myout("[WA] " + count);
					myout(ans.toString());
				}
			}
			myout("-------------------------");
		}
	}
	static void myout(Object t){
		System.out.println(t);
	}
}