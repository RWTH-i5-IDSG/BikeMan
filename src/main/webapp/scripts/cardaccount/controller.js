'use strict';

bikeManApp.controller('CardaccountController', ['$scope', 'resolvedCardAccounts', 'CardAccount',
    function ($scope, resolvedCardAccounts, CardAccount) {

        $scope.cardaccounts = resolvedCardAccounts;

//        $scope.delete = function (cardId) {
//            CardAccount.delete({cardId: cardId},
//                function () {
//                    $scope.cardaccounts = CardAccount.query();
//                });
//        };

        $scope.toggleCardAccount = function (cardAcc) {
            if (cardAcc.operationState === "OPERATIVE") {
                CardAccount.disable({'cardId': cardAcc.cardId}, function () {
                    $scope.cardaccounts = CardAccount.query();
                });
            } else {
                CardAccount.enable({'cardId': cardAcc.cardId}, function () {
                    $scope.cardaccounts = CardAccount.query();
                });
            }
        }

        $scope.create = function () {
            CardAccount.save($scope.newCardaccount, function () {
                $scope.cardaccounts = CardAccount.query();
                $('#addModal').modal('hide');
                $scope.clear();
            });
        };

        $scope.clear = function () {
            $scope.newCardaccount = null;
        }
    }]);
