//最小公倍数。最大公約数がいる
//最大公約数
static long lcm(long m, long n) {return Math.max(m,n) / gcd(m, n) * Math.min(m,n);}
static long gcd(long m, long n) {return n != 0 ? gcd(n, m % n) : m;}

//約数列挙
static ArrayList<Long> getDivisorList(long val){
	ArrayList<Long> list = new ArrayList<Long>(1024);
	for(long i = 1; i * i <= val; i++){
		if(val % i == 0){
			list.add(i);
			if(i * i != val){
				list.add(val / i);
			}
		}
	}
	Collections.sort(list);
	return list;
}

//素数判定
static boolean isPrime(long val){
    if(val <= 1 || (val != 2 && val % 2 == 0)){
        return false;
    }else if(val == 2){
        return true;
    }
    double root = Math.floor(Math.sqrt(val));
    for(long i = 3; i <= root; i += 2){
        if(val % i == 0){
            return false;
        }
    }
    return true;
}

//エラトステネスの篩
static HashSet<Integer> sieveOfEratos(int val){
    HashSet<Integer> primes = new HashSet<>();
    HashSet<Integer> nums = new HashSet<>();
    int[] used = {2, 3, 5, 7, 11};
    int underPrime = 13;
    if(val <= 1){
        return nums;
    }
    for(int i = 0; i < used.length; i++){
        if(used[i] <= val){
            nums.add(used[i]);
        }
    }
    for(int i = underPrime; i <= val; i += 2){
        boolean continued = false;
        for(int j = 0; j < used.length; j++){
            if(i % used[j] == 0){
                continued = true;
                break;
            }
        }
        if(continued){
            continue;
        }
        nums.add(i);
    }
    int i = 2;
    while(i <= Math.sqrt(val)){
        if(!nums.contains(i)){
            if(i == 2){
                i++;
            }else{
                i += 2;
            }
            continue;
        }
        int count = 1;
        while(i * count <= val){
            if(i <= 11 && Arrays.asList(used).contains(i)){
                break;
            }
            if(count == 1){
                primes.add(i);
            }
            nums.remove(i * count);
            count++;
        }
        if(i == 2){
            i++;
        }else{
            i += 2;
        }
    }
    Iterator<Integer> it = primes.iterator();
    while(it.hasNext()){
        nums.add(it.next());
    }
    return nums;
}

//繰り返し二乗法
static long mypow(long x, long n) {return mypow(x, n, -1);}
static long mypow(long x, long n, long m) {
    long ans = 1;
    while (n > 0) {
        if ((n & 1) == 1){
            ans *= x;
            if(m != -1){
                ans %= m;
            }
        }
	if(m != -1){
            x %= m;
        }
        x *= x;
        if(m != -1){
            x %= m;
        }
        n >>= 1;
    }
    return ans;
}

//組み合わせ
static class Combination{
    long[] kaijo;
    long[] inverse;
    long mod;
    int size;
    Combination(int N, long mod){
        this.mod = mod;
        this.size = N;
        kaijo = new long[N + 1];
        inverse = new long[N + 1];
        kaijo[0] = 1;
        inverse[0] = 1;
        for(int i = 1; i <= N; i++){
            kaijo[i] = kaijo[i - 1] * (long)i % mod;
            inverse[i] = inverse[i - 1] * mypow((long)i, mod - 2, mod) % mod;
        }
    }
    public long nCk(int n, int k){
        if(k < 0 || k > n){
            return 0;
        }
        return ((kaijo[n] * inverse[k]) % mod * inverse[n - k]) % mod;
    }
    public long nPk(int n, int k){
        if(k < 0 || k > n){
            return 0;
        }
        return (kaijo[n] * inverse[n - k]) % mod;
    }
    public long nHk(int n, int k){
        if(n + k - 1 < 0 || k < 0 || n < 0 || k > size || n > size || (n + k - 1) > size){
            return 0;
        }
        return nCk(n + k - 1, k);
    }
}

//素因数分解
static class PrimeFactorization{
    HashSet<Long> primes;
    HashMap<Long, Integer> map = new HashMap<>();
    PrimeFactorization(long val){
        if(isPrime(val)){
            map.put(val, 1);
            primes = new HashSet<>(map.keySet());
            return;
        }
        long div = 2;
        while(val != 1){
            if(val % div == 0){
                int count = 2;
                while(val % (long)Math.pow(div, count) == 0){
                    count++;
                }
                if(map.containsKey(div)){
                    map.put(div, map.get(div) + (count - 1));
                }else{
                    map.put(div, count - 1);
                }
                val /= (long)Math.pow(div, count - 1);
                if(isPrime(val)){
                    if(map.containsKey(val)){
                        map.put(val, map.get(val) + 1);
                    }else{
                        map.put(val, 1);
                    }
                    break;
                }
            }else{
                div = (div == 2) ? div + 1 : div + 2;
            }
        }
        primes = new HashSet<>(map.keySet());
    }
    public int get(long K){
        return map.get(K);
    }
    public HashSet<Long> getKeys(){
        return primes;
    }
    public boolean containsKey(long K){
        return map.containsKey(K);
    }
}

