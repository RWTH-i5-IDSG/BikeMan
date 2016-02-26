'use strict';

bikeManApp.controller('TransactionController', ['$scope', 'resolvedTransaction', 'Transaction', '$state',
    function ($scope, resolvedTransaction, Transaction, $state) {

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
                    // $scope.transactions = Transaction.query();
                    $state.go($state.$current, null, { reload: true });
                });
        };

        $scope.kill = function(id) {
            Transaction.kill({transactionId: id}).$promise.then(function () {
                $state.go($state.$current, null, { reload: true });
            });
        };

        $scope.clear = function () {
            $scope.transaction = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };

    }]);
