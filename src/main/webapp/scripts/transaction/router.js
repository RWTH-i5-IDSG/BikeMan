'use strict';

velocityApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider',
        function ($routeProvider, $httpProvider, $translateProvider) {
            $routeProvider
                .when('/transaction', {
                    templateUrl: 'views/transactions.html',
                    controller: 'TransactionController',
                    resolve:{
                        resolvedTransaction: ['Transaction', function (Transaction) {
                            return Transaction.query();
                        }]
                    }
                })
        }]);
