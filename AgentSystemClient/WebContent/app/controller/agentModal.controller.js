(function() {
	angular.module("agentApp").controller("agentModalController", agentModalController);
		
	agentModalController.$inject = ['$uibModalInstance', '$scope', '$http', 'getAgentType'];
	
	function agentModalController($uibModalInstance, $scope, $http, getAgentType) {
		
		$scope.name = "";
		
		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		}
		
		
		$scope.addAgent = function() {
			
			var agentData = {"type" : {"name" : getAgentType.type, "module" : getAgentType.module},
					"name" : $scope.name};
			
			$http.put('rest/agents/running', agentData)
			.success(function() {
				$uibModalInstance.dismiss('cancel'); //close the dialog
			})
			.error(function() {
				console.log("Error adding agent");
			});
		}
	}
	
})();