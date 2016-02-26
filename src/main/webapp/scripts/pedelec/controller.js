'use strict';

bikeManApp.controller('PedelecController', ['$scope', 'resolvedPedelec', 'Pedelec',
    function ($scope, resolvedPedelec, Pedelec) {

        $scope.sfPedelecs = resolvedPedelec;

        $scope.pedelecs = [];

        $scope.create = function () {
            console.log($scope.pedelec);
            Pedelec.save($scope.pedelec,
                function () {
                    $scope.pedelecs = Pedelec.query();
                    $('#savePedelecModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.pedelec = Pedelec.get({id: id});
            $('#savePedelecModal').modal('show');
        };

        $scope.delete = function (id) {
            Pedelec.delete({id: id},
                function () {
                    $scope.pedelecs = Pedelec.query();
                });
        };

        $scope.clear = function () {
            $scope.pedelec = {"manufacturerId": null, "state": null};
        };
    }
]);

bikeManApp.filter('myFilter', [function () {

    var standardComparator = function standardComparator(obj, text) {
        text = ('' + text).toLowerCase();
        return ('' + obj).toLowerCase().indexOf(text) > -1;
    };

    return function (array, expression) {
        //an example

        return array.filter(function (val, index) {

            if (expression.station && expression.station.stationName) {
                if (!(val.station && val.station.stationName)) return false;

                return standardComparator(val.station.stationName, expression.station.stationName);
            }

            if (expression.manufacturerId) {
                return standardComparator(val.manufacturerId, expression.manufacturerId);
            }

            if (expression.state) {
                return val.state === expression.state;
            }

            if (expression.transaction && expression.transaction.majorCustomerName) {
                console.log(expression.transaction.majorCustomerName)
                if (!(val.transaction && val.transaction.majorCustomerName)) return false;
                return standardComparator(val.transaction.majorCustomerName, expression.transaction.majorCustomerName);
            }

            return true;


        });
    };
}]);


bikeManApp.controller('PedelecDetailController', ['$scope', 'resolvedPedelec', 'Pedelec', 'Transaction', '$stateParams',
    function ($scope, resolvedPedelec, Pedelec, Transaction, $stateParams) {

        $scope.pedelec = resolvedPedelec;

        $scope.resultSizeValues = [10, 20, 50, 100, "all"];

        $scope.resultSize = 10;

        $scope.transactions = Transaction.queryTransactionsOfPedelecWithSize({
            pedelecId: $stateParams.pedelecId,
            resultSize: $scope.resultSize
        });

        $scope.isEditing = false;

        $scope.toggleEdit = function () {
            $scope.pedelec = Pedelec.get({id: $scope.pedelec.pedelecId});
            $scope.isEditing = !$scope.isEditing;
        }

        $scope.updateTransactions = function () {
            if ($scope.resultSize === "all") {
                $scope.transactions = Transaction.queryTransactionsOfPedelecWithSize({pedelecId: $stateParams.pedelecId});
            } else {
                $scope.transactions = Transaction.queryTransactionsOfPedelecWithSize({
                    pedelecId: $stateParams.pedelecId,
                    resultSize: $scope.resultSize
                });
            }
        }

        $scope.savePedelec = function () {

            var savePedelecDTO = {
                pedelecId: $scope.pedelec.pedelecId,
                manufacturerId: $scope.pedelec.manufacturerId,
                state: $scope.pedelec.state
            };

            Pedelec.update(savePedelecDTO, function () {
                $scope.isEditing = false;
            });
        }

        $scope.getConfig = function () {
            $scope.pedelecConfig = Pedelec.getConfig({id: $scope.pedelec.pedelecId});
        }

        $scope.updateConfig = function () {

            $scope.updateConfigDTO = {
                "maxCurrentValue": $scope.pedelecConfig.maxCurrentValue,
                "maxBatteryLevel": $scope.pedelecConfig.maxBatteryLevel
            }

            Pedelec.updateConfig({id: $scope.pedelec.pedelecId}, $scope.updateConfigDTO);

        }

    }]);

bikeManApp.controller('PedelecCreateController', ['$scope', 'Pedelec', '$timeout',
    function ($scope, Pedelec, $timeout) {

        $scope.pedelec = null;
        $scope.createSuccess = false;

        $scope.create = function () {

            $scope.$broadcast('show-errors-check-validity');

            if ($scope.form.$invalid) {
                return;
            }

            Pedelec.save($scope.pedelec,
                function () {
                    $scope.createSuccess = true;
                    $scope.clear();
                    $timeout(function () {
                        $scope.createSuccess = false;
                    }, 1000);
                });
        };

        $scope.clear = function () {
            $scope.$broadcast('show-errors-reset');
            $scope.pedelec = null;
        };

    }]);

