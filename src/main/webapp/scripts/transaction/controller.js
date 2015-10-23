'use strict';

bikeManApp.controller('TransactionController', ['$scope', 'resolvedTransaction', 'Transaction',
    function ($scope, resolvedTransaction, Transaction) {

        $scope.sfTransactions = resolvedTransaction;

        $scope.transactions = [];

        $scope.create = function () {
            Transaction.save($scope.transaction,
                function () {
                    $scope.transactions = Transaction.query();
                    $('#saveTransactionModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.transaction = Transaction.get({id: id});
            $('#saveTransactionModal').modal('show');
        };

        $scope.delete = function (id) {
            Transaction.delete({id: id},
                function () {
                    $scope.transactions = Transaction.query();
                });
        };

        $scope.kill = function(id) {
            Transaction.kill({transactionId: id});
            $scope.transactions = Transaction.query();
        }

        $scope.clear = function () {
            $scope.transaction = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };

    }]);
