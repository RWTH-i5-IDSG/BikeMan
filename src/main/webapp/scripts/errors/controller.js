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

bikeManApp.controller('ErrorHistoryController', ['$scope', 'resolvedErrorHistory',
    function ($scope, resolvedErrorHistory) {

        $scope.sfErrorHistory = resolvedErrorHistory;

        $scope.errorHistory = [];

    }]);