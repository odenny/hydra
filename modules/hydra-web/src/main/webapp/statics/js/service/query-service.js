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
angular.module('hydra.services.query', [])
    .factory('queryService', ['$filter', '$compile','ServiceList', function($filter, $compile, ServiceList){
        return {
            getTableSetting : function(myScope){
                var setting = {
                    sDom: "<'row'<'mySpan'l>r>t<'row'<'mySpan'i><'mySpan'p>>",
                    "bAutoWidth": false,
                    sInfo:"Showing _START_ to _END_ of _TOTAL_ entries",
                    sPaginationType: "bootstrap",
                    oLanguage: {
                        sLengthMenu: "每页展示 _MENU_ 条数据",
                        sZeroRecords: "没有任何数据",
                        sInfo: "从 _START_ 到 _END_ 总共 _TOTAL_ 条数据",
                        sInfoEmpty: "从 0 到 0 总共 0 条数据",
                        oPaginate: {
                            sPrevious: "上一页",
                            sNext: "下一页"
                        }
                    },
                    aLengthMenu: [[25, 50, 100, -1], [25, 50, 100, "全部"]],
                    iDisplayLength: 25,

                }
                return setting;
            },
            initTable:function(setting, myScope){
                setting.aoColumns = [
                    { "mData": "serviceId" },
                    { "mData": "timestamp" , "sClass": "center"},
                    { "mData": "duration" ,"sClass": "center"},
                    { "mData": "traceId" ,"sClass": "center"}
                ];
                setting.fnRowCallback = function( nRow, aData, iDisplayIndex ) {
                    $('td:eq(0)', nRow).html(myScope.serviceName);
                    $('td:eq(1)', nRow).html($filter('date')(aData['timestamp'], "yyyy-MM-dd HH:mm:ss"));
                    var element = $compile('<button type="button" class="btn btn-info" ng-click="linkToDetail('+aData['traceId']+')">查看详细</button>')(myScope);
                    $('td:eq(3)', nRow).html(element);
                };
                $('#traceTable').dataTable(setting);
            },
            initTableEx:function(setting, myScope){
                setting.aoColumns = [
                    { "mData": "serviceId" },
                    { "mData": "traceId" ,"sClass": "center"},
                    { "mData": "timestamp" , "sClass": "center"},
                    { "mData": "exInfo" ,"sClass": "center"},
                    { "mData": "traceId" ,"sClass": "center"}
                ];
                setting.sScrollX = "100%";
                setting.sScrollXInner = "110%";
                setting.bScrollCollapse = true;
                setting.fnRowCallback = function( nRow, aData, iDisplayIndex ) {
                    $('td:eq(0)', nRow).html(myScope.serviceName);
                    $('td:eq(2)', nRow).html($filter('date')(aData['timestamp'], "yyyy-MM-dd HH:mm:ss"));
                    $('td:eq(3)', nRow).html(aData['exInfo'].substring(0, 30) + '...').attr('title', aData['exInfo']);
                    var element = $compile('<button type="button" class="btn btn-info" ng-click="linkToDetail('+aData['traceId']+')">查看详细</button>')(myScope);
                    $('td:eq(4)', nRow).html(element);
                };
                $('#traceExTable').dataTable(setting);
            },
            initDate : function(){
                $('#startTime').datetimepicker({
                    language:  'zh-CN',
                    weekStart: 1,
                    todayBtn:  1,
                    autoclose: 1,
                    todayHighlight: true,
                    startView: 2,
                    forceParse: 0,
                    pickerReferer:'input',
                    initialDate:function(){
                        var date = new Date();
                        return new Date(date.getFullYear(), date.getMonth(), date.getDate());
                    }()
                });
            },
            initAuto: function(){
                $('#serviceName').typeahead({
                    items:15
                });
            },
            loadTableData : function(table, traceList){
                table.fnClearTable();
                table.fnAddData(traceList);
            },
            appChange:function (myScope) {
                $('#serviceName').val('');
                var appId;
                if (myScope.query.selectApp){
                    appId = myScope.query.selectApp.id;
                }
                if (appId){
                    myScope.query.serviceList = ServiceList.getAll({appId: appId}, function (serviceList) {
                        var serviceArray = [];
                        for (var i in serviceList) {
                            serviceArray.push(serviceList[i].name);
                            myScope.serviceMap[myScope.query.serviceList[i].id] = myScope.query.serviceList[i].name;
                        }
                        $('#serviceName').data('typeahead').source = serviceArray;
                    });
                }else {
                    $('#serviceName').data('typeahead').source = [];
                }
            }
        };
    }]);
