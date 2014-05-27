'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('pedelecs', {
                    url: '/pedelecs',
                    templateUrl: 'views/pedelecs.html',
                    controller: 'PedelecController',
                    resolve:{
                        resolvedPedelec: ['Pedelec', function (Pedelec) {
                            return Pedelec.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('pedelecDetail', {
                    url: 'pedelecs/:pedelecId',
                    templateUrl: 'views/pedelecDetail.html',
                    controller: 'pedelecDetailController',
                    resolve:{
                        resolvedPedelec: function (Pedelec, $route) {
                            return Pedelec.get({id: $route.current.params.pedelecId});
                        }
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('pedelecCreate', {
                    url: '/pedelecs/create',
                    templateUrl: 'views/pedelecCreate.html',
                    controller: 'PedelecCreateController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
