app.controller('brandController', function ($scope,$controller, $http, brandService) {
    /*$scope.findAll=function () {
        $http.get('../brand/findAll.do').success(
            function (response) {
                $scope.list=response;
            }
        )
    }*/
    $controller('baseController',{$scope:$scope}); //将basecontroller的变量和方法通过scope继承过来

    //分页
    $scope.findPage = function (page, rows) {
        brandService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;//品牌分页当前页数据
                $scope.paginationConf.totalItems = response.total;//接受总页数
                //接受总页数
            }
        )
    };
    $scope.entity = {};

    //添加
    $scope.add = function () {
        brandService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    alert(response.message)
                    $scope.reloadList();
                }
                else {
                    alert(response.message)
                }
            }
        )
    }
    //根据id获得一个品牌对象
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;//接收对象
            }
        )
    }
    //品牌修改
    $scope.update = function () {
        brandService.update($scope.entity).success(
            function (response) {
                if (response.success) {
                    alert(response.message)
                    $scope.reloadList();
                }
                else {
                    alert(response.message)
                }
            }
        )
    };
    $scope.save = function () {
//                var methodName = "add.do";
//                if ($scope.entity.id != null) {
//                    methodName = "update.do"
//                }

        var methodObject = null;
        if ($scope.entity.id != null) {
            methodObject = brandService.update($scope.entity);
        }
        else {
            methodObject=brandService.add($scope.entity);
        }
        methodObject.success(
            function (response) {
                if (response.success) {
                    alert(response.message)
                    $scope.reloadList();
                }
                else {
                    alert(response.message)
                }
            });
    };


    //批量删除品牌
    $scope.dele = function () {
        brandService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    alert(response.message)
                    $scope.reloadList();
                    $scope.selectIds = [];
                }
                else {
                    alert(response.message)
                }
            });

    }
    //条件分页查询
    $scope.searchEntity = {};
    $scope.search = function (page, rows) {
        brandService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;//品牌分页当前页数据
                $scope.paginationConf.totalItems = response.total;//接受总页数
                //接受总页数
            }
        )
    }

})