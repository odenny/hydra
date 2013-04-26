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
function QueryCtrl($scope,$filter, queryService, TraceList, TraceListEx, AppList, ServiceList) {

    queryService.initDate();
    $scope.traceList = [];
    queryService.initTable($scope.traceList, $scope);

    queryService.initAuto();

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
                if ($scope.query.exBtn.type){
                    delete $scope.query.durationMin;
                    delete $scope.query.durationMax;
                }
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

            var isValid = true;
            var validateMsg;
            //验证
            if (isValid && !serviceId ){
                validateMsg = "服务名不正确！";
                isValid = false;
            }
            if (isValid && $('#realTime').val() == ""){
                validateMsg = "请输入开始时间！";
                isValid = false;
            }
            showValidateMsg(isValid, validateMsg);

            var startTime = $filter('dateToLong')($('#realTime').val());
            var durationMin = $scope.query.durationMin || 0;
            var durationMax = $scope.query.durationMax || 1000000;
            $scope.serviceName = serviceName;

            function showValidateMsg(isValid, validateMsg){
                $scope.query.invalid = !isValid;
                $scope.query.validateMsg = validateMsg;
            }

            //查询
            if (isValid){
                if ($scope.query.exBtn.type){//如果查询所有异常trace
                    $scope.traceList = TraceListEx.getTraceList({serviceId: serviceId, startTime: startTime, sum: $scope.query.sum},function(traceList){
                        queryService.loadTableData(traceList);
                    });
                }else{
                    $scope.traceList = TraceList.getTraceList({serviceId: serviceId, startTime: startTime, durationMin: durationMin, durationMax: durationMax, sum: $scope.query.sum},function(traceList){
                        queryService.loadTableData(traceList);
                    });
                }
            }
        },
        appChange : function (appId) {
            $('#serviceName').val('');
            var appId;
            if ($scope.query.selectApp){
                appId = $scope.query.selectApp.id;
            }
            if (appId){
                $scope.query.serviceList = ServiceList.getAll({appId: appId}, function (serviceList) {
                    var serviceArray = [];
                    for (var i in serviceList) {
                        serviceArray.push(serviceList[i].name);
                    }
                    $('#serviceName').data('typeahead').source = serviceArray;
                });
            }else {
                $('#serviceName').data('typeahead').source = [];
            }
        },
        durationChange : function(){
            $scope.query.exBtn.type = false;
            $('#ex').removeClass('active');
        },
        invalid:false
    };

    $scope.linkToDetail = function(){
        alert("1231231");
    }

    $scope.query = query;
}
