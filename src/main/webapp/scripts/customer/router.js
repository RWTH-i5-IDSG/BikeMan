'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('customer', {
                    url: '/customer',
                    templateUrl: 'views/customers.html',
                    controller: 'CustomerController',
                    resolve:{
                        resolvedCustomer: ['Customer', function (Customer) {
                            return Customer.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
