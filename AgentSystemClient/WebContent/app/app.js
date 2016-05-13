var agentApp = angular.module("agentApp", ["ui.bootstrap", "ngRoute", "ngWebSocket", "ngCookies"]);

agentApp.config(function($routeProvider) {
	$routeProvider
	.when('/', {
		templateUrl: "app/views/cluster.html",
		controller: "clusterController"
	})
	.when('/agent', {
		templateUrl: "app/views/agent.html",
		controller: "agentController"
	})
	.when('/cluster', {
		templateUrl: "app/views/cluster.html",
		controller: "clusterController"
	});
});
