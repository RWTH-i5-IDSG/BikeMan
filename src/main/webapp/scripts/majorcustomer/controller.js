'use strict';

bikeManApp.controller('MajorcustomerController', ['$scope', 'resolvedMajorcustomers', 'Majorcustomer',
    function ($scope, resolvedMajorcustomers, Majorcustomer) {

        $scope.majorcustomers = resolvedMajorcustomers;

        $scope.create = function () {
            Majorcustomer.save($scope.majorcustomer,
                function () {
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.majorcustomer= Majorcustomer.get({id: id});
        };

        $scope.delete = function (id) {
            Majorcustomer.delete({id: id},
                function () {
                    $scope.majorcustomer = Majorcustomer.query();
                });
        };

        $scope.clear = function () {
            $scope.majorcustomer = null;
        };

        $scope.deleteModal = function (majorcustomer) {
            $scope.customerToDelete = majorcustomer;
        };

    }]);

bikeManApp.controller('MajorcustomerDetailController', ['$scope', 'resolvedMajorcustomer', 'Majorcustomer', '$stateParams',
    function($scope, resolvedMajorcustomer, Majorcustomer, Transaction, $stateParams) {

        $scope.majorcustomer = resolvedMajorcustomer;

        $scope.resultSizeValues = [10, 20, 50, 100, "all"];

        // set initial resultSize to 10
        $scope.resultSize = 10;

        // TODO: card accounts
//        $scope.transactions = Transaction.queryTransactionsOfMajorcustomerWithSize({login : $stateParams.login, resultSize : $scope.resultSize});

        $scope.isEditing = false;

        $scope.toggleEdit = function () {
            $scope.majorcustomer = Majorcustomer.searchByLogin({login: $scope.majorcustomer.login});
            $scope.isEditing = !$scope.isEditing;
        }


        $scope.saveMajorcustomer = function () {
            $scope.saveMajorcustomerDTO = {
                // TODO
            }

            Majorcustomer.update($scope.saveMajorcustomerDTO, function() {
                $scope.isEditing = false;
            })
        }

    }]);

bikeManApp.controller('MajorcustomerCreateController', ['$scope', 'Majorcustomer', '$timeout', '$state',
    function ($scope, Majorcustomer, $timeout, $state)  {

        $scope.majorcustomer = null;
        $scope.createSuccess = false;

        $scope.create = function () {

            $scope.$broadcast('show-errors-check-validity');

            if ($scope.form.$invalid) { return; }

            Majorcustomer.save($scope.majorcustomer,
                function () {
                    $state.go('majorcustomers');
                });
        };

        $scope.clear = function () {
            $scope.$broadcast('show-errors-reset');
            $scope.majorcustomer = null
        };

    }]);
