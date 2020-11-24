//Don't have to see. start------------------------------------------
var read = require('readline').createInterface({
	input: process.stdin, output: process.stdout
});
var obj; var inLine = [];
read.on('line', function(input){
	var tmp = input.split(' ');
	for(var i = 0; i < tmp.length; i++){
		inLine.push(tmp[i]);
	}
});
read.on('close', function(){
	obj = init(inLine);
	console.error('\n↑入力 ↓出力');
	Main();
});
function makeClone(obj){return (obj instanceof Set) ? new Set(Array.from(obj)) : JSON.parse(JSON.stringify(obj));}
function nextArray(size, code){
	var ret = new Array(size);
	for(var i = 0; i < size; i++){
		if(code == 'int'){
			ret[i] = nextInt();
		}else{
			ret[i] = next();
		}
	}
	return ret;
}
function nextIntArray(size){return nextArray(size, 'int');} function nextStrArray(size){return nextArray(size, 'str');}
function nextCharArray(){return myconv(next(),6);}
function next(){return obj.next();} function hasNext(){return obj.hasNext();} function nextInt(){return myconv(next(),1);}
function init(input){  
	return {
		list : input, index : 0, max : input.length,
		hasNext : function(){return (this.index < this.max);},
		next : function(){if(this.hasNext()){return this.list[this.index++];}else{throw 'ArrayIndexOutOfBoundsException ‚There is no more input';}}
	};
}
function myout(s){console.log(s);}
function myerr(s){console.error('debug:' + require('util').inspect(s,false,null));}
//param "no" is
//unknown or outlier : return i. 1: parseInt.
//2: split space. 4: split space and parseInt.
//6: split 1 character. 7: split 1 character and parseInt.
//8: join space. 9: join nextline. 0: join no character.
function myconv(i,no){try{switch(no){case 1:return parseInt(i);case 2:return i.split(' ');case 4:return i.split(' ').map(Number);case 6:return i.split('');case 7:return i.split('').map(Number);case 8:return i.join(' ');case 9:return i.join('\n');case 0:return i.join('');default:return i;}}catch(e){return i;}}

//Don't have to see. end------------------------------------------
function Main(){
	
}
