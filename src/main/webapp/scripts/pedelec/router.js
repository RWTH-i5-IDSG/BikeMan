'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('pedelec', {
                    url: '/pedelec',
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
        }]);
