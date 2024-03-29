//最小公倍数。最大公約数もいる
//最大公約数。ユークリッドの互除法
function lcm(m, n) {return (m / gcd(m, n)) * n;}
function gcd(m, n) {return n != 0 ? gcd(n, m % n) : m;}

//約数列挙
function getDivisorList(val){
	var list = [];
	for(var i = 1; i * i <= val; i++){
		if(val % i == 0){
			list.push(i);
			if(i * i != val){
				list.push(val / i);
			}
		}
	}
	list.sort(function(a,b){ return a - b; });
	return list;
}

//素数判定
function isPrime(val){
	if(val == null || val <= 1 || (val != 2 && val % 2 == 0)){
		return false;
	}else if(val == 2){
		return true;
	}
	var root = Math.floor(Math.sqrt(val));
	for(var i = 3; i <= root; i += 2){
		if(val % i == 0){
			return false;
		}
	}
	return true;
}

//エラトステネスの篩
//下記をもとにjavascriptへ置き換え + 集合化
//https://github.com/TheAlgorithms/Java/blob/master/src/main/java/com/thealgorithms/others/SieveOfEratosthenes.java
function sieveOfEratos(n) {
	if(n <= 0){
		throw 'IllegalArgumentException ,n must be positive.';
	}
	var Type = {
		Prime : 1,
		NOT_PRIME : 0
	};
    var isPrimeArray = new Array(n + 1);
    isPrimeArray.fill(Type.PRIME);
    isPrimeArray[0] = isPrimeArray[1] = Type.NOT_PRIME;
    var cap = Math.sqrt(n);
    var count = 0;
	for (var i = 2; i <= cap; i++) {
		if (isPrimeArray[i] == Type.PRIME) {
			count++;
			for (var j = 2; i * j <= n; j++) {
				isPrimeArray[i * j] = Type.NOT_PRIME;
			}
		}
	}
	var primes = new Array(count);
	var primeIndex = 0;
	for(var i = 0; i < isPrimeArray.length; i++){
		if(isPrimeArray[i] == Type.PRIME){
			primes[primeIndex++] = i;
		}
	}
	return new Set(primes);
}

//繰り返し二乗法
function mypow(x, n, m) {
	x = BigInt(x);
	n = BigInt(n);
	m = BigInt(m);
	var ans = 1n;
	while (n > 0n) {
		if ((n & 1n) == 1n){
			ans = ans * x;
			if(m != -1n){
				ans %= m;
			}
		}
		x = x * x;
		if(m != -1n){
			x %= m;
		}
		n >>= 1n;
	}
	return ans;
}

//組み合わせ。繰り返し二乗法がいる
function Combination(N, mod){
	var kaijo = new Array(N + 1);
	var inverse = new Array(N + 1);
	mod = BigInt(mod);
	kaijo[0] = 1n;
	inverse[0] = 1n;
	for(var i = 1; i <= N; i++){
		kaijo[i] = (kaijo[i - 1] * BigInt(i)) % mod;
		inverse[i] = (inverse[i - 1] * mypow(i, mod - 2n, mod)) % mod;
	}
	var obj = {
		kaijo : kaijo,
		inverse : inverse,
		mod : mod,
		size : N,
		nCk : function(n, k){
			if(k < 0 || k > n){
				return 0n;
			}
			return ((this.kaijo[n] * this.inverse[k]) % this.mod) * this.inverse[n - k] % this.mod;
		},
		nPk : function(n, k){
			if(k < 0 || k > n){
				return 0n;
			}
			return (this.kaijo[n] * this.inverse[n - k]) % this.mod;
		},
		nHk : function(n, k){
			if(n + k - 1 < 0 || k < 0 || n < 0 || k > this.size || n > this.size || (n + k - 1) > this.size){
				return 0n;
			}
			return this.nCk(n + k - 1, k);
		}
	}
	return obj;
}

//素因数分解。素数判定がいる
function getPrimeFactorization(val){
	var primeMap = {};
	var div = 2;
	if(isPrime(val)){
		primeMap[val] = 1;
		return primeMap;
	}
	while(val != 1){
		if(val % div == 0){
			var count = 2;
			while(val % Math.pow(div, count) == 0){
				count++;
			}
			(primeMap[div] == null)  ? primeMap[div] = (count - 1) : primeMap[div] += (count - 1);
			val /= Math.pow(div, count - 1);
			if(isPrime(val)){
				(primeMap[val] == null)  ? primeMap[val] = 1 : primeMap[val]++;
				break;
			}
		}
		div = (div == 2) ? div + 1 : div + 2;
	}
	return primeMap;
}

