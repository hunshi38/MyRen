$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'competitionname/list',
        datatype: "json",
        colModel: [			
			{ label: '编号', name: 'id', index: 'id', width: 30, key: true },
			{ label: '赛事名称', name: 'name', index: 'name', width: 160 }, 			
			{ label: '类型', name: 'type', index: 'type', width: 30 ,
				formatter:function(value,options,rowData){
				     if( value===0 ){
	                    return '竞技赛事';
	                 }else{
	                     return '群众赛事';
	                 }}
				
			}, 			
			{ label: '报名开始时间', name: 'signStartDate', index: 'sign_start_date', width: 60 }, 			
			{ label: '报名截止时间', name: 'signEndDate', index: 'sign_end_date', width: 60 }, 			
			{ label: '限制报名账号', name: 'limitAccount', index: 'limit_account', width: 60 }, 			
			{ label: '备注', name: 'note', index: 'note', width: 40 }	,
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		competitionName: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			 $("#isSave").attr("value","0");
			  $("#d_upload").removeAttr("style");
			vm.competitionName = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            $("#isSave").attr("value","1");
            $("#d_upload").attr("style","display:none");
            vm.getInfo(id)
		},
		saveOrUpdate: function () {
			var url = $("#isSave").val() == 0  ? "competitionname/save" : "competitionname/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.competitionName),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		download:function(event){
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			$.ajax({
				async:false,
				type: "get",
			    url: baseURL + "competitionname/info/"+id,
			    contentType: "application/json",
			    success: function(r){
			    	vm.competitionName = r.competitionName;
		               var filename=vm.competitionName.signTablePath;	
		               var url = baseURL+'competitionname/download/model';
		               var form = $("<form></form>").attr("action", url).attr("method", "post").attr("accept-charset","utf-8").attr("onsubmit","document.charset='utf-8'");
		               form.append($("<input></input>").attr("type", "hidden").attr("name", "filename").attr("value", filename));
		               form.append($("<input></input>").attr("type", "hidden").attr("name", "source").attr("value", "ajax"));
		               form.appendTo('body').submit().remove();
			    }
			});
	
			
		},
		upload:function(event){
			var name = $.trim(vm.competitionName['name']);
			//alert(name);
			if (!validatename(name)) {
				return;
			}
			//检验赛事类型
			var type = $.trim(vm.competitionName['type']);
			if (!validatetype(type)) {
				return;
			}	
			
			//检验赛事开始时间
			var signStartDate = $.trim(vm.competitionName['signStartDate']);
			if (!validatesignStartDate(signStartDate)) {
				return;
			}	
			//检验赛事结束时间
			var signEndDate = $.trim(vm.competitionName['signEndDate']);
			if (!validatetype(signEndDate)) {
				return;
			}	
			var isSave = $("#isSave").val();//0 is save,1 is update
			if(isSave == 1){
				vm.saveOrUpdate();
			}else{
			$("#form1").submit();
			   vm.reload();
			}
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "competitionname/delete",
				    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get(baseURL + "competitionname/info/"+id, function(r){
                vm.competitionName = r.competitionName;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});

//校验赛事名称
function validatename(obj) {
	if (obj == "" || obj == null) {
		alert('赛事名称不能为空!');
		return false;
	}
	return true;
}
//校验赛事类型
function validatetype(obj) {
	if (obj == "" || obj == null) {
		alert('赛事类型不能为空!');
		return false;
	}
	return true;
}



function validatesignStartDate(birth) {
	if (birth == null || birth == ""
			|| (!/^[0-9]{4}\/[0-9]{2}\/[0-9]{2}/.test(birth))) {
		alert("日期不能为空！");
		return false
	}
	return true;
}
function validatesignEndDate(birth) {
	if (birth == null || birth == ""
			|| (!/^[0-9]{4}\/[0-9]{2}\/[0-9]{2}/.test(birth))) {
		alert("日期不能为空！");
		return false
	}
	return true;
}
