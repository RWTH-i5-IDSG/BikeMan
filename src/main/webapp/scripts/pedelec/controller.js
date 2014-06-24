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

        $scope.resultSize = 5;

        $scope.transactions = Transaction.queryTransactionsOfPedelecWithSize({pedelecId : $stateParams.pedelecId, resultSize : $scope.resultSize});

        $scope.isEditing = false;

        $scope.toggleEdit = function () {
            $scope.pedelec = Pedelec.get({id: $scope.pedelec.pedelecId});
            $scope.isEditing = !$scope.isEditing;
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



    }]);

velocityApp.controller('PedelecCreateController', ['$scope', 'Pedelec', '$timeout',
    function ($scope, Pedelec, $timeout)  {

        $scope.pedelec = null;
        $scope.createSuccess = false;

        $scope.create = function () {
            Pedelec.save($scope.pedelec,
                function () {
                    $scope.createSuccess = true;
                    $timeout(function () {
                        $scope.createSuccess = false;
                        $scope.clear();
                    }, 3000);
                });
        };

        $scope.clear = function () {
            $scope.pedelec = null;
        };

    }]);

