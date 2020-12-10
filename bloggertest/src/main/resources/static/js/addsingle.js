$(function() {
	/*总的方法*/
	$('.btn-save-bank').click(function(){
		//试卷名、考试要求、
		var papername=$('#anchor-paper-name').find('input').val();
		if(papername.replace(/\s+/g, "")==null||papername.replace(/\s+/g, "")==''){
			$('.alert').attr('class', 'alert');
			$('.alert').html('试卷名称不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		}
		var paperrule=$('.paper-guide').val();
		//考试设置
		var testtime=parseInt($('.timelimit-wrap input').val());
		if(isNaN(testtime)||testtime<=0){
			$('.alert').attr('class', 'alert');
			$('.alert').html('请正确输入总时长，范围可为1-300分钟').addClass('alert-danger').show().delay(2000).fadeOut();
			return 1;
		}
		//考试时长
		var testset=
		$.ajax({
			type:"post",
			url:"",
			data:{
				"papername":papername,
				"testset":testset,
				"paperrule":paperrule,
				"testtime":testtime,
			},
			success:function(){
				$('.alert').attr('class', 'alert');
				$('.alert').html('试卷信息设值成功').addClass('alert-success').show().delay(1000).fadeOut();
			},
			error:function(){
				$('.alert').attr('class', 'alert');
				$('.alert').html('信息保存失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//所有的模态框关闭即删除
	$('body').on('hide.bs.modal', '.modal', function() {
		$("body").removeClass('modal-open');
		$(this).remove();
		$('div[class="modal-backdrop fade in"]').remove();
	})
	//收起题目大块面板
	$('body').on('click', '.q-reorder-toggle', function() {
		//目前状态为打开状态
		if($(this).children('i').hasClass('fa-angle-double-up')) {
			$(this).children('i').removeClass('fa-angle-double-up');
			$(this).children('i').addClass('fa-angle-double-down');
			$(this).children('span').html('打开');
			$(this).parent().next().find('.q-list').hide();
		} else {
			$(this).children('i').addClass('fa-angle-double-up');
			$(this).children('i').removeClass('fa-angle-double-down');
			$(this).children('span').html('收起');
			$(this).parent().next().find('.q-list').show();
		}

	});
	//收起具体题目面板
	$('body').on('click', '.q-expand-toggle', function() {
		//目前状态为打开状态
		if($(this).children('i').hasClass('fa-angle-double-up')) {
			$(this).children('i').removeClass('fa-angle-double-up');
			$(this).children('i').addClass('fa-angle-double-down');
			$(this).children('a').html('展开');
			$(this).next().next().next().hide();
		} else {
			$(this).children('i').addClass('fa-angle-double-up');
			$(this).children('i').removeClass('fa-angle-double-down');
			$(this).children('a').html('收起');
			$(this).next().next().next().show();
		}

	});
	//------------------------------------------------------------所有的删除单个题目操作-----------------------------------------------
	//删除单选题目
	$('body').on('click', '.delete-single', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		$.ajax({
			type: "post",
			url: "trandeletesingle",
			data: {
				"qid": qid
			},
			success: function(data) {
				if(data.code == "0000") {
					$('#singlediv').children('li[data-qid=' + qid + ']').remove();
					$('.alert').attr('class', 'alert');
					$('.alert').html('题目删除成功').addClass('alert-success').show().delay(1000).fadeOut();

					//				删除问题
					var i = 1;
					//重置题目顺序
					$("#singlediv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//删除多选题目
	$('body').on('click', '.delete-multiple', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		$.ajax({
			type: "post",
			url: "trandeletemultiple",
			data: {
				"qid": qid,
			},
			success: function(data) {
				if(data.code == "0000") {
					$('#multiplydiv').children('li[data-qid=' + qid + ']').remove();
					$('.alert').attr('class', 'alert');
					$('.alert').html('题目删除成功').addClass('alert-success').show().delay(1000).fadeOut();

					//				删除问题
					var i = 1;
					//重置题目顺序
					$("#multiplydiv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//删除填空题目
	$('body').on('click', '.delete-fill', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		$.ajax({
			type: "post",
			url: "trandeletefill",
			data: {
				"qid": qid
			},
			success: function(data) {
				if(data.code == "0000") {
					$('#filldiv').children('li[data-qid=' + qid + ']').remove();
					$('.alert').attr('class', 'alert');
					$('.alert').html('题目删除成功').addClass('alert-success').show().delay(1000).fadeOut();

					//				删除问题
					var i = 1;
					//重置题目顺序
					$("#filldiv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//删除判断题目
	$('body').on('click', '.delete-judge', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		//		alert(qid);
		$.ajax({
			type: "post",
			url: "trandeletejudge",
			data: {
				"qid": qid
			},
			success: function(data) {
				if(data.code == "0000") {
					$('#judgediv').children('li[data-qid=' + qid + ']').remove();
					$('.alert').attr('class', 'alert');
					$('.alert').html('题目删除成功').addClass('alert-success').show().delay(1000).fadeOut();

					//				删除问题
					var i = 1;
					//重置题目顺序
					$("#judgediv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//删除简答题目
	$('body').on('click', '.delete-short', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		$.ajax({
			type: "post",
			url: "trandeleteshortanswer",
			data: {
				"qid": qid
			},
			success: function(data) {
				if(data.code == "0000") {
					$('#shortanswerdiv').children('li[data-qid=' + qid + ']').remove();
					$('.alert').attr('class', 'alert');
					$('.alert').html('题目删除成功').addClass('alert-success').show().delay(1000).fadeOut();

					//				删除问题
					var i = 1;
					//重置题目顺序
					$("#shortanswerdiv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})

	/*添加知识点*/
	$('body').on('click', ".skill-list .btn-add", function() {
		//					alert('1');
		var pattern = /^[\u4E00-\u9FA5]{1,6}$/;
		var konwlegeStr = $(this).parent().prev().val();
		if(!pattern.test($(this).parent().prev().val())) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('知识点请用中文表示').addClass('alert-danger').show().delay(1000).fadeOut();
		} else {
			$("div[class='fuDiv']").append("<li>" + konwlegeStr + "</li>");
			$(this).parent().prev().val('');
		}

	});
	/*模式层知识点确认退出按钮同时*/
	$('body').on('click', "a[class='btn btn-add btn-xs']", function() {
		$(this).parents().find('.dropdown').hide();
		$(this).parents().find('.dropdown').removeClass('open');

	});
	$('body').on('click', "a[class='btn btn-default btn-xs']", function() {
		$(this).parents().find('.dropdown').hide();
		$(this).parents().find('.dropdown').removeClass('open');
		$("span[class='pull-left skill-name text-ellipsis']").text('无');
	});
	/*点击选择知识点*/
	$('body').on('click', 'div[class="fuDiv"] li', function() {
		$(this).addClass('checked virtual');
		$("span[class='pull-left skill-name text-ellipsis']").text($(this).text());
		$(this).siblings().removeClass('text-ellipsis checked virtual');

	})
	/*双击确认点击选择知识点*/
	$('body').on('dblclick', 'div[class="fuDiv"] li', function() {
		$(this).parents().find('.dropdown').hide();
		$(this).parents().find('.dropdown').removeClass('open');

	})
	/*知识点层点击出现*/
	$('body').on('click', "a[class='btn btn-default trigger']", function() {
		if($(this).next().hasClass('open')) {
			$(this).next().hide();
			$(this).next().removeClass('open');
		} else {
			$(this).next().show();
			$(this).next().addClass('open');
		}
	})
	/* -----------------------------------单选------------------------------------ */
	/*点击添加选项*/
	$('body').on('click', '.btn-add-option', function() {
		if($(this).closest('.options').find('li[class="option"]').size() > 26) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('选项不得多余26项').addClass('alert-danger').show().delay(1000).fadeOut();
		} else {
			var temp = $(this).closest('.modal').find('.insertItem li').clone();
			$(this).parents('center').before(temp);
		}

	})
	//
	/*点击删除单选选项*/
	$('body').on('click', '.modal .options .btn-remove-option', function() {
		if($(this).closest('.options').find('li[class="option"]').size() < 3) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('选项不得少于两项').addClass('alert-danger').show().delay(1000).fadeOut();
		} else {
			/*判断是否删除的是check按钮*/
			if($(this).siblings('label').find('input[type="radio"]').is(':checked')) {
				//				alert($(this).siblings('label').html());
				var modal = $(this).parents('.modal');
				//				alert('11');
				$(this).closest('li').remove();
				modal.find('.option').eq(0).find('input[type="radio"]').prop("checked", true);
			} else {
				$(this).closest('li').remove();
			}
		}

	})

	/*上传图片视频*/
	$('body').on('mouseenter', '.btn-add-media', function() {
		$(this).find('div').show();
	})
	$('body').on('mouseleave', '.btn-add-media', function() {

		$(this).find('div').hide();
	})

	/*---------------------------------------------------------------- 增加模态框 ------------------------------------------------------------------*/

	/*点击添加获取单选模态框*/
	$('.btn-add-singlequestions').click(function() {
		//ajax取出知识点显示模态框
		$.ajax({
			type: "post",
			url: "getkonwledges",
			data: {
				"paperid": 1,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('add-singelModaldiv', data);
					$('body').append(html);
					$('#add-singelModal').modal('show');
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！无法添加题目').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})

	/*点击添加获取多选模态框*/
	$('.btn-add-multiplequestions').click(function() {
		//ajax取出知识点显示模态框
		$.ajax({
			type: "post",
			url: "getkonwledges",
			data: {
				"paperid": 1,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('add-multipleModaldiv', data);
					$('body').append(html);
					$('#add-multipleModal').modal('show');
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！无法添加题目').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})

	/*点击添加获取判断模态框*/
	$('.btn-add-judgequestions').click(function() {
		//ajax取出知识点显示模态框
		$.ajax({
			type: "post",
			url: "getkonwledges",
			data: {
				"paperid": 1,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('add-judgeModaldiv', data);
					$('body').append(html);
					$('#add-judgeModal').modal('show');
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！无法添加题目').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})

	/*点击添加获取填空模态框*/
	$('.btn-add-fillquestions').click(function() {
		//ajax取出知识点显示模态框
		$.ajax({
			type: "post",
			url: "getkonwledges",
			data: {
				"paperid": 1,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('add-fillModaldiv', data);
					$('body').append(html);
					$('#add-fillModal').modal('show');
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！无法添加题目').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})

	/*点击添加获取简答模态框*/
	$('.btn-add-shortquestions').click(function() {
		//ajax取出知识点显示模态框
		$.ajax({
			type: "post",
			url: "getkonwledges",
			data: {
				"paperid": 1,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('add-shortModaldiv', data);
					$('body').append(html);
					$('#add-shortModal').modal('show');
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！无法添加题目').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})

	/*----------------------------------------------------确认增加题目按钮----------------------------------------------------------*/
	/*判断选项是否重复*/
	$('body').on('focusout', '.modal .options .option pre', function() {
		/*选项*/
		var options = $(this).parents('.modal').find('.options .option');
		var i = 0;
		var flag = 0;
		var itemOption = $(this).text();
		for(i = 0; i < options.size(); i++) {
			itemOption = itemOption.replace(/\s+/g, "");
			var curoption = options.eq(i).find('pre').text();
			curoption = curoption.replace(/\s+/g, "");
			if(curoption == itemOption && itemOption != '' && itemOption != null) {
				flag++;
			}
		}
		if(flag >= 2) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('目前内容重复！请慎重提交').addClass('alert-warning').show().delay(2000).fadeOut();
		}
	})

	/*添加单选题各输入框不为空的判断*/
	$('body').on('click', '.editorsingel-sure-btn', function() {
		var flag = 0;

		/*分数*/
		var score = parseInt($(this).closest('.modal').find('.scoreinput').val());
		if(score > 100 || score < 0 || isNaN(score)) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('输入分数不符合规则').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		}
		/*题干*/
		var itemTitle = $(this).parents('.modal').find('.form-title').children('div').children('p').text();
		itemTitle = itemTitle.replace(/\s+/g, "");
		if(itemTitle == "" || itemTitle == null) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('题干不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		} else {
			flag++;
		}
		/*选项*/
		var options = $(this).parents('.modal').find('.options .option');
		var i = 0;
		for(i = 0; i < options.size(); i++) {
			var itemOption = options.eq(i).find('pre').text();
			itemOption = itemOption.replace(/\s+/g, "");
			if(itemOption == '' || itemOption == null) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('选项不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
				return 1;
			}
		}
		/*发送json把题发到后台*/
		//获得用于存储答题数据
		var obj = {};
		var singleChoicesContent = [];
		var singlechoicejudge = [];
		var single_title = $(this).parents('.modal').find('.form-title').children('div').children('p').text();;
		var singelscore = parseInt($(this).parents('.modal').find('.scoreinput').val());
		var knowledge = $(this).parents('.modal').find('.skill-name').text();
		var analysis = $(this).parents('.modal').find('.analysis').children('div').children('p').text();
		var difficulty = parseInt($(this).parents('.modal').find('input[name="radiodifficult"]:checked').val());
		var qid = parseInt($(this).parents('.modal').attr('data-qid'));
		var sequence = $(this).parents('.modal').attr('data-sequence');
		obj['paperid'] = 1;
		obj['single_title'] = single_title;
		obj['singelscore'] = singelscore;
		obj['knowledge'] = knowledge;
		obj['analysis'] = analysis;
		obj['difficulty'] = difficulty;
		obj['qid'] = qid;
		var thisitem = $(this);
		//单选题选项
		for(i = 0; i < options.size(); i++) {
			var optionpre = options.eq(i).find('pre');
			var itemOption = options.eq(i).find('pre').text();
			//			alert(itemOption);
			if(options.eq(i).find('input[type="radio"]').is(':checked')) {
				singlechoicejudge.push(1);

			} else {
				singlechoicejudge.push(0);
			}
			//选项对错
			//			alert(options.eq(i).find('input[type="radio"]').is(':checked'));
			singleChoicesContent.push(itemOption);
		}
		obj['singleChoicesContent'] = singleChoicesContent;
		obj['singlechoicejudge'] = singlechoicejudge;
		//		alert("obj"+JSON.stringify(obj));
		$.ajax({
			type: 'post',
			url: 'transingle',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {

				var obj = JSON.parse(res);
				var html = template('singleitem', obj);
				qid = obj.paper_single.qId;
				//修改
				if(!isNaN(parseInt(sequence))) {
					$('#singlediv>li').eq(parseInt(sequence) - 1).replaceWith(html);
					$('.alert').attr('class', 'alert');
					$('.alert').html('修改题目成功').addClass('alert-success').show().delay(1000).fadeOut();

				} //新增
				else {
					$('#singlediv').append(html);
					//待删，新加入的元素已在模板中加入id
					$('#singlediv>li:last').attr("data-qid", qid);
					$('.alert').attr('class', 'alert');
					$('.alert').html('添加题目成功').addClass('alert-success').show().delay(1000).fadeOut();
				}
				//移除当前模态框
				var i = 1;
				//重置题目顺序
				$("#singlediv>li").each(function() {

					$(this).find('.q-sequence').html(i);
					i++;
				})
				//				thisitem.parents('.modal').modal('hide');
				$("body").removeClass('modal-open');
				thisitem.parents('.modal').remove();
				$('div[class="modal-backdrop fade in"]').remove();
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('添加题目失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}

		});

	});

	/*取人添加多选题各输入框不为空的判断*/
	$('body').on('click', '.editormultiple-sure-btn', function() {
		var flag = 0;

		/*分数*/
		var score = parseInt($(this).parents('.modal').find('.scoreinput').val());
		if(score > 100 || score < 0 || isNaN(score)) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('输入分数不符合规则').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		}
		/*题干*/
		var itemTitle = $(this).parents('.modal').find('.form-title').children('div').children('p').text();
		itemTitle = itemTitle.replace(/\s+/g, "");
		if(itemTitle == "" || itemTitle == null) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('题干不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		} else {
			flag++;
		}
		/*选项*/
		var options = $(this).parents('.modal').find('.options .option');
		var i = 0;
		var checkoptionnum = 0;
		for(i = 0; i < options.size(); i++) {
			var itemOption = options.eq(i).find('pre').text();
			itemOption = itemOption.replace(/\s+/g, "");
			if(itemOption == '' || itemOption == null) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('选项不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
				return 1;
			}
			if(options.eq(i).find('input[type="checkbox"]').is(':checked')) {
				checkoptionnum++;
			}
		}
		if(checkoptionnum < 2) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('正确选项至少为两项').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		}
		/*发送json把题发到后台*/
		//获得用于存储答题数据
		var obj = {};
		var multipleChoicesContent = [];
		var multiplechoicejudge = [];
		var multiple_title = $(this).parents('.modal').find('.form-title').children('div').children('p').text();;
		var multiplescore = parseInt($(this).parents('.modal').find('.scoreinput').val());
		var knowledge = $(this).parents('.modal').find('.skill-name').text();
		var analysis = $(this).parents('.modal').find('.analysis').children('div').children('p').text();
		var difficulty = parseInt($(this).parents('.modal').find('input[name="radiodifficult"]:checked').val());
		var qid = parseInt($(this).parents('.modal').attr('data-qid'));
		var sequence = $(this).parents('.modal').attr('data-sequence');
		obj['paperid'] = 1;
		obj['multiple_title'] = multiple_title;
		obj['multiplescore'] = multiplescore;
		obj['knowledge'] = knowledge;
		obj['analysis'] = analysis;
		obj['difficulty'] = difficulty;
		obj['qid'] = qid;
		var thisitem = $(this);
		//多选题选项
		for(i = 0; i < options.size(); i++) {
			var optionpre = options.eq(i).find('pre');
			var itemOption = options.eq(i).find('pre').text();
			//			alert(itemOption);
			if(options.eq(i).find('input[type="checkbox"]').is(':checked')) {
				multiplechoicejudge.push(1);

			} else {
				multiplechoicejudge.push(0);
			}
			//选项对错
			//			alert(options.eq(i).find('input[type="radio"]').is(':checked'));
			multipleChoicesContent.push(itemOption);
		}
		obj['multipleChoicesContent'] = multipleChoicesContent;
		obj['multiplechoicejudge'] = multiplechoicejudge;
		//				alert("obj"+JSON.stringify(obj));
		$.ajax({
			type: 'post',
			url: 'tranmultiple',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {

				var obj = JSON.parse(res);
				var html = template('multiplyitem', obj);
				qid = obj.paper_multiple.qId;
				//修改
				if(!isNaN(parseInt(sequence))) {
					$('#multiplydiv>li').eq(parseInt(sequence) - 1).replaceWith(html);
					$('.alert').attr('class', 'alert');
					$('.alert').html('修改题目成功').addClass('alert-success').show().delay(1000).fadeOut();

				} //新增
				else {
					$('#multiplydiv').append(html);
					//待删，新加入的元素已在模板中加入id
					$('#multiplydiv>li:last').attr("data-qid", qid);
					$('.alert').attr('class', 'alert');
					$('.alert').html('添加题目成功').addClass('alert-success').show().delay(1000).fadeOut();
				}
				//移除当前模态框
				var i = 1;
				//重置题目顺序
				$("#multiplydiv>li").each(function() {

					$(this).find('.q-sequence').html(i);
					i++;
				})
				//				thisitem.parents('.modal').modal('hide');
				$("body").removeClass('modal-open');
				thisitem.parents('.modal').remove();
				$('div[class="modal-backdrop fade in"]').remove();
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('添加题目失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}

		});

	});

	/*取人添加判断题各输入框不为空的判断*/
	$('body').on('click', '.editorjudge-sure-btn', function() {
		var flag = 0;

		/*分数*/
		var score = parseInt($(this).parents('.modal').find('.scoreinput').val());
		if(score > 100 || score < 0 || isNaN(score)) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('输入分数不符合规则').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		}
		/*题干*/
		var itemTitle = $(this).parents('.modal').find('.form-title').children('div').children('p').text();
		itemTitle = itemTitle.replace(/\s+/g, "");
		if(itemTitle == "" || itemTitle == null) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('题干不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		} else {
			flag++;
		}
		/*选项*/
		var options = $(this).parents('.modal').find('.options input[type="radio"]');

		/*发送json把题发到后台*/
		//获得用于存储答题数据
		var obj = {};
		var isture;
		var judge_title = $(this).parents('.modal').find('.form-title').children('div').children('p').text();;
		var judgescore = parseInt($(this).parents('.modal').find('.scoreinput').val());
		var knowledge = $(this).parents('.modal').find('.skill-name').text();
		var analysis = $(this).parents('.modal').find('.analysis').children('div').children('p').text();
		var difficulty = parseInt($(this).parents('.modal').find('input[name="radiodifficult"]:checked').val());
		var qid = parseInt($(this).parents('.modal').attr('data-qid'));
		var sequence = $(this).parents('.modal').attr('data-sequence');
		obj['paperid'] = 1;
		obj['judge_title'] = judge_title;
		obj['judgescore'] = judgescore;
		obj['knowledge'] = knowledge;
		obj['analysis'] = analysis;
		obj['difficulty'] = difficulty;
		obj['qid'] = qid;
		var thisitem = $(this);
		//判断题正确性
		if(options.eq(0).is(':checked')) {
			isture = 0;
		} else {
			isture = 1;
		}
		obj['isture'] = isture;
		//		alert("obj"+JSON.stringify(obj));
		$.ajax({
			type: 'post',
			url: 'tranjudge',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {

				var obj = JSON.parse(res);
				var html = template('judgeitem', obj);
				qid = obj.paper_judge.qId;
				//修改
				if(!isNaN(parseInt(sequence))) {
					$('#judgediv>li').eq(parseInt(sequence) - 1).replaceWith(html);
					$('.alert').attr('class', 'alert');
					$('.alert').html('修改题目成功').addClass('alert-success').show().delay(1000).fadeOut();

				} //新增
				else {
					$('#judgediv').append(html);
					//待删，新加入的元素已在模板中加入id
					$('#judgediv>li:last').attr("data-qid", qid);
					$('.alert').attr('class', 'alert');
					$('.alert').html('添加题目成功').addClass('alert-success').show().delay(1000).fadeOut();
				}
				//移除当前模态框
				var i = 1;
				//重置题目顺序
				$("#judgediv>li").each(function() {

					$(this).find('.q-sequence').html(i);
					i++;
				})
				//				thisitem.parents('.modal').modal('hide');
				$("body").removeClass('modal-open');
				thisitem.parents('.modal').remove();
				$('div[class="modal-backdrop fade in"]').remove();
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('添加题目失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}

		});
	});

	/*取人添加填空题各输入框不为空的判断*/
	$('body').on('click', '.editorfill-sure-btn', function() {
		var flag = 0;

		/*分数*/
		var score = parseInt($(this).parents('.modal').find('.scoreinput').val());
		if(score > 100 || score < 0 || isNaN(score)) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('输入分数不符合规则').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		}
		/*题干*/
		var itemTitle = $(this).parents('.modal').find('.form-title').children('div').children('p').text();
		itemTitle = itemTitle.replace(/\s+/g, "");
		if(itemTitle == "" || itemTitle == null) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('题干不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		} else {
			flag++;
		}
		/*选项*/
		var options = $(this).parents('.modal').find('.options .option');
		var i = 0;
		for(i = 0; i < options.size(); i++) {
			var itemOption = options.eq(i).find('pre').text();
			itemOption = itemOption.replace(/\s+/g, "");
			if(itemOption == '' || itemOption == null) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('答案不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
				return 1;
			}
		}
		/*发送json把题发到后台*/
		//获得用于存储答题数据
		var obj = {};
		var fillcontent = [];
		var fill_title = $(this).parents('.modal').find('.form-title').children('div').children('p').text();;
		var fillscore = parseInt($(this).parents('.modal').find('.scoreinput').val());
		var knowledge = $(this).parents('.modal').find('.skill-name').text();
		var analysis = $(this).parents('.modal').find('.analysis').children('div').children('p').text();
		var difficulty = parseInt($(this).parents('.modal').find('input[name="radiodifficult"]:checked').val());
		var qid = parseInt($(this).parents('.modal').attr('data-qid'));
		var sequence = $(this).parents('.modal').attr('data-sequence');
		obj['paperid'] = 1;
		obj['fill_title'] = fill_title;
		obj['fillscore'] = fillscore;
		obj['knowledge'] = knowledge;
		obj['analysis'] = analysis;
		obj['difficulty'] = difficulty;
		obj['qid'] = qid;
		var thisitem = $(this);
		//单选题选项
		for(i = 0; i < options.size(); i++) {
			var optionpre = options.eq(i).find('pre');
			var itemOption = options.eq(i).find('pre').text();
			fillcontent.push(itemOption);
		}
		obj['fillcontent'] = fillcontent;
		//		alert("obj"+JSON.stringify(obj));
		$.ajax({
			type: 'post',
			url: 'tranfill',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {

				var obj = JSON.parse(res);
				var html = template('fillitem', obj);
				qid = obj.paper_fill.qId;
				//修改
				if(!isNaN(parseInt(sequence))) {
					$('#filldiv>li').eq(parseInt(sequence) - 1).replaceWith(html);
					$('.alert').attr('class', 'alert');
					$('.alert').html('修改题目成功').addClass('alert-success').show().delay(1000).fadeOut();

				} //新增
				else {
					$('#filldiv').append(html);
					//待删，新加入的元素已在模板中加入id
					$('#filldiv>li:last').attr("data-qid", qid);
					$('.alert').attr('class', 'alert');
					$('.alert').html('添加题目成功').addClass('alert-success').show().delay(1000).fadeOut();
				}
				//移除当前模态框
				var i = 1;
				//重置题目顺序
				$("#filldiv>li").each(function() {

					$(this).find('.q-sequence').html(i);
					i++;
				})
				//				thisitem.parents('.modal').modal('hide');
				$("body").removeClass('modal-open');
				thisitem.parents('.modal').remove();
				$('div[class="modal-backdrop fade in"]').remove();
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('添加题目失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}

		});

	});

	/*添加简答各输入框不为空的判断*/
	$('body').on('click', '.editorshort-sure-btn', function() {
		var flag = 0;

		/*分数*/
		var score = parseInt($(this).parents('.modal').find('.scoreinput').val());
		if(score > 100 || score < 0 || isNaN(score)) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('输入分数不符合规则').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		}
		/*题干*/
		var itemTitle = $(this).parents('.modal').find('.form-title').children('div').children('p').text();
		itemTitle = itemTitle.replace(/\s+/g, "");
		if(itemTitle == "" || itemTitle == null) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('题干不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		} else {
			flag++;
		}

		/*发送json把题发到后台*/
		//获得用于存储答题数据
		var analysis = $(this).parents('.modal').find('.analysis').children('div').children('p').text();
		analysis = analysis.replace(/\s+/g, "");
		if(analysis == '' || analysis == null) {
			$('.alert').attr('class', 'alert');
			$('.alert').html('简答题参考答案不能为空').addClass('alert-danger').show().delay(1000).fadeOut();
			return 1;
		}
		var obj = {};
		var fillcontent = [];
		var shortanswer_title = $(this).parents('.modal').find('.form-title').children('div').children('p').text();;
		var shortanswerscore = parseInt($(this).parents('.modal').find('.scoreinput').val());
		var knowledge = $(this).parents('.modal').find('.skill-name').text();
		var difficulty = parseInt($(this).parents('.modal').find('input[name="radiodifficult"]:checked').val());
		var qid = parseInt($(this).parents('.modal').attr('data-qid'));
		var sequence = $(this).parents('.modal').attr('data-sequence');
		obj['paperid'] = 1;
		obj['shortanswer_title'] = shortanswer_title;
		obj['shortanswerscore'] = shortanswerscore;
		obj['knowledge'] = knowledge;
		obj['analysis'] = analysis;
		obj['difficulty'] = difficulty;
		obj['qid'] = qid;
		//		alert("obj"+JSON.stringify(obj));
		var thisitem = $(this);
		$.ajax({
			type: 'post',
			url: 'transhortanswer',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {

				var obj = JSON.parse(res);
				var html = template('shortitem', obj);
				qid = obj.paper_shortanswer.qId;
				//修改
				if(!isNaN(parseInt(sequence))) {
					$('#shortanswerdiv>li').eq(parseInt(sequence) - 1).replaceWith(html);
					$('.alert').attr('class', 'alert');
					$('.alert').html('修改题目成功').addClass('alert-success').show().delay(1000).fadeOut();

				} //新增
				else {
					$('#shortanswerdiv').append(html);
					//待删，新加入的元素已在模板中加入id
					$('#shortanswerdiv>li:last').attr("data-qid", qid);
					$('.alert').attr('class', 'alert');
					$('.alert').html('添加题目成功').addClass('alert-success').show().delay(1000).fadeOut();
				}
				//移除当前模态框
				var i = 1;
				//重置题目顺序
				$("#shortanswerdiv>li").each(function() {

					$(this).find('.q-sequence').html(i);
					i++;
				})
				//				thisitem.parents('.modal').modal('hide');
				$("body").removeClass('modal-open');
				thisitem.parents('.modal').remove();
				$('div[class="modal-backdrop fade in"]').remove();
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('添加题目失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}

		});

	});

	/*------------------------------------------------------修改按钮修改模态框--------------------------------------------------------------------*/
	//修改单选题
	$('body').on('click', '.edit-single', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		//  	alert(qid);
		var sequence = parseInt($(this).closest('.q-item').find('.q-sequence').html());

		$.ajax({
			type: "post",
			url: "traneditsingle",
			async: false,
			data: {
				"qid": qid,
				"sequence": sequence,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('edit-singelModaldiv', data);
					$('body').append(html);
					$('#editor-singelModal').modal('show');
				} else if(data.code == "60000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('抱歉查无题无法修改').addClass('alert-warning').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});

	})

	//修改多选题
	$('body').on('click', '.edit-multiple', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		var sequence = parseInt($(this).closest('.q-item').find('.q-sequence').html());
		$.ajax({
			type: "post",
			url: "traneditmultiple",
			data: {
				"qid": qid,
				"sequence": sequence,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('edit-multipleModaldiv', data);
					$('body').append(html);
					$('#editor-multipleModal').modal('show');
				} else if(data.code == "60000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('抱歉查无题无法修改').addClass('alert-warning').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});

	})
	//修改判断题
	$('body').on('click', '.edit-judge', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		var sequence = parseInt($(this).closest('.q-item').find('.q-sequence').html());
		$.ajax({
			type: "post",
			url: "traneditjudge",
			data: {
				"qid": qid,
				"sequence": sequence,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('edit-judgeModaldiv', data);
					$('body').append(html);
					$('#editor-judgeModal').modal('show');
				} else if(data.code == "60000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('抱歉查无题无法修改').addClass('alert-warning').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});

	})

	//修改填空题
	$('body').on('click', '.edit-fill', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		var sequence = parseInt($(this).closest('.q-item').find('.q-sequence').html());
		$.ajax({
			type: "post",
			url: "traneditfill",
			data: {
				"qid": qid,
				"sequence": sequence,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('edit-fillModaldiv', data);
					$('body').append(html);
					$('#editor-fillModal').modal('show');
				} else if(data.code == "60000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('抱歉查无题无法修改').addClass('alert-warning').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});

	})

	//修改简答题
	$('body').on('click', '.edit-short', function() {
		var qid = parseInt($(this).closest('.q-item').attr('data-qid'));
		var sequence = parseInt($(this).closest('.q-item').find('.q-sequence').html());
		$.ajax({
			type: "post",
			url: "traneditshortanswer",
			data: {
				"qid": qid,
				"sequence": sequence,
			},
			success: function(data) {
				if(data.code == "0000") {
					var html = template('edit-shortModaldiv', data);
					$('body').append(html);
					$('#editor-shortModal').modal('show');
				} else if(data.code == "60000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('抱歉查无题无法修改').addClass('alert-warning').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！题目删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});

	})

	/*------------------------------------------------------题目全选--------------------------------------------------------------------*/
	$('.checkall input[type="checkbox"]').change(function() {
		if($(this).is(':checked')) {
			//			alert($(this).closest('.q-list').find('.check-wrap').size());
			$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
				$(this).find('input[type="checkbox"]').prop('checked', true);
			})
		} else {
			$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
				$(this).find('input[type="checkbox"]').prop('checked', false);
			})
		}
	})

	/*------------------------------------------------------批量删除--------------------------------------------------------------------*/
	//批量删除单选题目
	$('body #paper-part-0').on('click', '.delete-sure-btn', function() {
		var checkchoices = [];
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		$.ajax({
			type: "post",
			url: 'trandeletesingles',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#paper-part-0').find('.check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).closest('.q-item').remove();
						}
					})
					//重置题目顺序
					var i = 1;
					$("#singlediv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//批量删除多选题目
	$('body #paper-part-1').on('click', '.delete-sure-btn', function() {
		var checkchoices = [];
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		$.ajax({
			type: "post",
			url: 'trandeletemultiples',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#paper-part-1').find('.check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).closest('.q-item').remove();
						}
					})
					//重置题目顺序
					var i = 1;
					$("#multiplydiv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	});

	//批量删除判断题目
	$('body #paper-part-2').on('click', '.delete-sure-btn', function() {
		var checkchoices = [];
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		$.ajax({
			type: "post",
			url: 'trandeletejudges',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#paper-part-2').find('.check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).closest('.q-item').remove();
						}
					})
					//重置题目顺序
					var i = 1;
					$("#judgediv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	});

	//批量删除填空题目
	$('body #paper-part-3').on('click', '.delete-sure-btn', function() {
		var checkchoices = [];
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		$.ajax({
			type: "post",
			url: 'trandeletefills',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#paper-part-3').find('.check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).closest('.q-item').remove();
						}
					})
					//重置题目顺序
					var i = 1;
					$("#filldiv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	});

	//批量删除简答题目
	$('body #paper-part-4').on('click', '.delete-sure-btn', function() {
		var checkchoices = [];
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		$.ajax({
			type: "post",
			url: 'trandeleteshortanswers',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#paper-part-4').find('.check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).closest('.q-item').remove();
						}
					})
					//重置题目顺序
					var i = 1;
					$("#shortanswerdiv>li").each(function() {

						$(this).find('.q-sequence').html(i);
						i++;
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('批量删除失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	});

	/*------------------------------------------------------批量设置分值-------------------------------------------------------------------*/
	//单选题分值
	$('.btn-setsingle-point-confirm').click(function() {
		var checkchoices = [];
		var score = parseInt($(this).siblings('input').val());
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		obj['score'] = score;
		$.ajax({
			type: "post",
			url: 'tranpaperscoresingle',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#singlediv .check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).next().find('.label-skill').eq(0).text(score + '分');
						}
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//多选题分值
	$('.btn-setmultiply-point-confirm').click(function() {
		var checkchoices = [];
		var score = parseInt($(this).siblings('input').val());
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		obj['score'] = score;
		$.ajax({
			type: "post",
			url: 'tranpaperscoremultiple',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#multiplydiv .check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).next().find('.label-skill').eq(0).text(score + '分');
						}
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//判断题分值
	$('.btn-setjudge-point-confirm').click(function() {
		var checkchoices = [];
		var score = parseInt($(this).siblings('input').val());
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		obj['score'] = score;
		$.ajax({
			type: "post",
			url: 'tranpaperscorejudge',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#judgediv .check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).next().find('.label-skill').eq(0).text(score + '分');
						}
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//填空题题分值
	$('.btn-setfill-point-confirm').click(function() {
		var checkchoices = [];
		var score = parseInt($(this).siblings('input').val());
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		obj['score'] = score;
		$.ajax({
			type: "post",
			url: 'tranpaperscorefill',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#filldiv .check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).next().find('.label-skill').eq(0).text(score + '分');
						}
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	//简答题分值
	$('.btn-setshort-point-confirm').click(function() {
		var checkchoices = [];
		var score = parseInt($(this).siblings('input').val());
		var obj = {};
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked')) {
				checkchoices.push(parseInt($(this).closest('.q-item').attr('data-qid')));
			}
		})
		obj['checkchoices'] = checkchoices;
		obj['score'] = score;
		$.ajax({
			type: "post",
			url: 'tranpaperscoreshortanswer',
			data: JSON.stringify(obj),
			contentType: "application/json; charset=utf-8",
			dataType: 'text',
			success: function(res) {
				var obj = JSON.parse(res);
				if(obj.code == "0000") {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值成功').addClass('alert-success').show().delay(1000).fadeOut();
					$('#shortanswerdiv .check-wrap').each(function() {
						if($(this).find('input[type="checkbox"]').is(':checked')) {
							$(this).next().find('.label-skill').eq(0).text(score + '分');
						}
					})
				} else {
					$('.alert').attr('class', 'alert');
					$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
				}
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('设置分值失败').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})

	/*------------------------------------------------------上下移动题目位置-------------------------------------------------------------------*/
	//上移
	$('.btn-up').click(function() {
		var checkchoices = [];
		var obj = {};
		var i = 1;
		var url;
		if($(this).closest('.pin-wrapper').next().attr('id') == 'singlediv') {
			url = 'tranmovesingle';
		} else if($(this).closest('.pin-wrapper').next().attr('id') == 'multiplydiv') {
			url = 'tranmovemultiple';
		} else if($(this).closest('.pin-wrapper').next().attr('id') == 'judgediv') {
			url = 'tranmovejudge';
		} else if($(this).closest('.pin-wrapper').next().attr('id') == 'filldiv') {
			url = 'tranmovefill';
		} else if($(this).closest('.pin-wrapper').next().attr('id') == 'shortanswerdiv') {
			url = 'tranmoveshortanswer';
		}

		var flag = false; //下一个能否移动标志位，如果此刻未被选中则flag=true,在此刻被选中已被移动则为true
		$(this).closest('.part-questions-container').find('.check-wrap').each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked') && i != 1 && flag == true) {
				var item1 = $(this).closest('.q-item');
				var item2 = $(this).closest('.q-item').prev();
				var q1id = parseInt(item1.attr('data-qid'));
				var q2id = parseInt(item2.attr('data-qid'));
				$.ajax({
					type: "post",
					url: url,
					async: false,
					data: {
						"q1id": q1id,
						"q2id": q2id
					},
					success: function(res) {
						if(res.code == "0000") {
							//							$('.alert').attr('class', 'alert');
							//							$('.alert').html('移动成功').addClass('alert-success').show().delay(1000).fadeOut();
							item1.attr('data-qid', q2id);
							item2.attr('data-qid', q1id);
							item1.find('.check-wrap input[type="checkbox"]').attr('data-qid', q2id);
							item2.find('.check-wrap input[type="checkbox"]').attr('data-qid', q1id);
							item1.insertBefore(item2);
							var sequencetemp = item1.find('.q-sequence').text();
							item1.find('.q-sequence').text(item2.find('.q-sequence').text());
							item2.find('.q-sequence').text(sequencetemp);
							flag = true;
						} else {
							$('.alert').attr('class', 'alert');
							$('.alert').html('上移失败').addClass('alert-danger').show().delay(1000).fadeOut();
						}
					},
					error: function(e) {
						$('.alert').attr('class', 'alert');
						$('.alert').html('上移失败').addClass('alert-danger').show().delay(1000).fadeOut();
					}
				});
			} else if(!$(this).find('input[type="checkbox"]').is(':checked')) {
				flag = true;
			}
			i++;
		})
	})

	//下移
	$('.btn-down').click(function() {
		var checkchoices = [];
		var obj = {};
		var i = 1;
		var url;
		if($(this).closest('.pin-wrapper').next().attr('id') == 'singlediv') {
			url = 'tranmovesingle';
		} else if($(this).closest('.pin-wrapper').next().attr('id') == 'multiplydiv') {
			url = 'tranmovemultiple';
		} else if($(this).closest('.pin-wrapper').next().attr('id') == 'judgediv') {
			url = 'tranmovejudge';
		} else if($(this).closest('.pin-wrapper').next().attr('id') == 'filldiv') {
			url = 'tranmovefill';
		} else if($(this).closest('.pin-wrapper').next().attr('id') == 'shortanswerdiv') {
			url = 'tranmoveshortanswer';
		}
		var flag = false; //下一个能否移动标志位，如果此刻未被选中则flag=true,在此刻被选中已被移动则为true
		$($(this).closest('.part-questions-container').find('.check-wrap').toArray().reverse()).each(function() {
			if($(this).find('input[type="checkbox"]').is(':checked') && i != 1 && flag == true) {
				var item1 = $(this).closest('.q-item');
				var item2 = $(this).closest('.q-item').next();
				var q1id = parseInt(item1.attr('data-qid'));
				var q2id = parseInt(item2.attr('data-qid'));
				$.ajax({
					type: "post",
					url: url,
					async: false,
					data: {
						"q1id": q1id,
						"q2id": q2id
					},
					success: function(res) {
						if(res.code == "0000") {
							//							$('.alert').attr('class', 'alert');
							//							$('.alert').html('下移成功').addClass('alert-success').show().delay(1000).fadeOut();
							item1.attr('data-qid', q2id);
							item2.attr('data-qid', q1id);
							item1.find('.check-wrap input[type="checkbox"]').attr('data-qid', q2id);
							item2.find('.check-wrap input[type="checkbox"]').attr('data-qid', q1id);
							item1.insertAfter(item2);
							var sequencetemp = item1.find('.q-sequence').text();
							item1.find('.q-sequence').text(item2.find('.q-sequence').text());
							item2.find('.q-sequence').text(sequencetemp);
							flag = true;
						} else {
							$('.alert').attr('class', 'alert');
							$('.alert').html('下移失败').addClass('alert-danger').show().delay(1000).fadeOut();
						}
					},
					error: function(e) {
						$('.alert').attr('class', 'alert');
						$('.alert').html('下移失败').addClass('alert-danger').show().delay(1000).fadeOut();
					}
				});
			} else if(!$(this).find('input[type="checkbox"]').is(':checked')) {
				flag = true;
			}
			i++;
		})
	})

	/*------------------------------------------------------依据知识点删除题目-----------------------------------------------------------------*/

	$('body').on('click', '.summary-list .btn-remove', function() {
		var url;
		var part = $(this).closest('.section-part');
		var divid = part.find('.q-list').attr('id');
		var knowname = $(this).closest('.summary-item').find('.skill-item').text();

		if(divid == 'singlediv') {
			url = 'transinglekowndelete';
		} else if(divid == 'multiplydiv') {
			url = 'tranmultiplekowndelete';
		} else if(divid == 'judgediv') {
			url = 'tranmovejudgekowndelete';
		} else if(divid == 'filldiv') {
			url = 'tranfillkowndelete';
		} else if(divid == 'shortanswerdiv') {
			url = 'transhortanswerkowndelete';
		}
		$.ajax({
			type: "post",
			url: url,
			data: {
				"knowname": knowname,
			},
			success: function(data) {
				part.find('.q-item').each(function() {
					if($(this).find('.label-skill').eq(1).text() == knowname) {
						$(this).find('.label-skill').eq(1).text('已删除');
						$(this).remove();
					}
				});
				var i = 1;
				part.find(".q-list>li").each(function() {

					$(this).find('.q-sequence').html(i);
					i++;
				})
				$('.alert').attr('class', 'alert');
				$('.alert').html('该知识点题目已删除').addClass('alert-success').show().delay(1000).fadeOut();
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});

	})

	/*------------------------------------------------------页面题目数据变动-------------------------------------------------------------------*/

	$('.q-list').on('DOMNodeInserted', function() {
		var url;
		var part = $(this).closest('.section-part');
		var divid = $(this).attr('id');
		if(divid == 'singlediv') {
			url = 'transinglekown';
		} else if(divid == 'multiplydiv') {
			url = 'tranmultiplekown';
		} else if(divid == 'judgediv') {
			url = 'tranmovejudgekown';
		} else if(divid == 'filldiv') {
			url = 'tranfillkown';
		} else if(divid == 'shortanswerdiv') {
			url = 'transhortanswerkown';
		}
		var itemall = parseInt($('.score-wrap').eq(0).find('.num').text());
		var scoreall = parseInt($('.score-wrap').eq(1).find('.num').text());
		var scores = 0;
		var items=0;
		var a = 0;
		part.find('.summary-item .text-highlight').each(function() {
			if(a % 2 == 0) {
				 items+= parseInt($(this).text());
			}
			else if(a%2==1){
				scores+=parseInt($(this).text());
			}
			a++;
		})
		scoreall -= scores;
		itemall-=items;
		$.ajax({
			type: "post",
			url: url,
			success: function(data) {
				var html = template('summary-listdiv', data);
				part.find('.summary-list').empty();
				part.find('.summary-list').append(html);
				//$('.score-wrap').find('.num').text(data);
				var score = 0;
				var item=0;
				var i = 0;
				part.find('.summary-item .text-highlight').each(function() {
					if(i % 2 == 0) {
						item += parseInt($(this).text());
					}
					else if(i%2==1){
						score+= parseInt($(this).text());
					}
					i++;
				})
				part.find('.text-golden-yellow').eq(1).text(score);
				part.find('.text-golden-yellow').eq(0).text(item);
//				part.find('.part-name-wrap span').text(score);
				$('.score-wrap').eq(1).find('.num').text(scoreall + score);
				$('.score-wrap').eq(0).find('.num').text(itemall + item);
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
	
	$('.q-list').on('DOMNodeRemoved', function() {
		var url;
		var part = $(this).closest('.section-part');
		var divid = $(this).attr('id');
		if(divid == 'singlediv') {
			url = 'transinglekown';
		} else if(divid == 'multiplydiv') {
			url = 'tranmultiplekown';
		} else if(divid == 'judgediv') {
			url = 'tranmovejudgekown';
		} else if(divid == 'filldiv') {
			url = 'tranfillkown';
		} else if(divid == 'shortanswerdiv') {
			url = 'transhortanswerkown';
		}
		var itemall = parseInt($('.score-wrap').eq(0).find('.num').text());
		var scoreall = parseInt($('.score-wrap').eq(1).find('.num').text());
		var scores = 0;
		var items=0;
		var a = 0;
		part.find('.summary-item .text-highlight').each(function() {
			if(a % 2 == 0) {
				 items+= parseInt($(this).text());
			}
			else if(a%2==1){
				scores+=parseInt($(this).text());
			}
			a++;
		})
		scoreall -= scores;
		itemall-=items;
		$.ajax({
			type: "post",
			url: url,
			success: function(data) {
				var html = template('summary-listdiv', data);
				part.find('.summary-list').empty();
				part.find('.summary-list').append(html);
				//$('.score-wrap').find('.num').text(data);
				var score = 0;
				var item=0;
				var i = 0;
				part.find('.summary-item .text-highlight').each(function() {
					if(i % 2 == 0) {
						item += parseInt($(this).text());
					}
					else if(i%2==1){
						score+= parseInt($(this).text());
					}
					i++;
				})
				part.find('.text-golden-yellow').eq(1).text(score);
				part.find('.text-golden-yellow').eq(0).text(item);
//				part.find('.part-name-wrap span').text(score);
				$('.score-wrap').eq(1).find('.num').text(scoreall + score);
				$('.score-wrap').eq(0).find('.num').text(itemall + item);
			},
			error: function(e) {
				$('.alert').attr('class', 'alert');
				$('.alert').html('出错！').addClass('alert-danger').show().delay(1000).fadeOut();
			}
		});
	})
})