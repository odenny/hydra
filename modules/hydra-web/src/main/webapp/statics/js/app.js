'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp',
        [
            'hydra.services.sequence','hydra.services.tree',
            'hydra.repository.trace','hydra.repository.service','hydra.services.query',
            'hydra.filters'
        ]).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/query', {templateUrl: '/index/query.html', controller: QueryCtrl});
        $routeProvider.when('/trace', {templateUrl: '/index/trace.html', controller: TraceCtrl});
//    $routeProvider.when('/view2', {templateUrl: 'partials/partial2.html', controller: MyCtrl2});
    $routeProvider.otherwise({redirectTo: '/query'});
    }]);
