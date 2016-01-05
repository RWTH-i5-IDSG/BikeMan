'use strict';

bikeManApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('stationErrors', {
                    url: '/stationErrors',
                    templateUrl: 'views/stationErrors.html',
                    controller: 'StationErrorsController',
                    resolve:{
                        resolvedStationErrors: ['StationErrors', function (StationErrors) {
                            return StationErrors.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('pedelecErrors', {
                    url: '/pedelecErrors',
                    templateUrl: 'views/pedelecErrors.html',
                    controller: 'PedelecErrorsController',
                    resolve:{
                        resolvedPedelecErrors: ['PedelecErrors', function (PedelecErrors) {
                            return PedelecErrors.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })

        }]);
