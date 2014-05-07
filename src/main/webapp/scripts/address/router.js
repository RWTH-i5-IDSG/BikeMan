'use strict';

velocityApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider',
        function ($routeProvider, $httpProvider, $translateProvider) {
            $routeProvider
                .when('/address', {
                    templateUrl: 'views/addresss.html',
                    controller: 'AddressController',
                    resolve:{
                        resolvedAddress: ['Address', function (Address) {
                            return Address.query();
                        }]
                    }
                })
        }]);
