'use strict';

velocityApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider',
        function ($routeProvider, $httpProvider, $translateProvider) {
            $routeProvider
                .when('/customer', {
                    templateUrl: 'views/customers.html',
                    controller: 'CustomerController',
                    resolve:{
                        resolvedCustomer: ['Customer', function (Customer) {
                            return Customer.query();
                        }]
                    }
                })
        }]);
