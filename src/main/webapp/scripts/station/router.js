'use strict';

velocityApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider',
        function ($routeProvider, $httpProvider, $translateProvider) {
            $routeProvider
                .when('/station', {
                    templateUrl: 'views/stations.html',
                    controller: 'StationController',
                    resolve:{
                        resolvedStation: ['Station', function (Station) {
                            return Station.query();
                        }]
                    }
                })
        }]);
