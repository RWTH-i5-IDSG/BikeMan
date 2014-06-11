'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('fleetmanager', {
                    url: '/fleetmanager',
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
        }]);
