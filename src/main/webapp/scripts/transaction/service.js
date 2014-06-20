'use strict';

velocityApp.factory('Transaction', ['$resource',
    function ($resource) {
        return $resource('app/rest/transactions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'queryOpenTransactions':{ method: 'GET', isArray: true, url: 'app/rest/transactions/open'},
            'queryClosedTransactions':{ method: 'GET', isArray: true, url: 'app/rest/transactions/closed'},
            'get': { method: 'GET'},
            'queryTransactionsOfPedelecWithSize' : { method: 'GET', isArray: true, url: "app/rest/transactions/pedelec/:pedelecId"}
        });
    }]);
