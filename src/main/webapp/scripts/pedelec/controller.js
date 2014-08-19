'use strict';

velocityApp.controller('PedelecController', ['$scope', 'resolvedPedelec', 'Pedelec',
    function ($scope, resolvedPedelec, Pedelec) {

        $scope.pedelecs = resolvedPedelec;

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
    }]);


velocityApp.controller('PedelecDetailController', ['$scope', 'resolvedPedelec', 'Pedelec', 'Transaction', '$stateParams',
    function ($scope, resolvedPedelec, Pedelec, Transaction, $stateParams) {

        $scope.pedelec = resolvedPedelec;

        $scope.resultSizeValues = [10, 20, 50, 100, "all"];

        $scope.resultSize = 10;

        $scope.transactions = Transaction.queryTransactionsOfPedelecWithSize({pedelecId : $stateParams.pedelecId, resultSize : $scope.resultSize});

        $scope.isEditing = false;

        $scope.toggleEdit = function () {
            $scope.pedelec = Pedelec.get({id: $scope.pedelec.pedelecId});
            $scope.isEditing = !$scope.isEditing;
        }

        $scope.updateTransactions = function () {
            if ($scope.resultSize === "all") {
                $scope.transactions = Transaction.queryTransactionsOfPedelecWithSize({pedelecId : $stateParams.pedelecId});
            } else {
                $scope.transactions = Transaction.queryTransactionsOfPedelecWithSize({pedelecId : $stateParams.pedelecId, resultSize : $scope.resultSize});
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

velocityApp.controller('PedelecCreateController', ['$scope', 'Pedelec', '$timeout',
    function ($scope, Pedelec, $timeout)  {

        $scope.pedelec = null;
        $scope.createSuccess = false;

        $scope.create = function () {

            $scope.$broadcast('show-errors-check-validity');

            if ($scope.form.$invalid) { return; }

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
            $scope.pedelec = null;
        };

    }]);

