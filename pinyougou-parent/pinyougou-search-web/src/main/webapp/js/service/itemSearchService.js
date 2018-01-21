app.service('itemSearchService',function($http){
	//搜索
	this.search=function(searchMap){
		return $http.post('../item/search.do',searchMap);
	}
})