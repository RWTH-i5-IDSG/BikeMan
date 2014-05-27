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


velocityApp.controller('PedelecDetailController', ['$scope', 'resolvedPedelec', 'Pedelec',
    function ($scope, resolvedPedelec, Pedelec) {

        $scope.pedelec = resolvedPedelec;


    }]);

velocityApp.controller('PedelecCreateController', ['$scope', 'Pedelec',
    function ($scope, Pedelec)  {

        $scope.pedelec = null;

        $scope.create = function () {
            Pedelec.save($scope.pedelec,
                function () {
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.pedelec = null;
        };

    }]);

