$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'resultmanage/list',
        datatype: "json",
        colModel: [			
			{ label: '编号', name: 'id', index: 'id', width: 30, key: true },
			{ label: '比赛名称', name: 'resultName', index: 'result_name', width: 80 },
			{ label: '项目名称', name: 'competitionId', index: 'competition_id', width: 80 }, 			
			{ label: '组别', name: 'additionalWord', index: 'additional_word', width: 80 }, 	
			{ label: '附件', name: 'resultFilePath', index: 'result_file_path', width: 80 }, 			
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

function getCompetitionNameData(){
	$.ajax({
		type : "POST",
		url : baseURL + "competitionname/listall",
		contentType : "application/json",
		success : function(r) {
			list = r.list;
			var select = $("#result_id");
			for (c in list) {
				var id = list[c].id;
				var name = list[c].name;
				select.append("<option  value=" + id + ">" + name
						+ "</option>");			
        }
		}
	});
}



function getEventNameData(){
	$.ajax({
		type : "POST",
		url : baseURL + "eventname/listall",
		contentType : "application/json",
		success : function(r) {
			list = r.list;
			var select = $("#competition_id");
			for (c in list) {
				var id = list[c].id;
				var name = list[c].name;
				select.append("<option  value=" + id + ">" + name
						+ "</option>");				
        }
		}
	});
	
}
function getGroupNameData(){
	$.ajax({
		type : "POST",
		url : baseURL + "groupname/listall",
		contentType : "application/json",
		success : function(r) {
			list = r.list;
			var select = $("#additional_word");
			for (c in list) {
				var id = list[c].id;
				var name = list[c].groupName;
				select.append("<option  value=" + name + ">" + name
						+ "</option>");				
        }
		}
	});
}


var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		resultManage: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "上传成绩单";
			vm.resultManage = {};
			getCompetitionNameData();
			getEventNameData();
			getGroupNameData();
			
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.resultManage.id == null ? "resultmanage/save" : "resultmanage/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.resultManage),
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
		upload:function(event){
			
			$("#form1").submit();
			vm.reload();
			
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "resultmanage/delete",
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
			$.get(baseURL + "resultmanage/info/"+id, function(r){
                vm.resultManage = r.resultManage;
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