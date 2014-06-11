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

velocityApp.controller('CustomerDetailController', ['$scope', 'resolvedCustomer', 'Customer',
    function($scope, resolvedCustomer, Customer, CreateEditCustomer) {

        $scope.customer = resolvedCustomer;

        $scope.isEditing = false;

        $scope.toggleEdit = function () {
            $scope.customer = Customer.searchByLogin({login: $scope.customer.login});
            $scope.isEditing = !$scope.isEditing;
        }

        $scope.saveCustomer = function () {
            $scope.saveCustomerDTO = {
                "userId": $scope.customer.userId,
                "login": $scope.customer.login,
                "customerId": $scope.customer.customerId,
                "firstname": $scope.customer.firstname,
                "lastname": $scope.customer.lastname,
                "address": $scope.customer.address,
                "birthday": $scope.customer.birthday,
                "cardId": $scope.customer.cardId,
                "isActivated": true
            }

            Customer.update($scope.saveCustomerDTO, function() {
                $scope.isEditing = false;
            })
        }

    }]);