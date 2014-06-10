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


        $scope.searchByEmail = function (email) {

            if (!email || !email.length) {
                $scope.customers = Customer.query();
            }
            else {
                $scope.customers = [Customer.searchByEmail({email: email})];
            }
        };

        $scope.searchByLogin = function (login) {
            if (!login || !login.length) {
                $scope.customers = Customer.query();
            }
            else {
                $scope.customers = [Customer.searchByLogin({login: login})];
            }
        };

        $scope.searchByName = function (name) {
            if (!name || !name.length) {
                $scope.customers = Customer.query();
            }
            else {

                var searchNameString;
                var commaSplitted = name.split(", ");
                if (commaSplitted.length == 1) {
                    var nameSplitted = name.split(" ");
                    searchNameString = nameSplitted.join("+");
                }
                else {
                    searchNameString = commaSplitted.reverse().join("+");
                }
                $scope.customers = Customer.searchByName({name: searchNameString});
            }
        };

        $scope.resetCustomers = function () {
            $scope.search = null;

            $scope.customers = Customer.query();
        }

    }]);
