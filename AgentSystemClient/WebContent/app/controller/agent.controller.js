(function() {
	angular.module("agentApp").controller("agentController", agentController);
		
	agentController.$inject = ["$http", "$scope"];
	
	function agentController($http, $scope) {

		
		$scope.startAgent = function() {
			
			var agentData = {};
			
			//zbog jednostavnosti za sad pozivamo ovu metodu
			$http.get('rest/agents/messages')
			.success(function() {
				
			})
			.error(function() {
				
			});
		}
		
	}
	
})();