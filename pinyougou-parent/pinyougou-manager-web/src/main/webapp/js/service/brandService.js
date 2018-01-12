//定义服务
app.service('brandService', function ($http) {
    this.add = function (entity) {
        return $http.post("../brand/add.do", entity);
    }
    this.findPage = function (page, rows) {
        return $http.get('../brand/findPage.do?page=' + page + "&rows=" + rows);
    }
    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id=' + id);
    }
    this.update = function (entity) {
        return $http.post('../brand/update.do', entity);
    }
//            this.save=function (methodName,entity) {
//                return  $http.post('../brand/' + methodName,entity);
//            }
    this.dele = function (selectIds) {
        return $http.get('../brand/delete.do?ids=' + selectIds);
    }
    this.search = function (page, rows, searchEntity) {

        return $http.post('../brand/search.do?page=' + page + "&&rows=" + rows, searchEntity);
    }
    //下拉列表数据
    this.selectOptionList=function(){
        return $http.get('../brand/selectOptionList.do');
    }
})