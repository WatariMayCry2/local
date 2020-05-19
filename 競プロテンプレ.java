import java.util.*;
import java.io.*;
public class Main {
  static public class InputIterator{
    ArrayList<String> inputLine = new ArrayList<String>(1024);
    int index = 0;
    int max;
    InputIterator(){
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      while(true){
        String read;
        try{
          read = br.readLine();
        }catch(IOException e){
          read = null;
        }
        if(read != null){
          inputLine.add(read);
        }else{
          break;
        }
      }
      max = inputLine.size();
    }
    public boolean hasNext(){return (index < max);}
    public String next(){
      if(hasNext()){
        String returnStr = inputLine.get(index);
        index++;
        return returnStr;
      }else{
        throw new IndexOutOfBoundsException("これ以上入力はないよ。");
      }
    }
  }
  
  static InputIterator ii = new InputIterator(); 
  static void myout(Object t){System.out.println(t);}
  static void myerr(Object t){System.err.println(t);}
  static String next(){return ii.next();}
  static int nextInt(){return Integer.parseInt(next());}
  static long nextLong(){return Long.parseLong(next());}
  static String[] nextStrArray(){return myHanSpSplit(next());}
  static String[] nextCharArray(){return mySingleSplit(next());}
  static String[] mySingleSplit(String str){return str.split("");}
  static String[] myHanSpSplit(String str){return str.split(" ");}
  static ArrayList<Integer> nextIntArray(){
    ArrayList<Integer> ret = new ArrayList<Integer>();
    String[] input = nextStrArray();
    for(int i = 0; i < input.length; i++){
      ret.add(Integer.parseInt(input[i]));
    }
    return ret;
  }
  static ArrayList<Long> nextLongArray(){
    ArrayList<Long> ret = new ArrayList<Long>();
    String[] input = nextStrArray();
    for(int i = 0; i < input.length; i++){
      ret.add(Long.parseLong(input[i]));
    }
    return ret;
  }
  
  static String kaigyoToStr(String[] list){return String.join("\n",list);}
  static String kaigyoToStr(ArrayList<String> list){return String.join("\n",list);}
  static String hanSpToStr(String[] list){return String.join(" ",list);}
  static String hanSpToStr(ArrayList<String> list){return String.join(" ",list);}
  public static void main(String[] args){
    
  }
  //Method addition frame start

  //Method addition frame end
}
