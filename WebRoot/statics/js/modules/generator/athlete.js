$(function() {
	$("#jqGrid").jqGrid({
		url : baseURL + 'athlete/list',
		datatype : "json",
		colModel : [ {
			label : '编号',
			name : 'id',
			index : 'id',
			width : 30,
			key : true
		}, {
			label : '身份证',
			name : 'idCard',
			index : 'id_card',
			width : 100
		}, {
			label : '注册运动员编号',
			name : 'registerCode',
			index : 'register_code',
			width : 40
		}, {
			label : '姓名',
			name : 'name',
			index : 'name',
			width : 40
		}, {
			label : '性别',
			name : 'gender',
			index : 'gender',
			width : 25,
			formatter : function(value, options, rowData) {
				if (value === 0) {
					return '男';
				} else {
					return '女';
				}
			}
		}, {
			label : '出生日期',
			name : 'birthday',
			index : 'birthday',
			width : 90,
			formatter : function(value, options, rowData) {
				var t = value.replace(" 00:00:00", "");
				return t;
			}
		}, {
			label : '所属单位',
			name : 'company',
			index : 'company',
			width : 80
		}, {
			label : '电话',
			name : 'phoneNumber',
			index : 'phone_number',
			width : 70
		}, {
			label : '球衣号码',
			name : 'jerseyNumber',
			index : 'jersey_number',
			width : 30
		}, {
			label : '带训教练',
			name : 'coach',
			index : 'coach',
			width : 40
		}, {
			label : '备注',
			name : 'note',
			index : 'note',
			width : 30
		} ],
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
			//隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		}
	});
});

