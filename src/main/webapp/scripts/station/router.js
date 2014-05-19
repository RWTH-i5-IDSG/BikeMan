'use strict';

velocityApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/station', {
                    templateUrl: 'views/stations.html',
                    controller: 'StationController',
                    resolve:{
                        resolvedStation: ['Station', function (Station) {
                            return Station.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .when('/stations/:stationId', {
                    templateUrl: 'views/stationDetail.html',
                    controller: 'StationDetailController',
                    resolve:{
                        resolvedStation: function (Station, $route) {
                            return Station.get({id: $route.current.params.stationId});
                        }
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);

// Station.get({id: idparam})
//$route.current.params.Id