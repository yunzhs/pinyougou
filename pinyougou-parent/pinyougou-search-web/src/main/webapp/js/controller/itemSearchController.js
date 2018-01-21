app.controller('itemSearchController',function($scope,itemSearchService){
	
	$scope.searchMap={keywords:''};//条件map
	$scope.resultMap={itemList:[]};//查询结果map
	//搜索
	$scope.search=function(){
		itemSearchService.search($scope.searchMap).success(
				function(response){
					$scope.resultMap=response;
					$scope.resultMap.itemList=response.rows;
				}
		);
	}
	
})
