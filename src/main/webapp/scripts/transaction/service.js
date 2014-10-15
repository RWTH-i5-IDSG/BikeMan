'use strict';

bikeManApp.factory('Transaction', ['$resource',
    function ($resource) {
        return $resource('app/rest/transactions/:id', {}, {
            'get': { method: 'GET'},

            'query': { method: 'GET', isArray: true},
            'queryOpenTransactions':{ method: 'GET', isArray: true, url: 'app/rest/transactions/open'},
            'queryClosedTransactions':{ method: 'GET', isArray: true, url: 'app/rest/transactions/closed'},
            'queryTransactionsOfPedelecWithSize' : { method: 'GET', isArray: true, url: "app/rest/transactions/pedelec/:pedelecId"},
            'queryTransactionsOfCustomerWithSize': { method: 'GET', isArray: true, url: "app/rest/transactions/customer/:login"},

            'queryMajorCustomerTransactions': {method: 'GET', isArray: true, url: 'app/rest/major-customer/transactions'},
            'queryOpenMajorCustomerTransactions': {method: 'GET', isArray: true, url: 'app/rest/major-customer/transactions/open'},
            'queryClosedMajorCustomerTransactions': {method: 'GET', isArray: true, url: 'app/rest/major-customer/transactions/closed'},
            'queryMajorCustomerTransactionsOfPedelecWithSize': {method: 'GET', isArray: true, url: 'app/rest/major-customer/transactions/pedelec/:pedelecId'},
            'queryMajorCustomerTransactionsOfLoginWithSize': {method: 'GET', isArray: true, url: 'app/rest/major-customer/transactions/customer/:login'}
        });
    }]);
