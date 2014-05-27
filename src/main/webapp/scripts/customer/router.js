'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider',
        function ($stateProvider, $httpProvider, $translateProvider) {
            $stateProvider
                .state('customer', {
                    url: '/customer',
                    templateUrl: 'views/customers.html',
                    controller: 'CustomerController',
                    resolve:{
                        resolvedCustomer: ['Customer', function (Customer) {
                            return Customer.query();
                        }]
                    }
                })
        }]);
