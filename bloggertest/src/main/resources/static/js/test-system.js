

//上一题下一题
var nums = $(".main .single-box").length;
		var num = 0;//记录当前答题的题号是几，默认是1
		alert(nums);
		//跳下一题
		function questionsAdd(){
			if(num < nums-1){
				num = num+1;
				for(var i = 0; i<nums; i++){
					if(num == i){
						$(".main").children().eq(num).css("display","block");
					}else{
						$(".main").children().eq(i).css("display","none");
					}
				}
			}
			
		}
		//跳上一题
		function questionsDel(){
			if(num > 0){
				num = num-1;
				for(var i = 0; i<nums; i++){
					if(num == i){
						$(".main").children().eq(num).css("display","block");
					}else{
						$(".main").children().eq(i).css("display","none");
					}
				}
			}
			
		}	
		


//判断题选中事件
$(document).ready(function(){
						$("input[name='optionsRadios1']").change(function(){
							$("input[name='optionsRadios1']:checked").each(function() { // 遍历name=test的多选框
								// 每一个被选中项的值$(this).attr("id")
								$("input[name='optionsRadios1']").each(function() { // 遍历name=test的多选框
									// 每一个被选中项的值$(this).attr("id")
									$(this).parent().css("border","1px solid #ccc");
									$(this).parent().css("background-color","#fff");
								});
								$(this).parent().css("border","1px solid #66ccff");
								$(this).parent().css("background-color","#f9f9f9");
							});
						});
					});
					

//单选题选中事件
$(document).ready(function(){
						$("input[name='optionsRadios2']").change(function(){
							$("input[name='optionsRadios2']:checked").each(function() { // 遍历name=test的多选框
								// 每一个被选中项的值$(this).attr("id")
								$("input[name='optionsRadios2']").each(function() { // 遍历name=test的多选框
									// 每一个被选中项的值$(this).attr("id")
									$(this).parent().css("border","1px solid #ccc");
									$(this).parent().css("background-color","#fff");
								});
								$(this).parent().css("border","1px solid #66ccff");
								$(this).parent().css("background-color","#f9f9f9");
							});
						});
					});
					

//多选题选中事件
$(document).ready(function(){
						$("input[name='optionsRadios3']").change(function(){
							$("input[name='optionsRadios3']").each(function() { // 遍历name=test的多选框
								// 每一个被选中项的值$(this).attr("id")
								$(this).parent().css("border","1px solid #ccc");
								$(this).parent().css("background-color","#fff");
							});
							$("input[name='optionsRadios3']:checked").each(function() { // 遍历name=test的多选框
								// 每一个被选中项的值$(this).attr("id")
								$(this).parent().css("border","1px solid #66ccff");
								$(this).parent().css("background-color","#f9f9f9");
							});
						});
					});