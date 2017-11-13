$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'registerathlete/list',
        datatype: "json",
        colModel: [			
			{ label: 'idCard', name: 'idCard', index: 'id_card', width: 50, key: true },
			{ label: '注册远动员编号', name: 'registerCode', index: 'register_code', width: 80 }, 			
			{ label: '姓名', name: 'name', index: 'name', width: 80 }, 			
			{ label: '', name: 'gender', index: 'gender', width: 80 }, 			
			{ label: '', name: 'birthday', index: 'birthday', width: 80 }, 			
			{ label: '所属单位ID', name: 'company', index: 'company', width: 80 }			
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
		registerAthlete: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.registerAthlete = {};
		},
		update: function (event) {
			var idCard = getSelectedRow();
			if(idCard == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(idCard)
		},
		saveOrUpdate: function (event) {
			var url = vm.registerAthlete.idCard == null ? "registerathlete/save" : "registerathlete/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.registerAthlete),
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
			var idCards = getSelectedRows();
			if(idCards == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "registerathlete/delete",
				    contentType: "application/json",
				    data: JSON.stringify(idCards),
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
		getInfo: function(idCard){
			$.get(baseURL + "registerathlete/info/"+idCard, function(r){
                vm.registerAthlete = r.registerAthlete;
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