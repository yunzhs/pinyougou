app.controller('baseController',function($scope){
	// 分页控件配置
	$scope.paginationConf = {
		currentPage : 1,// 当前页
		totalItems : 10,// 总记录数
		itemsPerPage : 10,// 每页显示多少条
		perPageOptions : [ 10, 20, 30, 40, 50 ],// 每页显示条数选项
		onChange : function() {// 当分页参数改变时，方法就会触发
			$scope.reloadList();// 重新加载
		}
	};

	$scope.reloadList = function() {
		$scope.search($scope.paginationConf.currentPage,
				$scope.paginationConf.itemsPerPage);
	}
	
	$scope.selectIds = [];// id数组
	// 操作id数组
	$scope.updateSelection = function($event, id) {
		if ($event.target.checked) {// 选中时push值
			$scope.selectIds.push(id);
		} else {// 否则，移除值
			var index = $scope.selectIds.indexOf(id);
			// splice 参数1:对象在数组中的位置 参数2：移除的个数
			$scope.selectIds.splice(index, 1);
		}
	}
	
	//json转化字符串
	$scope.jsonToString=function(jsonString,key){
		var value="";
		var json = JSON.parse(jsonString);
		for(var i=0;i<json.length;i++){
			if(i!=0){
				value+=",";
			}
			value+=json[i][key];
		}
		return value;
	}
	
});