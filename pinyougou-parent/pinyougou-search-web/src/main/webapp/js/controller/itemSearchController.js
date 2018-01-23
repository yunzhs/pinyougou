app.controller('itemSearchController',function($scope,itemSearchService){
	
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{}};//条件map
	
	//设置参数
	$scope.addToMap=function(key,name){
		if(key=='brand'||key=='category'){//选择的条件是品牌或者分类
			$scope.searchMap[key]=name;
		}else{
			$scope.searchMap.spec[key]=name;
		}
		$scope.search();
	}
	
	
	
	//移除参数
	$scope.removeFromMap=function(key){
		if(key=='brand'||key=='category'){//选择的条件是品牌或者分类
			$scope.searchMap[key]='';
		}else{
			delete $scope.searchMap.spec[key];
		}
		$scope.search();
	}
	
	
	
	$scope.resultMap={itemList:[]};//查询结果map
	//搜索
	$scope.search=function(){
		itemSearchService.search($scope.searchMap).success(
				function(response){
					$scope.resultMap=response;
					$scope.resultMap.itemList=response.rows;
					$scope.resultMap.categoryList=response.categoryList;
					$scope.resultMap.brandList=response.brandList;
					$scope.resultMap.specList=response.specList;
					
				}
		);
	}
	
})
