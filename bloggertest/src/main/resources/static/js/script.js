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

	$('.folder').closest('.folder-report').on('click', this, function() {
		if($(this).find('.folder-item').hasClass('selected')) {
			$(this).find('.folder-item').removeClass('selected');
			$(this).find('.folder-item').removeClass('preselected');
			$(this).find('.check-circle').remove();
			//从选择的文件当中删除
			selectfolders.splice($.inArray(this, selectfolders), 1);
		} else{
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

	$('#delete-folder').click(function() {
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
	
	
	
	//确认重命名文件夹
	$('#renamebankfolderbtn').click(function(){
		var alreadyfolder=$('.folder-report');
		if($('#new-name').val()==null||$('#new-name').val()==''){
			$('.alert').html('文件名不能为空！').addClass('alert-warning').show().delay(2000).fadeOut();

			return;
		}
		var i=0;
		for(i=0;i<alreadyfolder.length;i++){
			if(alreadyfolder.eq(i).find('.folder-name span').text()==$('#new-name').val()){
				//$('#editor-folderModal').modal('hide');
				//alert(alreadyfolder.eq(i).find('.folder-name span').text());
				$('.alert').html('已存在同名文件夹！更名失败').addClass('alert-warning').show().delay(2000).fadeOut();
				return;
			}
		}//$('#editor-folderModal').modal('hide');
		
		var p1 = selectfolders[0].getAttribute('folderid');
//		alert(p);
		var p2 = $('#new-name').val();
		var bf = {'folderid':p1,'foldername':p2};
		$.ajax({
			url:'renamebankfolder',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bf),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				$('.alert').html('重命名成功！').addClass('alert-success').show().delay(1000).fadeOut();

				if($('#currentfolderid').val()==""){
					window.location.href = "tobankTeacher";
				}else{
					window.location.href = "tobankTeacher2?folderid="+$('#currentfolderid').val();
				}
				
				
			},
			error:function(){
				$('.alert').html('重命名失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
		
		
	})
	
	//删除文件夹
	$('#deletebankfolder').click(function(){
		var i=0;
		for(i=0;i<selectfolders.length;i++){
			var p1 = selectfolders[i].getAttribute('folderid');
			var bf = {'folderid':p1};
			$.ajax({
			url:'deletebankfolder',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bf),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				$('.alert').html('删除成功！').addClass('alert-success').show().delay(1000).fadeOut();

				if($('#currentfolderid').val()==""){
					window.location.href = "tobankTeacher";
				}else{
					window.location.href = "tobankTeacher2?folderid="+$('#currentfolderid').val();
				}
				
				
			},
			error:function(){
				$('.alert').html('删除失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
			
		}
	
	
	})
	
	
	
		
	//打开文件夹，进入下一个页面
	$('#open-folder').click(function() {
		if(selectfolders.length == 0) {
			$('.alert').html('没有选中的文件夹').addClass('alert-warning').show().delay(1000).fadeOut();
		} else if(selectfolders.length > 1) {
			$('.alert').html('只能对单个文件夹进行操作').addClass('alert-warning').show().delay(1000).fadeOut();
		} else {
			var folderid =selectfolders[0].getAttribute('folderid');

//		var u = {'bankpath':p1,'folderid':p2};
		window.location.href= "tobankTeacher2?folderid="+folderid;
		}
	})
	
	
	//双击文件打开显示文件夹对应内容
	$(".folder-report").dblclick(function() {
//		alert("111");


		var folderid =$(this).attr('folderid');

//		var u = {'bankpath':p1,'folderid':p2};
		window.location.href= "tobankTeacher2?folderid="+folderid;
		
		
	});
	
	//删除题库操作
	var currentbankid ;
	$('.delete-bank').click(function(){
		if($(this).parent().prev().find('.icon').hasClass('icon-UploadCLoud')){
			$('.alert').html('该题库已上架到题库无法删除').addClass('alert-danger').show().delay(1000).fadeOut();
		}else{
			currentbankid=$(this).closest('.custom-col').attr('bankid');
//			alert(currentbankid);
			$('#delete-bankModal').modal('show');
		}
	})
	
	
	//移动题库打开文件
	var folder = $(".folder-container .folder"),
		front = folder.find('.front'),
		bank = $(".custom-col");
	var $movebank;

	bank.draggable({
		opacity: 0.7,
		helper: "clone"

	});

	folder.droppable({
		accept: '.custom-col',
		hoverClass: "open",
		drop: function(event, ui) {
			var droppedCount;
			if($(this).find(".front").text() == null || $(this).find(".front").text() == '') {
				droppedCount = 0;
			} else {
				droppedCount = parseInt($(this).find(".front").text());
			}
			// Remove the dragged icon

			// update the counters
			$(this).removeClass('open');
			$(this).find(".front").text(++droppedCount);
			
			ui.draggable.remove();
			
			var folderid =$(this).find(".front").attr('folderid');
//			alert(folderid+'***');
			var bankid = ui.draggable.attr('bankid');
//			alert(bankid);

			var b = {'bankid':bankid,'folderid':folderid};
			$.ajax({
			url:'bankmove',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(b),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				
			},
			error:function(){
				alert('移动失败！');
			}
		});
		},

		activate: function() {
			// When the user starts draggin an icon

		},

	});
	
	
	$('#addbank').click(function(){
		
		var name = $('#bank-name').val();
		var description = $('#bank-describe').val();
//		alert(description);
		if(name==null||name==''){
			$('.alert').html('题库名不能为空！').addClass('alert-warning').show().delay(2000).fadeOut();
			return;
		}
		if(description==null||description==''){
			$('.alert').html('题库描述不能为空！').addClass('alert-warning').show().delay(2000).fadeOut();
			return;
		}
		
		var alreadybank=$('.custom-col');
		var i=0;
		for(i=0;i<alreadybank.length;i++){
			if(alreadybank.eq(i).find('.product-text h5 a').text()==name){
				$('.alert').html('已存在同名题库！添加失败').addClass('alert-warning').show().delay(2000).fadeOut();
				return;
			}
		}
		
		var folderid = $('#currentfolderid').val();
		var bank = {'bankname':name,'bankdescription':description,'folderid':folderid};
		$.ajax({
			url:'addbank',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bank),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				$('.alert').html('创建成功！').addClass('alert-success').show().delay(1000).fadeOut();

				if($('#currentfolderid').val()==""){
					window.location.href = "tobankTeacher";
				}else{
					window.location.href = "tobankTeacher2?folderid="+$('#currentfolderid').val();
				}
				
				
			},
			error:function(){
				$('.alert').html('创建失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
		
	})
	
//	$('#editbank').click(function(){
//		alert("11111");
//		alert($('#editbank').attr('value'));
//		//$('#new-bank-name').val($('#editbank').val());
//		
////		$('#new-bank-describe').val($('#editbank').val())
//		$('#editor-bankModal').modal('show');	
//	})
	
	//编辑题库
	$('#editor-sure-btn').click(function(){
	
//		alert($('#clickbankid').val());
		var bankid= $('#clickbankid').val();
		var bankname = $('#new-bank-name').val();
		var bankdescription = $('#new-bank-describe').val();

		if(bankname==null||bankname==''){
			$('.alert').html('题库名不能为空！').addClass('alert-warning').show().delay(2000).fadeOut();
			return;
		}
		if(bankdescription==null||bankdescription==''){
			$('.alert').html('题库描述不能为空！').addClass('alert-warning').show().delay(2000).fadeOut();
			return;
		}

		var alreadybank=$('.custom-col');
		var i=0;
		for(i=0;i<alreadybank.length;i++){
			if(alreadybank.eq(i).find('.product-text h5 a').text()==bankname){
				$('.alert').html('已存在同名题库！添加失败').addClass('alert-warning').show().delay(2000).fadeOut();
				return;
			}
		}
		
		var bank = {'bankid':bankid,'bankname':bankname,'bankdescription':bankdescription};
		$.ajax({
			url:'editbank',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bank),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				$('.alert').html('修改成功！').addClass('alert-success').show().delay(1000).fadeOut();

				if($('#currentfolderid').val()==""){
					window.location.href = "tobankTeacher";
				}else{
					window.location.href = "tobankTeacher2?folderid="+$('#currentfolderid').val();
				}
				 
				
			},
			error:function(){
				$('.alert').html('修改失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
		
		
		
		
	})
	
	
	
	//上架题库
	$('#uploadbank').click(function(){
//		alert("111");
		var bankid = $('#uploadclickbankid').val();
//		alert(bankid);
		var banklabel = $('#banklabel').val();
//		alert(banklabel);
		var price = $('#uploadprice').val();
//		alert(price);
		if(price==null||price==''){
			$('.alert').html('请输入下载所需积分！').addClass('alert-warning').show().delay(2000).fadeOut();
			return;
		}
		var bank = {'bankid':bankid,'banklabel':banklabel,'price':price};
		$.ajax({
			url:'uploadbank',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bank),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				$('.alert').html('上传成功，想要编辑题库请先下架！').addClass('alert-success').show().delay(1000).fadeOut();

				if($('#currentfolderid').val()==""){
					window.location.href = "tobankTeacher";
				}else{
					window.location.href = "tobankTeacher2?folderid="+$('#currentfolderid').val();
				}
				 
				
			},
			error:function(){
				$('.alert').html('上传失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
		
	})
	//删除题库
	$('#deletebank').click(function(){
		var folderid = $('#currentfolderid').val();
		var bank = {'bankid':currentbankid,'folderid':folderid};
		
		
		$.ajax({
			url:'deletebank',
			type:'post',
			contentType:'application/json;charset=utf-8',//请求的字符编码
			data:JSON.stringify(bank),//转换成json格式字符串
			dateType:"text",//返回类型
			success:function(e){
//				alert(e);
				$('.alert').html('删除成功！').addClass('alert-success').show().delay(1000).fadeOut();

				if($('#currentfolderid').val()==""){
					window.location.href = "tobankTeacher";
				}else{
					window.location.href = "tobankTeacher2?folderid="+$('#currentfolderid').val();
				}
				 
				
			},
			error:function(){
				$('.alert').html('删除失败！').addClass('alert-warning').show().delay(1000).fadeOut();

			}
		});
		
		
		
	})
	
	

});