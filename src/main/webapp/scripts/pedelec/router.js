'use strict';

velocityApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/pedelec', {
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
