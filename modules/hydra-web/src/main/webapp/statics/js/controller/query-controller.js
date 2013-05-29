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
function QueryCtrl($scope, $filter, $location,//内置
                   queryService, sequenceService, treeService,//service
                   TraceList, TraceListEx, AppList, ServiceList, Trace) {//repository
    $scope.env = {
        windowWidth : window.screen.width,
        queryDivStyle: {width: function () {
            var width = window.screen.width;
            if (width == 1920) {
                return '77%';
            } else if (width < 1920 && width >= 1366) {
                return '63%';
            } else {
                return '55%';
            }
        }()},
        sequenceDivStyle:{
            width:'74%'
        }
    };
    $scope.serviceMap = {};
    $scope.tableType = 'duration';
    var setting = queryService.getTableSetting($scope);

    queryService.initDate();
    queryService.initTable(setting, $scope);
    queryService.initTableEx(setting, $scope);
    queryService.initAuto();

    var query = {
        exBtn: {
            type: false,
            name: function () {
                if ($scope.query.exBtn.type) {
                    return '出现异常';
                } else {
                    return '忽略异常';
                }
            },
            click: function () {
                $scope.query.exBtn.type = $scope.query.exBtn.type ? false : true;
                if ($scope.query.exBtn.type) {
                    delete $scope.query.durationMin;
                    delete $scope.query.durationMax;
                }
            }
        },
        queryBtn:{
            name:'查询',
            myClass: 'btn btn-success btn-large',
            disable: function(){
                query.queryBtn.myClass = 'btn btn-success btn-large disabled';
                query.queryBtn.name = '查询中...';
            },
            enable: function(){
                query.queryBtn.myClass = 'btn btn-success btn-large';
                query.queryBtn.name = '查询';
            }
        },
        appList: AppList.getAll(),
        serviceList: [],
        sum: 500,
        submitQuery: function () {
            var serviceId;
            for (var i in $scope.query.serviceList) {
                if ($scope.query.serviceList[i].name == $('#serviceName').val()) {
                    serviceId = $scope.query.serviceList[i].id;
                }
            }
            $scope.serviceName = $scope.serviceMap[serviceId];

            var isValid = true;
            var validateMsg;
            //验证
            if (isValid && !serviceId) {
                validateMsg = "服务名不正确！";
                isValid = false;
            }
            if (isValid && $('#realTime').val() == "") {
                validateMsg = "请输入开始时间！";
                isValid = false;
            }
            showValidateMsg(isValid, validateMsg);

            function showValidateMsg(isValid, validateMsg) {
                $scope.query.invalid = !isValid;
                $scope.query.validateMsg = validateMsg;
            }

            var startTime = $filter('dateToLong')($('#realTime').val());
            var durationMin = $scope.query.durationMin || 0;
            var durationMax = $scope.query.durationMax || 1000000;

            //查询
            if (isValid) {
                $scope.query.queryBtn.disable();
                queryService.loadTableData($('#traceExTable').dataTable(), []);
                queryService.loadTableData($('#traceTable').dataTable(), []);
                if ($scope.query.exBtn.type) {//如果查询所有异常trace
                    $scope.tableType = 'ex';
                    $scope.traceListEx = TraceListEx.getTraceList({serviceId: serviceId, startTime: startTime, sum: $scope.query.sum}, function (traceList) {
                        queryService.loadTableData($('#traceExTable').dataTable(), traceList);
                        $scope.query.queryBtn.enable();
                    });
                } else {//如果是查duration
                    $scope.tableType = 'duration';
                    $scope.traceList = TraceList.getTraceList({serviceId: serviceId, startTime: startTime, durationMin: durationMin, durationMax: durationMax, sum: $scope.query.sum}, function (traceList) {
                        queryService.loadTableData($('#traceTable').dataTable(), traceList);
                        $scope.query.queryBtn.enable();
                    });
                }

            }
        },
        appChange: function () {
            queryService.appChange($scope);
        },
        durationChange: function () {
            $scope.query.exBtn.type = false;
            $('#ex').removeClass('active');
        },
        invalid: false
    };

    $scope.linkToDetail = function (traceId) {
        $scope.showType = 'trace';


        $scope.returnToQuery = function () {
            $scope.showType = 'query';
            $('#treeDiv').empty();
            $('#sequenceDiv').empty();
            delete $scope.trace;
        }

        var trace = Trace.get({traceId: traceId}, function (t) {
            if (t.available) {
                sequenceService.getMyTrace(t, $scope);
                var spanMap = sequenceService.getSpanMap(t);

                sequenceService.createView(t);//生成时序图的svg
                sequenceService.createSpanAndDetail(t, spanMap, $scope);//生成时序图的具体细节

                treeService.createTree(t);//生成树的svg
                treeService.createTreeDetail(t, $scope);//生成树的具体结构
            }
        });

        $scope.trace = trace;
    }

    $scope.query = query;

    $scope.showType = 'query';

}
