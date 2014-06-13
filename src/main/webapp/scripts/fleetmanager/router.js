'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('fleetmanagers', {
                    url: '/fleetmanagers',
                    templateUrl: 'views/fleetmanagers.html',
                    controller: 'FleetmanagerController',
                    resolve:{
                        resolvedFleetmanagers: ['Fleetmanager', function (Fleetmanager) {
                            return Fleetmanager.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('fleetmanagerCreate', {
                    url: '/fleetmanagers/create/',
                    templateUrl: 'views/fleetmanagerCreate.html',
                    controller: 'FleetmanagerCreateController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
