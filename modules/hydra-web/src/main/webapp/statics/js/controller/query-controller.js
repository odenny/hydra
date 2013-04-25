/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
'use strict';
function QueryCtrl($scope,$filter, queryService, TraceList, AppList, ServiceList) {

    //以下为测试
//    $scope.abc = "11111111122222222233333333333";
//    $scope.test1= function(){
//        alert("1923890128390123");
//    }

    queryService.initDate();

    $scope.traceList = [];

    queryService.initTable($scope.traceList, $scope);

    var query = {
        exBtn:{
            type:false,
            name:function(){
                if ($scope.query.exBtn.type){
                    return '出现异常';
                }else {
                    return '正常调用';
                }
            },
            click:function(){
                $scope.query.exBtn.type = $scope.query.exBtn.type?false:true;
            }
        },
        appList : AppList.getAll(),
        serviceList:[],
        sum :500,
        submitQuery: function () {
            var serviceId;
            var serviceName;
            for(var i in $scope.query.serviceList){
                if ($scope.query.serviceList[i].name == $('#serviceName').val()){
                    serviceId = $scope.query.serviceList[i].id;
                    serviceName = $scope.query.serviceList[i].name;
                }
            }
            var startTime = $filter('dateToLong')($('#realTime').val());
            queryService.setTableServiceName(serviceName);
            $scope.traceList = TraceList.getTraceList({serviceId: serviceId, startTime: startTime, durationMin: $scope.query.durationMin, durationMax: $scope.query.durationMax, sum: $scope.query.sum},function(traceList){
                queryService.loadTableData(traceList);
            });
        },
        appChange : function (appId) {
            var appId = $scope.query.selectApp.id;
            $scope.query.serviceList = ServiceList.getAll({appId: appId}, function (serviceList) {
                var serviceArray = [];
                for (var i in serviceList) {
                    serviceArray.push(serviceList[i].name);
                }
                $('#serviceName').typeahead({source: serviceArray});
            });
        }
    };

    $scope.query = query;
}
