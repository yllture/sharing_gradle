var data;
$.ajax({
		type :'post',
		url:'smartFolderList',
//		data :JSON.stringify(obj),
		dataType:'text',
		async:false,
		contentType: "application/json; charset=utf-8",
		success:function(res){
			var obj=JSON.parse(res);
			data=obj;
	}	
});

//$.ajax({
//	type :'post',
//	url:'smartBankList',
////		data :JSON.stringify(obj),
//	dataType:'text',
//	async:false,
//	contentType: "application/json; charset=utf-8",
//	success:function(msg){
//		//alert(1);
//		alert(msg);
//		var bankl=JSON.parse(msg);
//		var banklist=bankl.bklist;
//		alert(banklist.length);
//		var length=banklist.length;
//		for(var i=0;i<length;i++){
//			alert($("div").attr("id",banklist[i].folderid));
//		}
//	}
//})




//var data = {
//	files: [
//    {
//    	id: 0,
//    	pid: -1,
//    	title: '全部文件'
//    },
//
//    {
//    	id: 1,
//    	pid: 0,
//    	title: '我的收藏'
//    },
//    {
//    	id: 2,
//    	pid: 0,
//    	title: '我的音乐'
//    },
//    {
//    	id: 3,
//    	pid: 0,
//    	title: '我的电影'
//    },
//    {
//    	id: 4,
//    	pid: 0,
//    	title: '我的书籍'
//    },
//
//    {
//    	id: 11,
//    	pid: 1,
//    	title: '工具'
//    },
//    {
//    	id: 12,
//    	pid: 1,
//    	title: '画册'
//    },
//    {
//    	id: 13,
//    	pid: 1,
//    	title: '班级图片'
//    },
//
//    {
//    	id: 41,
//    	pid: 4,
//    	title: 'JavaScript 高级程序设计'
//    },
//    {
//    	id: 42,
//    	pid: 4,
//    	title: '锋利的jQuery'
//    },
//    {
//    	id: 43,
//    	pid: 4,
//    	title: 'JavaScript语言精粹'
//    }
//	]
//};