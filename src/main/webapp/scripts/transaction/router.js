'use strict';

velocityApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider',
        function ($stateProvider, $httpProvider, $translateProvider) {
            $stateProvider
                .state('transaction', {
                    url: '/transaction',
                    templateUrl: 'views/transactions.html',
                    controller: 'TransactionController',
                    resolve:{
                        resolvedTransaction: ['Transaction', function (Transaction) {
                            return Transaction.query();
                        }]
                    }
                })
        }]);