//二分探索（一般）
//両端に番兵（極端に低い/高い値）を置くことを推奨する
function binarySearch(list, search, method){
	var ng = 0;
	var ok = list.length - 1;
	var center;
	while(Math.abs(ok - ng) > 1){
		center = Math.floor((ok + ng) / 2);
		switch(method){
			case "floor":
				if(list[center] > search){
					ok = center;
				}else{
					ng = center;
				}
				break;
			case "ceiling":
				if(list[center] < search){
					ng = center;
				}else{
					ok = center;
				}
				break;
			case "lower":
				if(list[center] >= search){
					ok = center;
				}else{
					ng = center;
				}
				break;
			case "higher":
				if(list[center] <= search){
					ng = center;
				}else{
					ok = center;
				}
				break;
		}
	}
	if(method == "floor" || method == "lower"){
		return list[ng];
	}else{
		return list[ok];
	}
}

//UnionFind
function UnionFind(n){
	var uf = {
		//全てのインデックスは「-X(親、絶対値はグループの大きさ)」「自分が属する親(≒根)」のいずれかを持つ。
		//最初はみんなグループの大きさ1の親
		'list' : new Array(n).fill(-1),
		'reset' : new Array(n),
		'group' : n,
		//同じ親を持つか
		'isSame' : function(L, R){
			return this.getRootIndex(L) == this.getRootIndex(R);
		},
		//自身の親インデックスをたどって根っこに着く
		//親にたどり終えたら子に帰っていくついでに、子たちに「一番上の親は誰か」を知らせる
		'getRootIndex' : function(index){
			var count = 0;
			while(this.list[index] >= 0){
				this.reset[count] = index;
				index = this.list[index];
				count++;
			}
			for(var i = 0; i < count; i++){
				this.list[this.reset[i]] = index;
			}
			return index;
		},
		//異なる親同士のまとまりを一つにする（同じ親ならスルー）
		//小さいグループの親が大きいグループの親の直下に着く
		//グループの大きさも更新する
		'doUnion' : function(L, R){
			var Lroot = this.getRootIndex(L);
			var Rroot = this.getRootIndex(R);
			if(Lroot != Rroot){
				this.group--;
				if(this.getSize(Lroot) >= this.getSize(Rroot)){
					var tmp = Lroot;
					Lroot = Rroot;
					Rroot = tmp;
				}
				this.list[Lroot] += this.list[Rroot];
				this.list[Rroot] = Lroot;
			}
		},
		//「-X(親、絶対値はグループの大きさ)」
		//なので、インデックスを指定→親を知る→親の持つグループの大きさがわかる。
		//ただしマイナス値なので、掛け算して返す。
		'getSize' : function(index){
			return -this.list[this.getRootIndex(index)];
		},
		'getGroupCount' : function(){
			return this.group;
		}
	}
	return uf;
}

//z-algorithm
function zAlgorithm(s){
	var z = new Array(s.length).fill(0);
	for(var i = 1, j = 0; i < s.length; i++){
		if(i + z[i - j] < j + z[j]){
			z[i] = z[i - j];
		}else{
			var k = Math.max(0, j + z[j] - i);
			while(i + k < s.length && s[k] == s[i + k]){
				k++;
			}
			z[i] = k;
			j = i;
		}
	}
	z[0] = s.length;
	return z;
}