var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		title : null,
		athlete : {}
	},
	methods : {
		query : function() {
			vm.reload();
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			$("#isSave").attr("value", "0");
			vm.athlete = {};
		},
		update : function(event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";
			$("#isSave").attr("value", "1");
			vm.getInfo(id)
		},
		saveOrUpdate : function(event) {
			var idCard = $.trim(vm.athlete['idCard']);
			if (!valiIdCard(idCard)) {
				return;
			}

			// 运动员编号校验
			var registerCode = $.trim(vm.athlete['registerCode']);
			if (!validateregisterCode(registerCode)) {
				return;
			}
			
			// 姓名校验
			var name = $.trim(vm.athlete['name']);
			if (!validatename(name)) {
				return;
			}

			// 性别校验
			var gender = vm.athlete['gender'] + '';
			if (!validategender(gender)) {
				return;
			}

			// 校验出生日期
			var birth = $("#birth").val();
			if (!validatebirthday(birth)) {
				return;
			}
			//检验所属单位
			var company =$.trim(vm.athlete['company']);
			if (!validatecompany(company)) {
				return;
			}

			// 校验手机号
			var mobile = $.trim(vm.athlete['phoneNumber']);
			if (!validatemobile(mobile)) {
				return;
			}
			
			//检验秋衣号码
			var jerseyNumber = $.trim(vm.athlete['jerseyNumber']);
			if (!validatejerseyNumber(jerseyNumber)) {
				return;
			}
			
			//检验教练
			var coach = $.trim(vm.athlete['coach']);
			if (!validatecoach(coach)) {
				return;
			}
			
			var url = $("#isSave").val() == 0 ? "athlete/save"
					: "athlete/update";
			var k = $("#birth").val() + " 00:00:00";
			vm.athlete['birthday'] = k;
			
			$.ajax({
				type : "POST",
				url : baseURL + url,
				contentType : "application/json",
				data : JSON.stringify(vm.athlete),
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
					url : baseURL + "athlete/delete",
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
			$.get(baseURL + "athlete/info/" + id, function(r) {
				vm.athlete = r.athlete;
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

//手机号验证
function validatemobile(mobile) {
	if (mobile.length == 0) {
		alert('手机号码不能为空！');
		return false;
	}
	if (mobile.length != 11) {
		alert('请输入有效的手机号码，需是11位！');
		return false;
	}

	var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	if (!myreg.test(mobile)) {
		alert('请输入有效的手机号码！');
		return false;
	}
	return true;
}

// 校验运动员编号
function validateregisterCode(obj) {
	if (obj == "" || obj == null) {
		alert('运动员编号不能为空!');
		return false;
	}
	return true;
}

// 检验姓名
function validatename(obj) {
	if (obj == "" || obj == null) {
		alert('姓名不能为空!');
		return false;
	}
	var reg = /^[\u0391-\uFFE5]+$/;
	if (!reg.test(obj)) {
		alert('必须输入中文！');
		return false;
	}
	return true;
}

// 校验身份证
// 构造函数，变量为15位或者18位的身份证号码
function clsIDCard(CardNo) {
	this.Valid = false;
	this.ID15 = '';
	this.ID18 = '';
	this.Local = '';
	if (CardNo != null)
		this.SetCardNo(CardNo);
}

// 设置身份证号码，15位或者18位
clsIDCard.prototype.SetCardNo = function(CardNo) {
	this.ID15 = '';
	this.ID18 = '';
	this.Local = '';
	CardNo = CardNo.replace(" ", "");
	var strCardNo;
	if (CardNo.length == 18) {
		pattern = /^\d{17}(\d|x|X)$/;
		if (pattern.exec(CardNo) == null)
			return;
		strCardNo = CardNo.toUpperCase();
	} else {
		pattern = /^\d{15}$/;
		if (pattern.exec(CardNo) == null)
			return;
		strCardNo = CardNo.substr(0, 6) + '19' + CardNo.substr(6, 9)
		strCardNo += this.GetVCode(strCardNo);
	}
	this.Valid = this.CheckValid(strCardNo);
}

// 校验身份证有效性
clsIDCard.prototype.IsValid = function() {
	return this.Valid;
}

// 返回生日字符串，格式如下，1981-10-10
clsIDCard.prototype.GetBirthDate = function() {
	var BirthDate = '';
	if (this.Valid)
		BirthDate = this.GetBirthYear() + '-' + this.GetBirthMonth() + '-'
				+ this.GetBirthDay();
	return BirthDate;
}

// 返回生日中的年，格式如下，1981
clsIDCard.prototype.GetBirthYear = function() {
	var BirthYear = '';
	if (this.Valid)
		BirthYear = this.ID18.substr(6, 4);
	return BirthYear;
}

// 返回生日中的月，格式如下，10
clsIDCard.prototype.GetBirthMonth = function() {
	var BirthMonth = '';
	if (this.Valid)
		BirthMonth = this.ID18.substr(10, 2);
	if (BirthMonth.charAt(0) == '0')
		BirthMonth = BirthMonth.charAt(1);
	return BirthMonth;
}

// 返回生日中的日，格式如下，10
clsIDCard.prototype.GetBirthDay = function() {
	var BirthDay = '';
	if (this.Valid)
		BirthDay = this.ID18.substr(12, 2);
	return BirthDay;
}

// 返回性别，1：男，0：女
clsIDCard.prototype.GetSex = function() {
	var Sex = '';
	if (this.Valid)
		Sex = this.ID18.charAt(16) % 2;
	return Sex;
}

// 返回15位身份证号码
clsIDCard.prototype.Get15 = function() {
	var ID15 = '';
	if (this.Valid)
		ID15 = this.ID15;
	return ID15;
}

// 返回18位身份证号码
clsIDCard.prototype.Get18 = function() {
	var ID18 = '';
	if (this.Valid)
		ID18 = this.ID18;
	return ID18;
}

// 返回所在省，例如：上海市、浙江省
clsIDCard.prototype.GetLocal = function() {
	var Local = '';
	if (this.Valid)
		Local = this.Local;
	return Local;
}

clsIDCard.prototype.GetVCode = function(CardNo17) {
	var Wi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1);
	var Ai = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
	var cardNoSum = 0;
	for ( var i = 0; i < CardNo17.length; i++)
		cardNoSum += CardNo17.charAt(i) * Wi[i];
	var seq = cardNoSum % 11;
	return Ai[seq];
}

clsIDCard.prototype.CheckValid = function(CardNo18) {
	if (this.GetVCode(CardNo18.substr(0, 17)) != CardNo18.charAt(17))
		return false;
	if (!this.IsDate(CardNo18.substr(6, 8)))
		return false;
	var aCity = {
		11 : "北京",
		12 : "天津",
		13 : "河北",
		14 : "山西",
		15 : "内蒙古",
		21 : "辽宁",
		22 : "吉林",
		23 : "黑龙江 ",
		31 : "上海",
		32 : "江苏",
		33 : "浙江",
		34 : "安徽",
		35 : "福建",
		36 : "江西",
		37 : "山东",
		41 : "河南",
		42 : "湖北 ",
		43 : "湖南",
		44 : "广东",
		45 : "广西",
		46 : "海南",
		50 : "重庆",
		51 : "四川",
		52 : "贵州",
		53 : "云南",
		54 : "西藏 ",
		61 : "陕西",
		62 : "甘肃",
		63 : "青海",
		64 : "宁夏",
		65 : "新疆",
		71 : "台湾",
		81 : "香港",
		82 : "澳门",
		91 : "国外"
	};
	if (aCity[parseInt(CardNo18.substr(0, 2))] == null)
		return false;
	this.ID18 = CardNo18;
	this.ID15 = CardNo18.substr(0, 6) + CardNo18.substr(8, 9);
	this.Local = aCity[parseInt(CardNo18.substr(0, 2))];
	return true;
}

clsIDCard.prototype.IsDate = function(strDate) {
	var r = strDate.match(/^(\d{1,4})(\d{1,2})(\d{1,2})$/);
	if (r == null)
		return false;
	var d = new Date(r[1], r[2] - 1, r[3]);
	return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[2] && d
			.getDate() == r[3]);
}

function valiIdCard(idCard) {
	var checkFlag = new clsIDCard(idCard);
	if (!checkFlag.IsValid()) {
		alert("输入的身份证号无效,请输入真实的身份证号！");
		document.getElementByIdx("idCard").focus();
		return false;
	}
	return true;
}

function validategender(obj) {
	if (!(obj == '1' || obj == '0')) {
		//console.log(obj);
		alert('性别不能为空!');
		return false;
	}
	return true;
}

function validatebirthday(birth) {
	if (birth == null || birth == ""
			|| (!/^[0-9]{4}-[0-9]{2}-[0-9]{2}/.test(birth))) {
		alert("出生日期不能为空！");
		return false
	}
	return true;
}

//校验运动员编号
function validatecompany(obj) {
	if (obj == "" || obj == null) {
		alert('运动员单位不能为空!');
		return false;
	}
	return true;
}

//校验运动员编号
function validatecoach(obj) {
	if (obj == "" || obj == null) {
		alert('运动员教练不能为空!');
		return false;
	}
	return true;
}

//校验运动员编号
function validatejerseyNumber(obj) {
	if (obj == "" || obj == null) {
		alert('运动员球衣号码不能为空!');
		return false;
	}
	return true;
}

function validategender(obj) {
	if (!(obj == '1' || obj == '0')) {
		//console.log(obj);
		alert('性别不能为空!');
		return false;
	}
	return true;
}

