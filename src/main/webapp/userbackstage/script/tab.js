// JavaScript Document

function g(o){
	return document.getElementById(o);
}

function HoverLi(m,n,counter){
	
	for(var i=1;i<=counter;i++){
		g('tb_'+m+i).className='';
		g('tbc_'+m+i).className='undis';
	}
	g('tbc_'+m+n).className='dis';
	g('tb_'+m+n).className='active';
}
