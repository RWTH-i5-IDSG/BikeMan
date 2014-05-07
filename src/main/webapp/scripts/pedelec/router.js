'use strict';

velocityApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider',
        function ($routeProvider, $httpProvider, $translateProvider) {
            $routeProvider
                .when('/pedelec', {
                    templateUrl: 'views/pedelecs.html',
                    controller: 'PedelecController',
                    resolve:{
                        resolvedPedelec: ['Pedelec', function (Pedelec) {
                            return Pedelec.query();
                        }]
                    }
                })
        }]);
