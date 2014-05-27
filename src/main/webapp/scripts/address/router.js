'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider',
        function ($stateProvider, $httpProvider, $translateProvider) {
            $stateProvider
                .state('address', {
                    url: '/address',
                    templateUrl: 'views/addresss.html',
                    controller: 'AddressController',
                    resolve:{
                        resolvedAddress: ['Address', function (Address) {
                            return Address.query();
                        }]
                    }
                })
        }]);
