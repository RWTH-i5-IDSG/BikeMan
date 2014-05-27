'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('stations', {
                    url: '/stations',
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
                .state('.stationId', {
                    url: '/:stationId',
                    templateUrl: 'views/stationDetail.html',
                    controller: 'StationDetailController',
                    resolve:{
                        resolvedStation: function (Station, $route) {
                            return Station.get({id: $route.current.params.stationId});
                        }
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    },
                    parent: 'stations'
                })
        }]);

// Station.get({id: idparam})
//$route.current.params.Id