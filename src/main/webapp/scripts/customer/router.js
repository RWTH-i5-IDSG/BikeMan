'use strict';

bikeManApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('customers', {
                    url: '/customers',
                    templateUrl: 'views/customers.html',
                    controller: 'CustomerController',
                    resolve: {
                        resolvedCustomer: ['Customer', function (Customer) {
                            return Customer.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('customerDetail', {
                    url: '/customers/:login',
                    templateUrl: 'views/customerDetail.html',
                    controller: 'CustomerDetailController',
                    resolve: {
                        resolvedCustomer: ['Customer', '$stateParams',
                            function (Customer, $stateParams) {
                                return Customer.searchByLogin({login: $stateParams.login});
                            }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('customerCreate', {
                    url: '/customers/create/',
                    templateUrl: 'views/customerCreate.html',
                    controller: 'CustomerCreateController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
