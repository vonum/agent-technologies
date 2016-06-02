(function() {
	angular.module("agentApp").controller("agentModalController", agentModalController);
		
	agentModalController.$inject = ['$uibModalInstance', '$scope', '$http', 'getAgentType', 'getDataStream'];
	
	function agentModalController($uibModalInstance, $scope, $http, getAgentType, getDataStream) {
		
		$scope.name = "";
		
		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		}
		
		
		$scope.addAgent = function() {
			
			var agentData = {"type" : {"name" : getAgentType.type, "module" : getAgentType.module},
					"name" : $scope.name};
			
			if(getDataStream.dataStream != null) {
				var socketMsg = "startAgent:" + JSON.stringify(agentData);
				getDataStream.dataStream.send(socketMsg);
				$uibModalInstance.dismiss('cancel');
			} else {
				$http.put('rest/agents/running', agentData)
				.success(function() {
					$uibModalInstance.dismiss('cancel'); //close the dialog
				})
				.error(function() {
					console.log("Error adding agent");
				});
			}
				
		}
	}
	
})();