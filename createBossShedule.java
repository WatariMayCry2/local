import java.util.*; import java.io.*; import java.math.*; import java.text.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.DateTimeException;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.*;
public class Main{
	//見なくていいよ　ここから------------------------------------------
	static class InputIterator {
		BufferedReader br;
		ArrayDeque<String> buffer = new ArrayDeque<>();
		boolean isInteractive;

		InputIterator(boolean interactive) {
			isInteractive = interactive;
			try {
				br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
				if (!interactive) {
					String line;
					while ((line = br.readLine()) != null) {
						Collections.addAll(buffer, line.split(" "));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		boolean hasNext() {
			if (isInteractive) {
				if (!buffer.isEmpty()) return true;
				try {
					String line = br.readLine();
					if (line == null) return false;
					Collections.addAll(buffer, line.split(" "));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return !buffer.isEmpty();
			}
			return !buffer.isEmpty();
		}

		String next() {
			if (!hasNext()) throw new NoSuchElementException();
			return buffer.poll();
		}
	}

	static HashMap<Integer, String> CONVSTR = new HashMap<>();
	static InputIterator ii;
	static PrintWriter out = new PrintWriter(System.out);
	static void flush(){out.flush();}
	static void myout(Object t){out.println(t);}
	static void myerr(Object... t){System.err.print("debug:");System.err.println(Arrays.deepToString(t));}
	static String next(){return ii.next();}
	static boolean hasNext(){return ii.hasNext();}
	static int nextInt(){return Integer.parseInt(next());}
	static long nextLong(){return Long.parseLong(next());}
	static double nextDouble(){return Double.parseDouble(next());}
	static ArrayList<String> nextCharArray(){return myconv(next(), 0);}
	static ArrayList<String> nextStrArray(int size){
		ArrayList<String> ret = new ArrayList<>(size);
		for(int i = 0; i < size; i++){
			ret.add(next());
		}
		return ret;
	}
	static ArrayList<Integer> nextIntArray(int size){
		ArrayList<Integer> ret = new ArrayList<>(size);
		for(int i = 0; i < size; i++){
			ret.add(Integer.parseInt(next()));
		}
		return ret;
	}
	static ArrayList<Long> nextLongArray(int size){
		ArrayList<Long> ret = new ArrayList<>(size);
		for(int i = 0; i < size; i++){
			ret.add(Long.parseLong(next()));
		}
		return ret;
	}
	@SuppressWarnings("unchecked")
	static String myconv(Object list, int no){//only join
		StringBuilder sb = new StringBuilder("");
		String joinString = CONVSTR.get(no);
		if(list instanceof String[]){
			return String.join(joinString, (String[])list);
		}else if(list instanceof long[]){
			long[] tmp = (long[])list;
			if(tmp.length == 0){
				return "";
			}
			sb.append(String.valueOf(tmp[0]));
			for(int i = 1; i < tmp.length; i++){
				sb.append(joinString).append(String.valueOf(tmp[i]));
			}
			return sb.toString();
		}else if(list instanceof int[]){
			int[] tmp = (int[])list;
			if(tmp.length == 0){
				return "";
			}
			sb.append(String.valueOf(tmp[0]));
			for(int i = 1; i < tmp.length; i++){
				sb.append(joinString).append(String.valueOf(tmp[i]));
			}
			return sb.toString();
		}else if(list instanceof ArrayList){
			ArrayList tmp = (ArrayList)list;
			if(tmp.size() == 0){
				return "";
			}
			sb.append(tmp.get(0));
			for(int i = 1; i < tmp.size(); i++){
				sb.append(joinString).append(tmp.get(i));
			}
			return sb.toString();
		}else{
			throw new ClassCastException("Don't join");
		}
	}
	static ArrayList<String> myconv(String str, int no){//only split
		String splitString = CONVSTR.get(no);
		return new ArrayList<String>(Arrays.asList(str.split(splitString)));
	}
	static ArrayList<String> myconv(String str, String no){
		return new ArrayList<String>(Arrays.asList(str.split(no)));
	}
	public static void main(String[] args){
		ii = new InputIterator(true);
		CONVSTR.put(8, " "); CONVSTR.put(9, "\n"); CONVSTR.put(0, "");
		solve();flush();
	}
	//見なくていいよ　ここまで------------------------------------------
	//このコードをコンパイルするときは、「-encoding UTF-8」を指定すること
	static void solve(){//ここがメイン関数
        String line = next();
        if (line == null) {
            System.err.println("MM/DD を1行で入力してください（例: 3/1 や 03/01）");
            return;
        }
		// 全角→半角、余分な空白除去、全角スラッシュ許容
        line = Normalizer.normalize(line, Normalizer.Form.NFKC).trim();
        line = line.replace('／', '/');

        // 年は4桁、月日1～2桁を許容
        Pattern p = Pattern.compile("\\s*(\\d{4})\\s*/\\s*(\\d{1,2})\\s*/\\s*(\\d{1,2})\\s*");
        Matcher m = p.matcher(line);
        if (!m.matches()) {
            System.err.println("入力形式が不正です。YYYY/MM/DD で入力してください（例: 2025/9/8）");
            return;
        }

        int year  = Integer.parseInt(m.group(1));
        int month = Integer.parseInt(m.group(2));
        int day   = Integer.parseInt(m.group(3));

        LocalDate startDate;
        try {
            startDate = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            System.err.println("不正な日付です: " + e.getMessage());
            return;
        }

        // 0～7日分出力。最終日(+7)のみ2行目を出力しない
        for (int i = 0; i < 7; i++) {
            LocalDate d = startDate.plusDays(i);
            String mmdd = d.format(OUT_FMT); // 出力は西暦不要
            String youbi = JP_DOW[d.getDayOfWeek().getValue() - 1];

            System.out.println(mmdd + "(" + youbi + ") 22:30集合、23:00出発");
            if (i != 6) {
                System.out.println(mmdd + "(" + youbi + ") 23:00集合、23:15出発");
            }
        }
	}
	//メソッド追加エリア　ここから

private static final String[] JP_DOW = {"月", "火", "水", "木", "金", "土", "日"};
    private static final DateTimeFormatter OUT_FMT = DateTimeFormatter.ofPattern("MM/dd");

	//メソッド追加エリア　ここまで
}
