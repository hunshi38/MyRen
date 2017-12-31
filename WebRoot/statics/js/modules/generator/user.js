$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/list',
        datatype: "json",
        colModel: [			
			{ label: '编号', name: 'id', index: 'id', width: 30, key: true },
			{ label: '账户名', name: 'userName', index: 'user_name', width: 80 }, 			
			{ label: '密码', name: 'password', index: 'password', width: 80 }, 			
			{ label: '0 超级管理员  1系统管理员 2裁判 3区县管理员（竞技报名） 4其他报名人员', name: 'userType', index: 'user_type', width: 80 }, 			
			{ label: '备注', name: 'note', index: 'note', width: 80 }, 			
			{ label: 'ut', name: 'ut', index: 'ut', width: 80 },
			{ label: 'md5', name: 'md5', index: 'md5', width: 80 }
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
function getUserTypeData(cId){
	$.ajax({
		type : "POST",
		url : baseURL + "usertype/list",
		contentType : "application/json",
		success : function(r) {
			var list =r.list;
			$("#ssign_type").empty();
			var select = $("#ssign_type");
			for (c in list) {
				var id = list[c].id;
				var name = list[c].typeName;
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
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		user: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.user = {};
			getUserTypeData();
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            $.get(baseURL + "user/info/"+id, function(r){
                vm.user = r.user;
                getUserTypeData(vm.user.userType)
            });
		},
		saveOrUpdate: function (event) {
			//检验用户名
			var userName = $.trim(vm.user['userName']);
			if (!validateuserName(userName)) {
				return;
			}
			//检验密码
			var password = $.trim(vm.user['password']);
			if (!validatepassword(password)) {
				return;
			}
			//检验角色
			var userType = $.trim(vm.user['userType']);
			if (!validateuserType(userType)) {
				return;
			}
			
			var url = vm.user.id == null ? "user/save" : "user/update";
			var pass=vm.user['password'];
			var  md5= hex_md5(pass);
			vm.user['md5'] = md5;
			vm.user['userType'] = $("#ssign_type").val();
			
	   	$.ajax({
				type: "POST",
			    url: baseURL + url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.user),
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
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "user/delete",
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
			$.get(baseURL + "user/info/"+id, function(r){
                vm.user = r.user;
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

//校验用户名
function validateuserName(obj) {
	if (obj == "" || obj == null) {
		alert('用户名不能为空!');
		return false;
	}
	return true;
}
//校验密码
function validatepassword(obj) {
	if (obj == "" || obj == null) {
		alert('密码不能为空!');
		return false;
	}
	return true;
}
//校验用户角色
function validateuserType(obj) {
	if (obj == "" || obj == null) {
		alert('用户角色不能为空!');
		return false;
	}
	if (!(obj == 0||obj == 1 || obj == 2||obj == 3||obj == 4)) {
		alert("用户角色不正确！")
		return false;
	}
	return true;
}

