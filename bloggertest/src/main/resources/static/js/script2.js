$(function() {
	//这里是关于folder操作的js

	//选中之后右下角打勾的框
	var selectcheck='<div class="check-circle"><i class="fa fa-check-square"></i></div>';
	var selectfolders = new Array();
	//动态绑定文件夹元素的打开和点击选择事件
	$(".folder-report").on("mouseenter", this, function() {
		if($(this).find('.folder__detail') == null) {
			$('.alert').html('文件夹里还没东西').addClass('alert-info').show().delay(1000).fadeOut();
		}
		$(this).find('.folder').addClass('open');
		$(this).find('.folder-item').addClass('preselected');
	});
	$(".folder-report").on("mouseleave", this, function() {
		$(this).find('.folder').removeClass('open');
		$(this).find('.folder-item').removeClass('preselected');
	});

	$('.folder-report').on('click', this, function() {
		if($(this).find('.folder-item').hasClass('selected')) {
			$(this).find('.folder-item').removeClass('selected');
			$(this).find('.folder-item').removeClass('preselected');
			$(this).find('.check-circle').remove();
			//从选择的文件当中删除
			selectfolders.splice($.inArray(this, selectfolders), 1);
		} else {
			$(this).find('.folder-item').addClass('selected');
			$(this).find('.folder-item').removeClass('preselected');
			$(this).find('.folder-item').append(selectcheck);
			//添加进入选择的文件
			selectfolders.push(this);
		}
	})
	//文件增删改查按钮操作
	//	$('#add-folder').click(function() {
	//		
	//	})

	$('#delete-over-folder').click(function() {
		if(selectfolders.length == 0) {
			$('.alert').html('没有选中的文件夹').addClass('alert-warning').show().delay(1000).fadeOut();
		} else {
			$('#delete-folderModal').modal('show');
		}
	})
	$('#editor-folder').click(function() {
		if(selectfolders.length == 0) {
			$('.alert').html('没有选中的文件夹').addClass('alert-warning').show().delay(1000).fadeOut();
		} else if(selectfolders.length > 1) {
			$('.alert').html('只能对单个文件夹进行操作').addClass('alert-warning').show().delay(1000).fadeOut();
		} else {
			var selected = $('.selected');
			$('#old-name').val(selected.find('.folder-name span').text());
			$('#editor-folderModal').modal('show');
		}
	})
	
	
	
	
	
	//删除文件夹
	$('#deletebankfolder').click(function(){
		var i=0;
		for(i=0;i<selectfolders.length;i++){
			var p1 = selectfolders[i].getAttribute('folderid');
//			alert(p1);
			var bf = {'folderid':p1};
			$.ajax({
			url:'deletegarbagebankfolder',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bf),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				$('.alert').html('删除成功！').addClass('alert-success').show().delay(1000).fadeOut();

			},
			error:function(){
				$('.alert').html('删除失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
			
		}
		window.location.href = "tobankTrashTeacher";
	
	
	})
	
	
	//全部清空
	
	$('#clear-trash').click(function(){
		
		
		$('#clear-trashModal').modal('show');
		var alreadyfolder=$('.folder-report');

	})
	//清空回收站
	$('#deleteall').click(function(){
		
		var alreadyfolder=$('.folder-report');
		var i=0;
		for(i=0;i<alreadyfolder.length;i++){
			var p1 = alreadyfolder[i].getAttribute('folderid');
//			alert(p1);
			var bf = {'folderid':p1};
			$.ajax({
			url:'deletegarbagebankfolder',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bf),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
//				$('.alert').html('删除成功！').addClass('alert-success').show().delay(1000).fadeOut();

			},
			error:function(){
				$('.alert').html('删除失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
			
		}
		
		var alreadybank = $('.custom-col');
		var j =0;
//		alert("5555");
		for(j=0;j<alreadybank.length;j++){
			var p1 = alreadybank[j].getAttribute('bankid');
			var bank = {'bankid':p1};
		
//		alert("666");
		$.ajax({
			url:'deletegarbagebank',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bank),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				
				
			},
			error:function(){
				$('.alert').html('删除失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
			
		}
		$('.alert').html('删除成功！').addClass('alert-success').show().delay(1000).fadeOut();
		window.location.href = "tobankTrashTeacher";
		
	})
	
	
		
	
	 
	//删除题库操作
	var currentbankid ;
	$('.delete-bank').click(function(){ 
//		alert("22222");
		currentbankid=$(this).closest('.custom-col').attr('bankid');
		$('#delete-bankModal').modal('show');
		
	})
	
	
	
	//删除题库
	$('#deletebank').click(function(){
		
		var bank = {'bankid':currentbankid};
		
		
		$.ajax({
			url:'deletegarbagebank',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bank),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				$('.alert').html('删除成功！').addClass('alert-success').show().delay(1000).fadeOut();
				
				window.location.href = "tobankTrashTeacher";
				
				
			},
			error:function(){
				$('.alert').html('删除失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
		
		
		
	})
	
	
	//还原文件夹操作
	$('#back-folder').click(function(){
		
		if(selectfolders.length == 0) {
			$('.alert').html('没有选中的文件夹').addClass('alert-warning').show().delay(1000).fadeOut();
		} else if(selectfolders.length > 1) {
			$('.alert').html('只能对单个文件夹进行操作').addClass('alert-warning').show().delay(1000).fadeOut();
		} else {
		
			var p1 = selectfolders[0].getAttribute('folderid');
//			alert(p1);
			var bf = {'folderid':p1};
			$.ajax({
			url:'getbackpath',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bf),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				if(e=="error"){
					$('.alert').html('原目录不存在，无法还原！').addClass('alert-warning').show().delay(1000).fadeOut();
					return;
				}
				$('#backpath').val(e);
			},
			error:function(){
				$('.alert').html('系统维护，请稍后操作！').addClass('alert-warning').show().delay(1000).fadeOut();
				return;
			}
			});
			$('#back-folderModal').modal('show');
	
		}
		
	})
	
	//还原文件夹
	$('#rebackfolder').click(function(){
		
			var p1 = selectfolders[0].getAttribute('folderid');
//			alert(p1);
			var bf = {'folderid':p1};
			$.ajax({
			url:'rebackfolder',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bf),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				$('.alert').html('还原成功！').addClass('alert-success').show().delay(1000).fadeOut();
				
				window.location.href = "tobankTrashTeacher";
			},
			error:function(){
				$('.alert').html('还原失败！').addClass('alert-warning').show().delay(1000).fadeOut();
				window.location.href = "tobankTrashTeacher";
				
			}
			});
		
		
	})
	
	
	//还原题库操作
	$('.back-bank').click(function(){
		
		
		currentbankid=$(this).closest('.custom-col').attr('bankid');
		var bank = {'bankid':currentbankid};
		
		
		$.ajax({
			url:'getbankbackpath',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bank),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				if(e=="error"){
					$('.alert').html('原目录不存在，无法还原！').addClass('alert-warning').show().delay(1000).fadeOut();
					return;
				}else{
					$('#backbankpath').val(e);
					$('#back-bankModal').modal('show');
				}
				
				
			},
			error:function(){
				$('.alert').html('系统维护，请稍后操作！').addClass('alert-warning').show().delay(1000).fadeOut();
				return;
			}
		});

		
		
	})
	
	
	
	//还原题库
	$('#rebackbank').click(function(){
		
		var bank = {'bankid':currentbankid};

		$.ajax({
			url:'rebackbank',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bank),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
				$('.alert').html('还原成功！').addClass('alert-success').show().delay(1000).fadeOut();
				
				window.location.href = "tobankTrashTeacher";
				
			},
			error:function(){
				$('.alert').html('还原失败！').addClass('alert-warning').show().delay(1000).fadeOut();
				window.location.href = "tobankTrashTeacher";
			}
		});
		
		
		
		
	})
	
	
	
	
	
	

});