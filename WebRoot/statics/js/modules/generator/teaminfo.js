$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'teaminfo/list',
        datatype: "json",
        colModel: [			
			{ label: '编号', name: 'id', index: 'id', width: 30, key: true },
			{ label: '队伍名称', name: 'teamName', index: 'team_name', width: 80 }, 	
			{ label: '运动员集合', name: 'athleteIdSet', index: 'athlete_id_set', width: 80 }, 				
			{ label: '报名方式', name: 'signType', index: 'sign_type', width: 30,
				formatter:function(value,options,rowData){
				     if( value===0 ){
	                    return '团体报名';
	                 }else{
	                     return '个人报名';
	                 }}	
			}, 			
			{ label: '队长姓名', name: 'teamCaptain', index: 'team_captain', width: 80 }, 					
			{ label: '领队姓名', name: 'teamLeader', index: 'team_leader', width: 50 }, 	
			{ label: '联系方式', name: 'teamContact', index: 'team_contact', width: 80 }, 	
			{ label: '单位', name: 'company', index: 'company', width: 80 },
			{ label: '备注', name: 'note', index: 'note', width: 40 }
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
		detailinfo:false,
		showList: true,
		title: null,
		teamInfo: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.teamInfo = {};
		},
		detail: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
			vm.detailinfo = true;
            vm.title = "详情"; 
            $.get(baseURL + "teaminfo/info/"+id, function(r){
                var team = r.teamInfo;
                $("#competition-name").text(team.teamName);
                $("#company").text("报名单位："+team.company);
                $("#leader").html("领队："+team.teamLeader+"&nbsp;&nbsp;&nbsp;&nbsp;联系电话："+team.teamContact);
                $("#captain").text("教练："+team.teamCaptain);
      
                var tableContentStr="<tr><th style='text-align:center;'>身份证号码</th><th style='text-align:center;'>姓名</th><th style='text-align:center;'>报名方式</th></tr>";
                var atheleteSet = team.athleteIdSet;
                var aths= new Array(); //定义一数组 
                aths=atheleteSet.split(";"); //字符分割 
               
                for (i=0;i<aths.length ;i++ ) 
                {   tableContentStr +="<tr>"
                	var athp = new Array();
                	athp = aths[i].split("-");
                	for( j=0;j<athp.length;j++){
                		tableContentStr +="<td>"+athp[j]+"</td>";
                	}
                	
                	tableContentStr+="</tr>"
                }
             
               $("#athSet").html(tableContentStr);
               
            });
            
            
            
		},
		
		saveOrUpdate: function (event) {
			var url = vm.teamInfo.id == null ? "teaminfo/save" : "teaminfo/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.teamInfo),
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
				    url: baseURL + "teaminfo/delete",
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
			$.get(baseURL + "teaminfo/info/"+id, function(r){
                vm.teamInfo = r.teamInfo;
            });
		},
		reload: function (event) {
			vm.showList = true;
			vm.detailinfo = false;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});

function　back(e){
	vm.reload(e);
}
