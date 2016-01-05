'use strict';

bikeManApp.controller('StationErrorsController', ['$scope', 'resolvedStationErrors',
    function ($scope, resolvedStationErrors) {

        $scope.sfStationErrors = resolvedStationErrors;

        $scope.stationErrors = [];

    }]);


bikeManApp.controller('PedelecErrorsController', ['$scope', 'resolvedPedelecErrors',
    function ($scope, resolvedPedelecErrors) {

        $scope.sfPedelecErrors = resolvedPedelecErrors;

        $scope.pedelecErrors = [];

    }]);