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
                .state('stationDetail', {
                    url: 'stations/:stationId/',
                    templateUrl: 'views/stationDetail.html',
                    controller: 'StationDetailController',
                    resolve:{
                        resolvedStation: function (Station, $stateParams) {
                            return Station.get({id: $stateParams.stationId});
                        }
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('stationCreate', {
                    url: '/stations/create',
                    templateUrl: 'views/stationCreate.html',
                    controller: 'StationCreateController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);