$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'registerathlete/list',
        datatype: "json",
        colModel: [
            { label: '姓名', name: 'name', index: 'name', width: 40 },
			{ label: '身份证', name: 'idCard', index: 'id_card', width: 80, key: true },
			{ label: '注册运动员编号', name: 'registerCode', index: 'register_code', width: 60 }, 					
			{ label: '性别', name: 'gender', index: 'gender', width: 20,
				formatter:function(value,options,rowData){
			     if( value===0 ){
                    return '男';
                 }else{
                     return '女';
                 }}
			}, 			
			{ label: '出生日期', name: 'birthday', index: 'birthday', width: 80,
			formatter:function(value,options,rowData){
			var t = value.replace(" 00:00:00","");
			return t;
			}
			}, 			
			{ label: '所属单位', name: 'company', index: 'company', width: 80 }			
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
    
        new AjaxUpload('#upload', {
        action: baseURL + "registerathlete/uploadexcel",
        name: 'file',
        autoSubmit:true,
        responseType:"json",
        onSubmit:function(file, extension){
           
            if (!(extension && /^(xls|xlsx)$/.test(extension.toLowerCase()))){
                alert('只支持xls、xlsx格式的表格！');
                return false;
            }
        },
        onComplete : function(file, r){
            if(r.code == 0){
              alert('操作成功', function(index){
							vm.reload();
						});
            }else{
                alert(r.msg);
            }
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
			$("#isSave").attr("value","0");
			vm.registerAthlete = {};
		},
		update: function (event) {
			var idCard = getSelectedRow();
			if(idCard == null){
				return ;
			}
			$("#isSave").attr("value","1");
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(idCard)
		},
		saveOrUpdate: function (event) {
		var name = $.trim(vm.registerAthlete['name']);
		var idCard = $.trim(vm.registerAthlete['idCard']);
	    var registerCode= $.trim(vm.registerAthlete['registerCode']);
	    var gender = vm.registerAthlete['gender']+'';
	    alert(gender);
	    var birth= $("#birth").val();
	    var company = $.trim(vm.registerAthlete['company']);

	    if(name == null || name == "") {
						alert("姓名不能为空！");
						return false
					}
					if(name.length > 6 || (!/^[\u0391-\uFFE5]+$/.test(name))) {
						alert("姓名格式不正确！")
						return false
					}
					//身份证号校验
					if(!IdentityCodeValid(idCard)) {
						alert("身份证号格式不正确！")
						return false
					}
					 if(registerCode == null || registerCode == "") {
						alert("注册编号不能为空！");
						return false
					}
					if(gender!='0'&& gender !='1'){
					alert("请选择性别！");
						return false
					}
               if(birth == null || birth == ""|| (!/^[0-9]{4}-[0-9]{2}-[0-9]{2}/.test(birth))) {
						alert("出生日期不正确！");
						return false
					}
	            if(company == null || company == "") {
						alert("单位不能为空！");
						return false
					}
	
			var url = $("#isSave").val() == 0 ? "registerathlete/save" : "registerathlete/update";
            var k = $("#birth").val()+" 00:00:00";
			vm.registerAthlete['birthday'] = k;
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



function IdentityCodeValid(code) {
	var city = {
		11: "北京",
		12: "天津",
		13: "河北",
		14: "山西",
		15: "内蒙古",
		21: "辽宁",
		22: "吉林",
		23: "黑龙江 ",
		31: "上海",
		32: "江苏",
		33: "浙江",
		34: "安徽",
		35: "福建",
		36: "江西",
		37: "山东",
		41: "河南",
		42: "湖北 ",
		43: "湖南",
		44: "广东",
		45: "广西",
		46: "海南",
		50: "重庆",
		51: "四川",
		52: "贵州",
		53: "云南",
		54: "西藏 ",
		61: "陕西",
		62: "甘肃",
		63: "青海",
		64: "宁夏",
		65: "新疆",
		71: "台湾",
		81: "香港",
		82: "澳门",
		91: "国外 "
	};
	var pass = true;

	if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)) {
		pass = false;
	} else if(!city[code.substr(0, 2)]) {
		pass = false;
	} else {
		//18位身份证需要验证最后一位校验位  
		if(code.length == 18) {
			code = code.split('');
			//∑(ai×Wi)(mod 11)  
			//加权因子  
			var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
			//校验位  
			var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
			var sum = 0;
			var ai = 0;
			var wi = 0;
			for(var i = 0; i < 17; i++) {
				ai = code[i];
				wi = factor[i];
				sum += ai * wi;
			}
			var last = parity[sum % 11];
			if(parity[sum % 11] != code[17]) {
				pass = false;
			}
		}
	}
	return pass;
}