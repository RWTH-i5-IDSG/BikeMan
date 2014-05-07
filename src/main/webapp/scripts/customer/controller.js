'use strict';

velocityApp.controller('CustomerController', ['$scope', 'resolvedCustomer', 'Customer',
    function ($scope, resolvedCustomer, Customer) {

        $scope.customers = resolvedCustomer;

        $scope.create = function () {
            Customer.save($scope.customer,
                function () {
                    $scope.customers = Customer.query();
                    $('#saveCustomerModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.customer = Customer.get({id: id});
            $('#saveCustomerModal').modal('show');
        };

        $scope.delete = function (id) {
            Customer.delete({id: id},
                function () {
                    $scope.customers = Customer.query();
                });
        };

        $scope.clear = function () {
            $scope.customer = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
