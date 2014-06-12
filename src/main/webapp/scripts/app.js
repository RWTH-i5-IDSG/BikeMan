'use strict';

/* App Module */
var httpHeaders;

var velocityApp = angular.module('velocityApp', ['http-auth-interceptor', 'tmh.dynamicLocale',
    'ngResource', 'ngCookies', 'velocityAppUtils', 'pascalprecht.translate', 'truncate', 'ui.router']);

velocityApp
    .config(['$stateProvider', '$urlRouterProvider', '$httpProvider', '$translateProvider',  'tmhDynamicLocaleProvider', 'USER_ROLES', '$compileProvider',
        function ($stateProvider, $urlRouterProvider, $httpProvider, $translateProvider, tmhDynamicLocaleProvider, USER_ROLES, $compileProvider) {
//            $urlRouterProvider.otherwise('/login')
//                    templateUrl: 'views/main.html',
//                    controller: 'MainController',
//                    access: {
//                            authorizedRoles: [USER_ROLES.all]
//                        authorizedRoles: '*'
//                    }
//                })
            $stateProvider
                .state('main', {
                    url: '/main',
                    templateUrl: 'views/main.html',
                    controller: 'MainController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
//                .state("main.layout", {
//                    views: {
//                        'login': {
//                            templateUrl: 'views/login.html',
//                            controller: 'LoginController'
//                        },
//                        'test': {
//                            templateUrl: 'views/error.html'
//                        }
//                    }
//                })
                .state('login', {
                    url: '/login',
                    templateUrl: 'views/login.html',
                    controller: 'LoginController',
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
                })


            // GLOBAL MESSAGES
            // http://blog.tomaka17.com/2012/12/random-tricks-when-using-angularjs/
            var elementsList = $();
            var showMessage = function(content, cl, time, type) {
                $('<div class="voffset3"><button type="button" class="close" data-dismiss="alert">&times;</button></div>')
                    .addClass('message')
                    .addClass(cl)
                    .addClass('alert')
                    .addClass(type)
                    .hide()
                    .fadeIn('fast')
                    .delay(time)
                    .fadeOut('fast', function() { $(this).remove(); })
                    .appendTo(elementsList)
                    .text(content);
            };

            $httpProvider.responseInterceptors.push(function($timeout, $q) {
                return function(promise) {
                    var errorInterval = 3000;
                    var successInterval = 1000;

                    return promise.then(function(successResponse) {
                        if (successResponse.config.method.toUpperCase() != 'GET') {
                            var alertType = 'alert-success';
                            showMessage('Successful', 'successMessage', successInterval, alertType);
                        }
                        return successResponse;

                    }, function(errorResponse) {
                        var alertType = 'alert-danger';
                        switch (errorResponse.status) {
                            case 401:
                                showMessage('Wrong usename or password', 'errorMessage', errorInterval, alertType);
                                break;
                            case 403:
                                showMessage('You don\'t have the right to do this', 'errorMessage', errorInterval, alertType);
                                break;
                            case 500:
                                showMessage('Server internal error ' + errorResponse.status + ': ' + errorResponse.message, 'errorMessage', errorInterval, alertType);
                                break;
                            default:
                                showMessage('Error ' + errorResponse.status + ': ' + errorResponse.message, 'errorMessage', errorInterval, alertType);
                        }
                        return $q.reject(errorResponse);
                    });
                };
            });

            $compileProvider.directive('appMessages', function() {
                var directiveDefinitionObject = {
                    link: function(scope, element, attrs) { elementsList.push($(element)); }
                };
                return directiveDefinitionObject;
            });

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
        .run(['$rootScope', '$state',
        function ($rootScope, $state) {
            $state.transitionTo('main');
        }])
        .run(['$rootScope', '$location', '$http', 'AuthenticationSharedService', 'Session', 'USER_ROLES',
            function($rootScope, $location, $http, AuthenticationSharedService, Session, USER_ROLES) {
                $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                    $rootScope.isAuthorized = AuthenticationSharedService.isAuthorized;
                    $rootScope.userRoles = USER_ROLES;
                    AuthenticationSharedService.valid(toState.access.authorizedRoles);
                });

                // Call when the the client is confirmed
                $rootScope.$on('event:auth-loginConfirmed', function(data) {
                    $rootScope.authenticated = true;
//                    if ($location.path() === "/login") {
                        $location.path('/main').replace();
//                    }
                });

                // Call when the 401 response is returned by the server
                $rootScope.$on('event:auth-loginRequired', function(rejection) {
                    Session.invalidate();
                    $rootScope.authenticated = false;
                    if ($location.path() !== "/" && $location.path() !== "") {
                        $location.path('/login').replace();
                    }
                });

                // Call when the 403 response is returned by the server
                $rootScope.$on('event:auth-notAuthorized', function(rejection) {
                    $rootScope.errorMessage = 'errors.403';
                    $location.path('/error').replace();
                });

                // Call when the user logs out
                $rootScope.$on('event:auth-loginCancelled', function() {
                    $location.path('');
                });
        }]);