//辞書順で次に来る文字列（順列全列挙用）
static String nextPermutation(String s){
    ArrayList<Character> list = new ArrayList<>();
    for (int i = 0; i < s.length(); i++) list.add(s.charAt(i));
    int pivotPos = -1;
    char pivot = 0;
    for (int i = list.size() - 2; i >= 0; i--) {
        if (list.get(i) < list.get(i + 1)) {
            pivotPos = i;
            pivot = list.get(i);
            break;
        }
    }
    //s is order by asc
    if (pivotPos == -1 && pivot == 0) return "Final";
    int L = pivotPos + 1;
    int R = list.size() - 1;
    int minPos = -1;
    char min = Character.MAX_VALUE;
    for (int i = R; i >= L; i--) {
        if (pivot < list.get(i) && list.get(i) < min) {
            min = list.get(i);
            minPos = i;
        }
    }
    Collections.swap(list, pivotPos, minPos);
    Collections.sort(list.subList(L, R+1));
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < list.size(); i++) sb.append(list.get(i));
    return sb.toString();
}

//UnionFind
static class UnionFind{
    int[] list;
    int group;
    UnionFind(int size){
        list = new int[size];
        Arrays.fill(list, -1);
        group = size;
    }
    boolean isSame(int L, int R){
        return getRootIndex(L) == getRootIndex(R);
    }
    int getRootIndex(int index){
        if(list[index] < 0){
            return index;
        }else{
            list[index] = getRootIndex(list[index]);
            return list[index];
        }
    }
    void doUnion(int L, int R){
        int Lroot = getRootIndex(L);
        int Rroot = getRootIndex(R);
        if(Lroot != Rroot){
            group--;
            if(getSize(Lroot) >= getSize(Rroot)){
                int tmp = Lroot;
                Lroot = Rroot;
                Rroot = tmp;
            }
            list[Lroot] += list[Rroot];
            list[Rroot] = Lroot;
        }
    }
    int getSize(int index){
        return -list[getRootIndex(index)];
    }
    int getGroupCount(){
        return group;
    }
    boolean isRoot(int index){
        return getRootIndex(index) == index;
    }
}

//二分探索（一般）
static int binarySearch(ArrayList<Integer> list,int search){
    int ng = -1;
    int ok = list.size();
    while(Math.abs(ok - ng) > 1){
        int center = (ok + ng) / 2;
        if(list.get(center) >= search){
            ok = center;
        }else{
            ng = center;
        }
    }
    return list.get(ok);
    //return list.get(ng);
}

//BinaryIndextree(FenwickTree)
static class FenwickTree{
    private int _n;
    private long[] data;

    public FenwickTree(int n){
        this._n = n;
        data = new long[n];
    }

    public FenwickTree(long[] data) {
        this(data.length);
    	build(data);
    }

    public void set(int p, long x){
        add(p, x - get(p));
    }

    public void add(int p, long x){
        assert(0<=p && p<_n);
        p++;
        while(p<=_n){
            data[p-1] += x;
            p += p&-p;
        }
    }
    public long sum(int l, int r){
        assert(0<=l && l<=r && r<=_n);
        return sum(r)-sum(l);
    }

    public long get(int p){
        return sum(p, p+1);
    }

    private long sum(int r){
        long s = 0;
        while(r>0){
            s += data[r-1];
            r -= r&-r;
        }
        return s;
    }

    private void build(long[] dat) {
        System.arraycopy(dat, 0, data, 0, _n);
        for (int i=1; i<=_n; i++) {
            int p = i+(i&-i);
            if(p<=_n){
                data[p-1] += data[i-1];
            }
        }
    }
}

//最小共通祖先（LCA）
static class LCA{
    int[][] parent;
    int[] depth;
    Node[] graph;
    int size;
    int bit = 1;
    LCA(Node[] graph, int root){
        this.graph = graph;
        size = graph.length;
        while((1 << bit) < size){
            bit++;
        }
        parent = new int[bit][size];
        depth = new int[size];
        dfs(root, -1, 0);
        for(int i = 0; i < bit - 1; i++){
            for(int j = 0; j < size; j++){
                if(parent[i][j] < 0){
                    parent[i + 1][j] = -1;
                }else{
                    parent[i + 1][j] = parent[i][parent[i][j]];
                }
            }
        }
    }
    public void dfs(int now, int p, int nowDepth){
        parent[0][now] = p;
        depth[now] = nowDepth;
        ArrayList<Integer> child = graph[now].child;
        for(int i = 0; i < child.size(); i++){
            if(child.get(i) != p){
                dfs(child.get(i), now, nowDepth + 1);
            }
        }
    }
    public int getLCA(int L, int R){
        if(depth[L] > depth[R]){
            int tmp = L;
            L = R;
            R = tmp;
        }
        for(int i = 0; i < bit; i++){
            if(((depth[R] - depth[L]) >> i & 1) == 1){
                R = parent[i][R];
            }
        }
        if(L == R){
            return L;
        }
        for(int i = bit - 1; i >= 0; i--){
            if(parent[i][L] != parent[i][R]){
                L = parent[i][L];
                R = parent[i][R];
            }
        }
        return parent[0][L];
    }
}

//拡張ユークリッド互除法
static class ExtendGcd{
    long X = 0;
    long Y = 0;
    ExtendGcd(){}
    public long extgcd(long a, long b){
        if(b == 0){
            X = 1;
            Y = 0;
            return a;
        }else{
            long G = extgcd(b, a % b);
            long tmpX = X;
            long tmpY = Y;
            X = tmpY;
            Y = tmpX - ((a / b) * tmpY);
            return G;
        }
    }
}
