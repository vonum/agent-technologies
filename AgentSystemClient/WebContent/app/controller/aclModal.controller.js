(function() {
	angular.module("agentApp").controller("aclModalController", aclModalController);
		
	aclModalController.$inject = ['$uibModalInstance', '$scope', '$http', 'getPerformatives', 'getRunningAgents'];
	
	function aclModalController($uibModalInstance, $scope, $http, getPerformatives, getRunningAgents) {
		
		$scope.aclMsg = {
			"performative" : "",
			"sender" : "", //AIDS
			"receivers" : [], //AIDS list
			"replyTo" : "", //AIDS
			"content" : "",
			"contentObject" : "", //Object
			"userArgs" : {}, //HashMap
			"encoding" : "milan",
			"ontology" : "milan",
			"protocol" : "milan",
			"conversationId" : "",
			"replayWith" : "",
			"inReplayTo" : "",
			"replyBy" : ""		
		};
		
		//from map of agents to array of agents
		$scope.agents = [];
		for(var prop in getRunningAgents) {
			$scope.agents.push(getRunningAgents[prop]);
		}
		
		//SELECT FOR PERFORMATIVE
		$scope.currPerformative = "INFORM";
		$scope.performatives = getPerformatives;
		
		$scope.selectPerformative = function(p) {
			$scope.currPerformative = p;
		}
		
		//SELECT FOR SENDER
		$scope.currSender = {"name": "none"};
		
		$scope.selectSender = function(s) {
			$scope.currSender = s;
		}
		
		//SELECT FOR RECEIVER
		$scope.currReceiver = {"name" : "none"};
		
		$scope.selectReceiver = function(r) {
			$scope.currReceiver = r;
		}
		
		//SELECT FOR REPLAY TO
		$scope.currReplayTo = {"name" : "none"};
		
		$scope.selectReplayTo = function(r) {
			$scope.currReplayTo = r;
		}
		
		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		}
		
		$scope.sendMsg = function() {
			
			//setup acl data
			$scope.aclMsg.performative = $scope.currPerformative;
			$scope.aclMsg.sender = $scope.currSender;
			$scope.aclMsg.receivers.push($scope.currReceiver);
			$scope.aclMsg.replyTo = $scope.currReplayTo;
			
			console.log($scope.currReceiver);
			
			var url = "http://" + $scope.currReceiver.host.address + ":8080/AgentSystemClient/"
			
			$http.post(url + 'rest/agents/messages/', $scope.aclMsg)
			.success(function(d) {
				$scope.cancel();
			})
			.error(function(d) {
				console.log("Error loading agent types");
			});
		}
		
	}
	
})();