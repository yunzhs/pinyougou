app.controller('itemSearchController',function($scope,$location,itemSearchService){
	
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':10,'sortField':'','sort':'' };//条件map
	
	//设置参数
	$scope.addToMap=function(key,name){
		if(key=='brand'||key=='category'|| key=='price'){//选择的条件是品牌或者分类
			$scope.searchMap[key]=name;
		}else{
			$scope.searchMap.spec[key]=name;
		}
		$scope.search();
	}
	
	
	
	//移除参数
	$scope.removeFromMap=function(key){
		if(key=='brand'||key=='category'|| key=='price'){//选择的条件是品牌或者分类
			$scope.searchMap[key]='';
		}else{
			delete $scope.searchMap.spec[key];
		}
		$scope.search();
	}
	
	
	
	$scope.resultMap={itemList:[]};//查询结果map
	//搜索
	$scope.search=function(){
        $scope.searchMap.pageNo= parseInt($scope.searchMap.pageNo) ;
		itemSearchService.search($scope.searchMap).success(
				function(response){
					$scope.resultMap=response;
					$scope.resultMap.itemList=response.rows;
					$scope.resultMap.categoryList=response.categoryList;
					$scope.resultMap.brandList=response.brandList;
					$scope.resultMap.specList=response.specList;
                    buildPageLabel();//调用
				}
		);
	}
    buildPageLabel=function(){
        $scope.pageLabel=[];//新增分页栏属性
        var maxPageNo= $scope.resultMap.totalPages;//得到最后页码
        var firstPage=1;//开始页码
        var lastPage=maxPageNo;//截止页码
        $scope.firstDot=true;//前面有点
        $scope.lastDot=true;//后边有点
        if($scope.resultMap.totalPages> 5){  //如果总页数大于5页,显示部分页码
            if($scope.searchMap.pageNo<=3){//如果当前页小于等于3
                lastPage=5; //前5页
                $scope.firstDot=false;//前面没点
            }else if( $scope.searchMap.pageNo>=lastPage-2  ){//如果当前页大于等于最大页码-2
                firstPage= maxPageNo-4;		 //后5页
                $scope.lastDot=false;//后边没点
            }else{ //显示当前页为中心的5页
                firstPage=$scope.searchMap.pageNo-2;
                lastPage=$scope.searchMap.pageNo+2;
            }
        }
        else {
            $scope.firstDot=false;//前面无点
            $scope.lastDot=false;//后边无点
        }
        //循环产生页码标签
        for(var i=firstPage;i<=lastPage;i++){
            $scope.pageLabel.push(i);
        }
    }
    $scope.queryByPage=function(pageNo){
        //页码验证
        if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
            return;
        }
        $scope.searchMap.pageNo=pageNo;
        $scope.search();
    }
    //判断当前页为第一页
    $scope.isTopPage=function(){
        if($scope.searchMap.pageNo==1){
            return true;
        }else{
            return false;
        }
    }

    //判断当前页是否未最后一页
    $scope.isEndPage=function(){
        if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
            return true;
        }else{
            return false;
        }
    }
    //设置排序规则
    $scope.sortSearch=function(sortField,sort){
        $scope.searchMap.sortField=sortField;
        $scope.searchMap.sort=sort;
        $scope.search();
    }
    //判断关键字是不是品牌
    $scope.keywordsIsBrand=function(){
        for(var i=0;i<$scope.resultMap.brandList.length;i++){
            if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){//如果包含
                return true;
            }
        }
        return false;
    }

    //加载查询字符串
    $scope.loadkeywords=function(){
        $scope.searchMap.keywords=  $location.search()['keywords'];
        $scope.search();
    }
	
})
