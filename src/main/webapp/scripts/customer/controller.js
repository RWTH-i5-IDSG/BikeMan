'use strict';

bikeManApp.controller('CustomerController', ['$scope', 'resolvedCustomer', 'Customer',
    function ($scope, resolvedCustomer, Customer) {

        $scope.customers = resolvedCustomer;

        $scope.delete = function (id) {
            Customer.delete({id: id},
                function () {
                    $scope.customers = Customer.query();
                });
        };

        $scope.searchByLogin = function (login) {
            if (!login || !login.length) {
                $scope.customers = Customer.query();
            }
            else {
                $scope.customers = Customer.searchByLogin({login: login}, function(customer) {
                    if (customer) {
                        $scope.customers = [customer];
                    } else {
                        $scope.customers = [];
                    }
                });
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
        };

        $scope.toggleActivation = function (customerId, isActivated) {
            if (isActivated) {
                Customer.deactivate({id: customerId}, function () {
                    $scope.customers = Customer.query();
                });
            } else {
                Customer.activate({id: customerId}, function () {
                    $scope.customers = Customer.query();
                });
            }
        };
    }]);

bikeManApp.controller('CustomerDetailController', ['$scope', 'resolvedCustomer', 'Customer', 'Transaction', '$stateParams',
    function($scope, resolvedCustomer, Customer, Transaction, $stateParams) {

        $scope.customer = resolvedCustomer;

        $scope.resultSizeValues = [10, 20, 50, 100, "all"];

        // set initial resultSize to 10
        $scope.resultSize = 10;

        $scope.transactions = Transaction.queryTransactionsOfCustomerWithSize({login : $stateParams.login, resultSize : $scope.resultSize});

        $scope.isEditing = false;

        // open the datepicker control
        $scope.open = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.isOpen = true;
        }

        $scope.dateOptions = {
            formatYear: 'yyyy',
            startingDay: 1
        };

        $scope.maxDate = new Date();

        $scope.toggleEdit = function () {
            $scope.customer = Customer.searchByLogin({login: $scope.customer.login});
            $scope.isEditing = !$scope.isEditing;
        }

        $scope.updateTransactions = function () {
            if ($scope.resultSize === "all") {
                $scope.transactions = Transaction.queryTransactionsOfCustomerWithSize({login : $stateParams.login});
            } else {
                $scope.transactions = Transaction.queryTransactionsOfCustomerWithSize({login : $stateParams.login, resultSize : $scope.resultSize});
            }
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

bikeManApp.controller('CustomerCreateController', ['$scope', 'Customer', '$timeout', '$filter',
    function($scope, Customer, $timeout, $filter) {

        // open the datepicker control
        $scope.open = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.opened = true;
        }

        $scope.dateOptions = {
            formatYear: 'yyyy',
            startingDay: 1
        };

        $scope.maxDate = new Date();

        $scope.create = function () {
            $scope.$broadcast('show-errors-check-validity');

            if ($scope.form.$invalid) { return; }

            Customer.save($scope.customer,
                function () {
                    $timeout(function () {
                        $scope.clear();
                    }, 1000);
                });
        };

        $scope.clear = function () {
            $scope.customer = {
                userId: null,
                login: null,
                customerId: null,
                cardId: null,
                firstname: null,
                lastname: null,
                address: null,
                birthday: null,
                isActivated: false
            }
        };

        $scope.clear();
}])