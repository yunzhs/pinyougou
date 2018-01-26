app.controller('itemPageController',function($scope){

	$scope.addNum=function(x){
		$scope.num=parseInt($scope.num)+x;
		if($scope.num<1){
			$scope.num=1;
		}
	}

	$scope.specificationItems={};//保存用户选择的规格
	
	
	//选择的规格
	$scope.selectSpecification=function(name,value){
		$scope.specificationItems[name]=value;
		$scope.searchSku();
	}
	
	
	$scope.isSelected=function(name,value){
		if($scope.specificationItems[name]==value){
			return true;
		}else{
			return false;
		}
	}

	$scope.sku={};//当前SKU
	
	$scope.loadSku=function(){
		$scope.sku=itemList[0];
		$scope.specificationItems=JSON.parse(JSON.stringify(itemList[0].spec));
	}
	
	
	//比较两个json对象的值是否相等
	$scope.matchObject=function(map1,map2){
		for(var k in map1){
			if(map1[k]!=map2[k]){
				return false
			}
		}
		
		for(var k in map2){
			if(map1[k]!=map2[k]){
				return false
			}
		}
		return true;
	}
	//按选中的规格查找当前的SKU
	$scope.searchSku=function(){
		for(var i=0;i<itemList.length;i++){
			//alert(JSON.stringify(itemList[i]));
			if($scope.matchObject(itemList[i].spec,$scope.specificationItems)){
				
				
				$scope.sku=itemList[i];
				return;
			}
		}
		$scope.sku={'id':'-----','title':'-----','price':0,'spec':[]}
	}
	
	
	$scope.addToCart=function(){
		alert($scope.sku.id+"--"+$scope.num);
	}
})