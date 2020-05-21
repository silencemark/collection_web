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

function DownHoverLi(m,n,counter){
	
	for(var i=1;i<=counter;i++){
		g('dname_'+m+i).className='t_name dis';
		g('dbox_'+m+i).className='xx_box undis';
	}
	g('dbox_'+m+n).className='xx_box dis';
	g('dname_'+m+n).className='t_name undis';
}
