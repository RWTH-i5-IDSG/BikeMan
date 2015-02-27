'use strict';

bikeManApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('pedelecs', {
                    url: '/pedelecs',
                    templateUrl: 'views/pedelecs.html',
                    controller: 'PedelecController',
                    resolve: {
                        resolvedPedelec: ['Pedelec', function (Pedelec) {
                            return Pedelec.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('pedelecDetail', {
                    url: '/pedelecs/:pedelecId',
                    templateUrl: 'views/pedelecDetail.html',
                    controller: 'PedelecDetailController',
                    resolve: {
                        resolvedPedelec: ['Pedelec', '$stateParams',
                            function (Pedelec, $stateParams) {
                                return Pedelec.get({id: $stateParams.pedelecId});
                            }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('pedelecCreate', {
                    url: '/pedelecs/create/',
                    templateUrl: 'views/pedelecCreate.html',
                    controller: 'PedelecCreateController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
