'use strict';

bikeManApp.controller('MajorcustomerController', ['$scope', 'resolvedMajorcustomer', 'Majorcustomer',
    function ($scope, resolvedMajorcustomer, Majorcustomer) {

        $scope.majorcustomer = resolvedMajorcustomer;

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