//両端キュー
function ArrayDeque(add){
	var queue = {
		L : 0,//next add Area
		R : 0,//next add Area
		map : {},
		unshift : function(V){ return this.addFirst(V); },
		addFirst : function(V){
			this.map[this.L] = V;
			if(this.L == this.R){
				this.R++;
			}
			this.L--;
		},
		shift : function(){ return this.pollFirst(); },
		pollFirst : function(){
			if(this.L == this.R){
				return null;
			}
			var ret = this.map[this.L + 1];
			this.L++;
			if(Math.abs(this.L - this.R) == 1){
				this.R--;
			}
			return ret;
		},
		peekFirst : function(){
			if(this.L == this.R){
				return null;
			}
			return this.map[this.L + 1];
		},
		add : function(V){ return this.addLast(V); },
		push : function(V){ return this.addLast(V); },
		addLast : function(V){
			this.map[this.R] = V;
			if(this.L == this.R){
				this.L--;
			}
			this.R++;
		},
		pop : function(){ return this.pollLast(); },
		pollLast : function(){
			if(this.L == this.R){
				return null;
			}
			var ret = this.map[this.R - 1];
			this.R--;
			if(Math.abs(this.L - this.R) == 1){
				this.L++;
			}
			return ret;
		},
		peekLast : function(){
			if(this.L == this.R){
				return null;
			}
			return this.map[this.R - 1];
		},
		size : function(){
			if(this.L == this.R){
				return 0;
			}else{
				return Math.abs(this.L - this.R) - 1;
			}
		},
		toString : function(){
			var list = [];
			for(var i = this.L + 1; i < this.R; i++){
				list.push(this.map[i]);
			}
			return "[" + list.join(", ") + "]";
		},
		get : function(index){
			if(this.L + 1 + index >= this.R || index < 0){
				return null;
			}else{
				return this.map[this.L + 1 + index];
			}
		},
		set : function(index, V){
			if(this.L + 1 + index >= this.R || index < 0){
				throw 'ArrayIndexOutOfBoundsException can\'t set';
			}else{
				this.map[this.L + 1 + index] = V;
			}
		},
		isEmpty : function(){
			return this.size() == 0;
		},
		clear : function(){
			this.L = 0;
			this.R = 0;
			this.map = {}
		}
	}
	if(add){
		for(var i = 0; i < add.length; i++){
			queue.addLast(add[i]);
		}
	}
	return queue;
}

//BigIntに対応した平方根
function mysqrt(value) {
    if (value < 0n) {
        throw 'square root of negative numbers is not supported'
    }

    if (value < 2n) {
        return value;
    }

    function newtonIteration(n, x0) {
        const x1 = ((n / x0) + x0) >> 1n;
        if (x0 === x1 || x0 === (x1 - 1n)) {
            return x0;
        }
        return newtonIteration(n, x1);
    }

    return newtonIteration(value, 1n);
}

//優先度付きキュー。Comparator定義可能
class PriorityQueue {
	constructor(f) {
		this.heap = [];
		this.func = f;
	}

	add(v) {
		var heap = this.heap;
		var i = heap.length++;
		var j;
		while(i) {
			j = (i - 1) >> 1;
			if(this.func(heap[j], v) <= 0){
				break;
			}
			heap[i] = heap[j];
			i = j;
		}
		heap[i] = v;
	}

	poll() {
		var heap = this.heap;
		var top = heap[0];
		var popped = heap.pop();
		var i = 0
		var j
		var k = heap.length >> 1;
		while(i < k) {
			j = (i * 2) + 1;
			if(this.func(heap[j + 1], heap[j]) < 0){
				j++;
			}
			if(this.func(popped, heap[j]) <= 0){
				break
			}
			heap[i] = heap[j];
			i = j;
		}

		if(heap.length){
			heap[i] = popped;
		} 
		return top;
	}

	size() {
		return this.heap.length;
	}

	peek() {
		return this.heap[0];
	}
}

//辞書順で次に来る文字列（順列全列挙用）
function nextPermutation(sequence) {
  // 入力数列を配列に変換
  let arr = sequence.split('');

  // 配列の後ろから見て、昇順に並んでいない一番小さいインデックスを探す
  let i = arr.length - 2;
  while (i >= 0 && arr[i].charCodeAt() >= arr[i + 1].charCodeAt()) {
    i--;
  }

  // 入力数列がすでに辞書順最大の場合、"Final"を返す
  if (i === -1) {
    return 'Final';
  }

  // arr[i] よりも大きい数の中で最小の数を見つける
  let j = arr.length - 1;
  while (arr[j].charCodeAt() <= arr[i].charCodeAt()) {
    j--;
  }

  // arr[i] と arr[j] を交換する
  [arr[i], arr[j]] = [arr[j], arr[i]];

  // arr[i+1] 以降の要素を反転させる
  let left = i + 1;
  let right = arr.length - 1;
  while (left < right) {
    [arr[left], arr[right]] = [arr[right], arr[left]];
    left++;
    right--;
  }

  // 次の数列を文字列として返す
  return arr.join('');
}
