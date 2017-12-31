$(function() {
	$("#jqGrid").jqGrid({
		url : baseURL + 'competitioninfo/list',
		datatype : "json",
		colModel : [ {
			label : '编号',
			name : 'id',
			index : 'id',
			width : 30,
			key : true
		}, {
			label : '赛事名',
			name : 'competitionNameEntity',
			index : 'competition_id',
			width : 80,
			formatter : function(value, options, rowData) {

				return value.name;
			}
		}, {
			label : '项目名',
			name : 'eventNameEntity',
			index : 'event_id',
			width : 80,
			formatter : function(value, options, rowData) {

				return value.name;
			}
		}, {
			label : '组别名',
			name : 'groupNameEntity',
			index : 'group_id',
			width : 80,
			formatter : function(value, options, rowData) {

				return value.groupName;
			}
		}, {
			label : '报名方式',
			name : 'signTypeEntity',
			index : 'sign_type',
			width : 80,
			formatter : function(value, options, rowData) {

				return value.name;
			}

		}, {
			label : '备注',
			name : 'note',
			index : 'note',
			width : 80
		}
		],
		viewrecords : true,
		height : 385,
		rowNum : 10,
		rowList : [ 10, 30, 50 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		multiselect : true,
		pager : "#jqGridPager",
		jsonReader : {
			root : "page.list",
			page : "page.currPage",
			total : "page.totalPage",
			records : "page.totalCount"
		},
		prmNames : {
			page : "page",
			rows : "limit",
			order : "order"
		},
		gridComplete : function() {
			// 隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		}
	});
});
function getCompetitionNameData(cId){
	$.ajax({
		type : "POST",
		url : baseURL + "competitionname/listall",
		contentType : "application/json",
		success : function(r) {
			list = r.list;
			$("#scompetition_id").empty();
			var select = $("#scompetition_id");
			for (c in list) {
				var id = list[c].id;
				var name = list[c].name;
				if(parseInt(id)==parseInt(cId)){
					select.append("<option  value=" + id +" selected='selected'"+ ">" + name
							+ "</option>");
					}
					else{
					select.append("<option  value=" + id + ">" + name
							+ "</option>");
							}		
        }
		}
	});
}



function getEventNameData(cId){
	$.ajax({
		type : "POST",
		url : baseURL + "eventname/listall",
		contentType : "application/json",
		success : function(r) {
			list = r.list;
			$("#sevent_id").empty();
			var select = $("#sevent_id");
			for (c in list) {
				var id = list[c].id;
				var name = list[c].name;
				if(parseInt(id)==parseInt(cId)){
					select.append("<option  value=" + id +" selected='selected'"+ ">" + name
							+ "</option>");
					}
					else{
					select.append("<option  value=" + id + ">" + name
							+ "</option>");
							}
							
        }
		}
	});
	
}
function getGroupNameData(cId){
	$.ajax({
		type : "POST",
		url : baseURL + "groupname/listall",
		contentType : "application/json",
		success : function(r) {
			list = r.list;
			$("#sgroup_id").empty();
			var select = $("#sgroup_id");
			for (c in list) {
				var id = list[c].id;
				var name = list[c].groupName;
				if(parseInt(id)==parseInt(cId)){
					select.append("<option  value=" + id +" selected='selected'"+ ">" + name
							+ "</option>");
					}
					else{
					select.append("<option  value=" + id + ">" + name
							+ "</option>");
							}
						
        }
		}
	});
}
function getSignTypeData(cId){
	$.ajax({
		type : "POST",
		url : baseURL + "signtype/list",
		contentType : "application/json",
		success : function(r) {
			var list =r.list;
			$("#ssign_type").empty();
			var select = $("#ssign_type");
			for (c in list) {
				var id = list[c].id;
				var name = list[c].name;
				if(parseInt(id)==parseInt(cId)){
					select.append("<option  value=" + id +" selected='selected'"+ ">" + name
							+ "</option>");
					}
					else{
					select.append("<option  value=" + id + ">" + name
							+ "</option>");
							}			
        }
		}
	});
}

var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		title : null,
		competitionInfo : {}
	},
	methods : {
		query : function() {
			vm.reload();
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.competitionInfo = {};
			getCompetitionNameData();
			getEventNameData();
			getGroupNameData();
			getSignTypeData();
		},
		update : function(event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";

			$.get(baseURL + "competitioninfo/info/" + id, function(r) {
				vm.competitionInfo = r.competitionInfo;
				getCompetitionNameData(vm.competitionInfo.competitionNameEntity.id);
				getEventNameData(vm.competitionInfo.eventNameEntity.id);
				getGroupNameData(vm.competitionInfo.groupNameEntity.id);
				getSignTypeData(vm.competitionInfo.signTypeEntity.id);
			});
			
			
		},
		saveOrUpdate : function(event) {
			var url = vm.competitionInfo.id == null ? "competitioninfo/save"
					: "competitioninfo/update";
			vm.competitionInfo['competitionId']=$("#scompetition_id").val();
			vm.competitionInfo['eventId']=$("#sevent_id").val();
			vm.competitionInfo['groupId']=$("#sgroup_id").val();
			vm.competitionInfo['signType']=$("#ssign_type").val();
			
			$.ajax({
				type : "POST",
				url : baseURL + url,
				contentType : "application/json",
				data : JSON.stringify(vm.competitionInfo),
				success : function(r) {
					if (r.code === 0) {
						alert('操作成功', function(index) {
							vm.reload();
						});
					} else {
						alert(r.msg);
					}
				}
			});
		},
		del : function(event) {
			var ids = getSelectedRows();
			if (ids == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : baseURL + "competitioninfo/delete",
					contentType : "application/json",
					data : JSON.stringify(ids),
					success : function(r) {
						if (r.code == 0) {
							alert('操作成功', function(index) {
								$("#jqGrid").trigger("reloadGrid");
							});
						} else {
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo : function(id) {
			$.get(baseURL + "competitioninfo/info/" + id, function(r) {
				vm.competitionInfo = r.competitionInfo;
				
			});
		},
		reload : function(event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				page : page
			}).trigger("reloadGrid");
		}
	}
});