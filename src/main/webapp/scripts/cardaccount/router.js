'use strict';

bikeManApp
    .config(['$stateProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($stateProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $stateProvider
                .state('cardaccounts', {
                    url: '/cardaccounts',
                    templateUrl: 'views/cardaccounts.html',
                    controller: 'CardaccountController',
                    resolve:{
                        resolvedCardAccounts: ['CardAccount', function (CardAccount) {
                            return CardAccount.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
