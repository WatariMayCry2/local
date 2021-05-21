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
function sieveOfEratos(val){
	var primes = new Set();
	var nums = new Set();
	var used = [2,3,5,7,11];//already primes
	var underPrime = 13;
	if(val <= 1){
		return nums;
	}
	for(var i = 0; i < used.length; i++){if(used[i] <= val){nums.add(used[i]);}}
	for(var i = underPrime; i <= val; i = i + 2){
		var continued = false;
		for(var j = 0; j < used.length; j++){
			if(i % used[j] == 0){continued = true; break;}
		}
		if(continued){continue;}
		nums.add(i);
	}
	for(var i = 2; i <= Math.sqrt(val); (i == 2) ? i++ : i = i + 2){
		if(!nums.has(i)){continue;}
		var count = 1;
		while(i * count <= val){
			if(i <= 11 && used.indexOf(i) != -1){break;}
			if(count == 1){primes.add(i);}
			nums.delete(i * count);
		count++;
		}
	}
	var primeItr = Array.from(primes);
	for(var i = 0; i < primeItr.length; i++){
		nums.add(primeItr[i]);
	}
	return nums;
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
function binarySearch(list,search){
	var ng = -1;
	var ok = list.length;
	var center;
	while(Math.abs(ok - ng) > 1){
		center = Math.floor((ok + ng) / 2);
		if(list[center] >= search){
			ok = center;
		}else{
			ng = center;
		}
	}
	return list[ok];
	//return list[ng];
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
