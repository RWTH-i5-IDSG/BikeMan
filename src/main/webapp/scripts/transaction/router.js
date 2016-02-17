'use strict';

bikeManApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('transaction', {
                    url: '/transaction',
                    templateUrl: 'views/transactions.html',
                    controller: 'TransactionController',
                    resolve:{
                        resolvedTransaction: ['Transaction', function (Transaction) {
                            return Transaction.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('closedTransaction', {
                    url: '/closed-transaction',
                    templateUrl: 'views/transactions.html',
                    controller: 'TransactionController',
                    resolve:{
                        resolvedTransaction: ['Transaction', function (Transaction) {
                            return Transaction.queryClosedTransactions();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('openTransaction', {
                    url: '/open-transaction',
                    templateUrl: 'views/transactions.html',
                    controller: 'TransactionController',
                    resolve:{
                        resolvedTransaction: ['Transaction', function (Transaction) {
                            return Transaction.queryOpenTransactions();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('majorcustomer_transaction', {
                    url: '/majorcustomer-transactions',
                    templateUrl: 'views/transactions-majorcustomer.html',
                    controller: 'TransactionController',
                    resolve:{
                        resolvedTransaction: ['Transaction', function (Transaction) {
                            return Transaction.queryMajorCustomerTransactions();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })

                .state('majorcustomer_closedTransaction', {
                    url: '/majorcustomer-closed-transaction',
                    templateUrl: 'views/transactions-majorcustomer.html',
                    controller: 'TransactionController',
                    resolve:{
                        resolvedTransaction: ['Transaction', function (Transaction) {
                            return Transaction.queryClosedMajorCustomerTransactions();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('majorcustomer_openTransaction', {
                    url: '/majorcustomer-open-transaction',
                    templateUrl: 'views/transactions-majorcustomer.html',
                    controller: 'TransactionController',
                    resolve:{
                        resolvedTransaction: ['Transaction', function (Transaction) {
                            return Transaction.queryOpenMajorCustomerTransactions();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('fleetmanager_transaction', {
                    url: '/fleetmanager-transactions',
                    templateUrl: 'views/transactions-fleetmanager.html',
                    controller: 'TransactionController',
                    resolve:{
                        resolvedTransaction: ['Transaction', function (Transaction) {
                            return Transaction.queryFleetManagerTransactions();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
