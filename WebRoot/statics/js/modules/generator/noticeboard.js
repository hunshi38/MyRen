$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'noticeboard/list',
        datatype: "json",
        colModel: [			
			{ label: '编号', name: 'id', index: 'id', width: 20, key: true},
			{ label: '标题', name: 'title', index: 'title', width: 200 }, 			
			{ label: '发布时间', name: 'createTime', index: 'create_time', width: 80 }, 						
			{ label: '浏览量', name: 'readNumber', index: 'read_number', width: 20 }, 			
			{ label: '赛事名', name: 'cId', index: 'c_id', width: 20 }			
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

function addArticle() {
 
	var title = $("#title").val();
	if (title == null || title.length == 0 || title.length > 40) {
		layer.msg('标题长度不正确[1-40]',{time:2000});
		return false;
	}
	
	var cid = $("#cid").val();
	if (cid == null) {
		layer.msg('栏目不能为空', {time : 2000});
		return false;
	}
	
	
	var html =  UE.getEditor('content').getContent();
	alert(html);
	var contentPlain = ue.getContentTxt();
	if (contentPlain == null || contentPlain.length == 0) {
		layer.msg('内容不能为空', {time : 2000});
		return false;
	}
	var da = {title : title, cid : cid, content : html};
	
	$.ajax({
    	url: baseURL + 'noticeboard/save',
    	type: 'POST',
    	data: da,
    	dataType: 'json'
		}).done(function(r) {
			if(r.code === 0){
				alert('操作成功', function(index){
					vm.reload();
				});
			}else{
				alert(r.msg);
			}
		});

	return true;
}
function getCompetitonNameData(){
	$("#title").val("");
	$("#content").val("");
	var ue = UE.getEditor('content');
	ue.setContent("");
    $.ajax({
		type: "POST",
	    url: baseURL + "competitionname/listall",
	    contentType: "application/json",
	    success: function(r){
	    	list = r.list;
	    	var select =$("#cid");
			for(c in list) {
				var id = list[c].id;
				var name = list[c].name;
				select.append("<option value="+id+">"+name+"</option>");
				}
			
		}
	});
}			


var vm = new Vue({
	el:'#rrapp',
	data:{
		hideList:false,
		showList: true,
		announce:false,
		title: null,
		noticeBoard: {}
		
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.hideList = false;
			vm.announce = true;
			vm.title = "发布";
			getCompetitonNameData();
		},
		
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
			vm.hideList =true;
            vm.title = "详情";
            vm.getInfo(id);
            data:{
				contentHtml:vm.content
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
				    url: baseURL + "noticeboard/delete",
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
		 vm.showList = false;
			$.get(baseURL + "noticeboard/info/"+id, function(r){
                vm.noticeBoard = r.noticeBoard;
            });
		},
		reload: function (event) {
			vm.showList = true;
			vm.hideList = false;
			vm.announce = false;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});