'use strict';

/* App Module */
var httpHeaders;

var bikeManApp = angular.module('bikeManApp', ['http-auth-interceptor', 'tmh.dynamicLocale',
    'ngResource', 'ngCookies', 'bikeManAppUtils', 'pascalprecht.translate', 'truncate', 'ui.router', 'ui.bootstrap', 'ui.bootstrap.showErrors']);

bikeManApp
    .config(['$stateProvider', '$urlRouterProvider', '$httpProvider', '$translateProvider',  'tmhDynamicLocaleProvider', 'USER_ROLES', '$compileProvider',
        function ($stateProvider, $urlRouterProvider, $httpProvider, $translateProvider, tmhDynamicLocaleProvider, USER_ROLES, $compileProvider) {

            $urlRouterProvider.otherwise('/main');


            $stateProvider
                .state('login', {
                    url: '/login',
                    templateUrl: 'views/login.html',
                    controller: 'LoginController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('main', {
                    url: '/main',
                    templateUrl: 'views/main.html',
                    controller: 'MainController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('error', {
                    url: '/error',
                    templateUrl: 'views/error.html',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('settings', {
                    url: '/settings',
                    templateUrl: 'views/settings.html',
                    controller: 'SettingsController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('password', {
                    url: '/password',
                    templateUrl: 'views/password.html',
                    controller: 'PasswordController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('sessions', {
                    url: '/sessions',
                    templateUrl: 'views/sessions.html',
                    controller: 'SessionsController',
                    resolve:{
                        resolvedSessions:['Sessions', function (Sessions) {
                            return Sessions.get();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('metrics', {
                    url: '/metrics',
                    templateUrl: 'views/metrics.html',
                    controller: 'MetricsController',
                    access: {
                        authorizedRoles: [USER_ROLES.admin]
                    }
                })
                .state('logs', {
                    url: '/logs',
                    templateUrl: 'views/logs.html',
                    controller: 'LogsController',
                    resolve:{
                        resolvedLogs:['LogsService', function (LogsService) {
                            return LogsService.findAll();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.admin]
                    }
                })
                .state('audits', {
                    url: '/audits',
                    templateUrl: 'views/audits.html',
                    controller: 'AuditsController',
                    access: {
                        authorizedRoles: [USER_ROLES.admin]
                    }
                })
                .state('logout', {
                    url: '/logout',
                    templateUrl: 'views/main.html',
                    controller: 'LogoutController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
                .state('docs', {
                    url: '/docs',
                    templateUrl: 'views/docs.html',
                    access: {
                        authorizedRoles: [USER_ROLES.admin]
                    }
                });


            // GLOBAL MESSAGES
            $httpProvider.responseInterceptors.push(function($timeout, $q, $rootScope) {
                return function(promise) {
                    var errorInterval = 3000;
                    var successInterval = 1500;

                    return promise.then(function(successResponse) {

                        $rootScope.$broadcast('remove-error-message');

                        if (successResponse.config.method.toUpperCase() != 'GET') {
                            var alertType = 'alert-success';
                            //showMessage('Successful', 'successMessage', successInterval, alertType);
                            $rootScope.$broadcast('new-success-message', {msg: "Successful"});
                        }
                        return successResponse;

                    }, function(errorResponse) {
                        var alertType = 'alert-danger';
                        switch (errorResponse.status) {
                            // remove this because 401 is used for checking user auth
                            case 400:
                                //showMessage(errorResponse.data.message + "\n" + errorResponse.data.fieldErrors.join("\n"), 'errorMessage', errorInterval, alertType);

                                $rootScope.$broadcast('new-error-message', {msg: errorResponse.data.message, errors:errorResponse.data.fieldErrors});
                                break;
                            case 401:
                                // do nothing for now
//                                showMessage('Wrong usename or password', 'errorMessage', errorInterval, alertType);
                                break;
                            case 403:
                                //showMessage('You don\'t have the right to do this', 'errorMessage', errorInterval, alertType);
                                $rootScope.$broadcast('new-error-message', {msg: 'You don\'t have the right to do this'});
                                break;
                            case 404:
                                //showMessage('Not Found', 'errorMessage', errorInterval, alertType);
                                $rootScope.$broadcast('new-error-message', {msg: 'You don\'t have the right to do this'});
                                break;
                            case 500:
                                $rootScope.$broadcast('new-error-message', {msg: errorResponse.data.message, error:errorResponse.data.fieldErrors});
                                break;
                            default:
//                                showMessage('Error ' + errorResponse.status + ': ' + errorResponse.data.message, 'errorMessage', errorInterval, alertType);
//                                console.log(errorResponse);
                                //showMessage(errorResponse.data.message, 'errorMessage', errorInterval, alertType);
                                $rootScope.$broadcast('new-error-message', {msg: 'You don\'t have the right to do this'});
                                break;
                        }
                        return $q.reject(errorResponse);
                    });
                };
            });

//            $compileProvider.directive('appMessages', function() {
//                var directiveDefinitionObject = {
//                    link: function(scope, element, attrs) { elementsList.push($(element)); }
//                };
//                return directiveDefinitionObject;
//            });

            // END Global Messages

            // Initialize angular-translate
            $translateProvider.useStaticFilesLoader({
                prefix: 'i18n/',
                suffix: '.json'
            });

            $translateProvider.preferredLanguage('en');

            $translateProvider.useCookieStorage();

            tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js')
            tmhDynamicLocaleProvider.useCookieStorage('NG_TRANSLATE_LANG_KEY');

            httpHeaders = $httpProvider.defaults.headers;
        }])
        .run(['$rootScope', '$state', 'AuthenticationSharedService', 'USER_ROLES',
        function ($rootScope, $state, AuthenticationSharedService, USER_ROLES) {
            $rootScope.isAuthorized = AuthenticationSharedService.isAuthorized;
            AuthenticationSharedService.valid(USER_ROLES.all).then(function(authenticated) {
                if (!authenticated) {
                    $state.transitionTo("login");
                } else if ($state.$current.name == "login") {
                    $state.transitionTo('main');
                }
            });
        }])
        .run(['$rootScope', '$location', '$http', 'AuthenticationSharedService', 'Session', 'USER_ROLES', '$state',

            function($rootScope, $location, $http, AuthenticationSharedService, Session, USER_ROLES, $state) {

                // We are storing the state (web-page) for later use (See Step 1 and 2 below)
                var wantedState;

                $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                    $rootScope.isAuthorized = AuthenticationSharedService.isAuthorized;
                    $rootScope.userRoles = USER_ROLES;
                    AuthenticationSharedService.valid(toState.access.authorizedRoles).then(function(authenticated){
                        if (!authenticated) {
                            $state.go("login");
                        }
                        if (authenticated && toState.name == "login") {
                            $state.go("main");
                        }
                    });
                });

                // Call when the 401 response is returned by the server
                $rootScope.$on('event:auth-loginRequired', function(rejection) {

                    AuthenticationSharedService.refresh();
                    // Step 1:
                    // Save the state that the user wanted to see
                    // to route to after login is confirmed (see Step 2)
//                    wantedState = $state.current;
//
//                    $rootScope.authenticated = false;
//                    $state.go("login");

                });

                // Call when the the client is confirmed
                $rootScope.$on('event:auth-loginConfirmed', function(data) {
                    $rootScope.authenticated = true;

                    // Step 2:
                    // Since the login is confirmed now, route to the state
                    // that the user wanted to see beforehand

                    // first access to the frontend
                    if (typeof wantedState === "undefined") {
                        $state.go("main");

                    // consequent state changes
                    } else {
                        console.log(wantedState.name);
                        $state.go(wantedState);
                    }
                });

                // Call when the 403 response is returned by the server
                $rootScope.$on('event:auth-notAuthorized', function(rejection) {
                    $rootScope.errorMessage = 'errors.403';
//                    $location.path('/error').replace();
                    $state.go("error");
                });

                // Call when the user logs out
                $rootScope.$on('event:auth-loginCancelled', function() {
                    $state.go("login");
                });
        }]);
