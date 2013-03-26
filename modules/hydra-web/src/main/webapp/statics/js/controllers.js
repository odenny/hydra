'use strict';

/* Controllers */

//用来操作多条件查询和grid展现的Ctrl
function QueryCtrl($scope, serviceQuery) {
    $scope.serviceArray = serviceQuery.getAllService();
//    $scope.queryResult = serviceQuery.getAllTrace();
}
//QueryCtrl.$inject = [$scope, queryService];

function TraceCtrl($scope, Trace, createView){
    //跟踪的js-model

    var trace = Trace.get({traceId:12},function(){//这个方法可以对model进行重新组装

    });

    createView(trace);

    trace.view.init();
    console.log(trace)

}


function MyCtrl2() {
}
MyCtrl2.$inject = [];
