var tronApp = angular.module('tronApp', [ 'ngRoute', 'checklist-model',
		'angular.filter', 'ui.bootstrap', 'angular-confirm', 'ngJsTree','flow','toastr','hljs','ui.bootstrap.datetimepicker','dndLists']);

tronApp.controller('DashboardController', function($scope, $rootScope, $http,
		$location, $confirm) {


	$http.get("/patches/unset").success(function(data, status){
		
	});
	
	$http.get("/patches").success(function(data, status) {
		$scope.strarrays = data;
	});
	
	$http.get("/dashboard").success(function(data, status){
		$scope.items = data;
		$scope.env = $scope.items[0];
	});
	
	$scope.updateDetails = function(item){
		console.log("XXXXXXX");
		$scope.env = item;
	}

});

tronApp.controller('UserController', function($scope, $http, toastr){
	
	$scope.user = {roles:[]};
	
	$http.get('/roles').success(function(data, status){
		$scope.roles = data;
	});
	
	$http.get('/users').success(function(data, status){
		$scope.users = data;
	});
	
	$scope.deleteUser = function(user) {
		 $scope.user = user;
		 $http.post('/users/delete', $scope.user).success(function(data, status, header){
			 toastr.success('User Deleted!', 'Alert!');
				$http.get('/users').success(function(data, status){
					$scope.users = data;
				});
		 });
	}
	
	$scope.saveUser = function(){
		$http.post('/users', $scope.user).success(function(data, status, header){
			$('#myModal').modal('toggle');
			toastr.success('Configuration Saved!', 'Alert!');
			$http.get('/users').success(function(data, status){
				$scope.users = data;
			});
		}).error(function(data, status, headers){
			toastr.error(data.message, 'Alert!');
		});
	};
	
	$scope.updateUser = function(user) {
		$scope.user = user;
		$('#myModal').modal('toggle');
	};	
	
	$scope.addUser = function(){
		$scope.user = null;
		$scope.user = {roles:[]};
		$('#myModal').modal('toggle');
	}
	

	$scope.availableRoles = function(value) {
		var perms = $scope.user.roles;
	    for (var i = 0; i < perms.length; i++) {
	        if (perms[i].name === value.name) {
	            return false;
	        }
	    }
	    return true;
	};
	
});

tronApp.controller('RolesController', function($scope, $http, toastr){
	
	$http.get('/roles').success(function(data, status){
		$scope.roles = data;
	});
	
	$http.get('/permissions').success(function(data, status){
		$scope.permissions = data;
	});
	$scope.role = {permissions:[]};
	$scope.availablePermissions = function(value) {
		var perms = $scope.role.permissions;
	    for (var i = 0; i < perms.length; i++) {
	        if (perms[i].name === value.name) {
	            return false;
	        }
	    }
	    return true;
	};
		
	
	$scope.saveRole = function(){
		$http.post('/roles', $scope.role).success(function(data, status, header){
			$('#myModal').modal('toggle');
			toastr.success('Configuration Saved!', 'Alert!');
			$http.get('/roles').success(function(data, status){
				$scope.roles = data;
			});
		}).error(function(data, status, headers){
			toastr.error(data.message, 'Alert!');
		});
	};
	
	
	
	$scope.updateRole = function(role) {
		$scope.role = role;
		$('#myModal').modal('toggle');
	};	
	
	$scope.addRole = function(){
		$scope.role = {permissions:[]};
		$('#myModal').modal('toggle');
	}
});

tronApp.controller('DBPatchController', function($scope, $window, $location, $http){
	
	$scope.isCollapsed = true;
	
	$scope.metaData = null;
	$http.get("/systables.json").success(function(data, status){
		$scope.metaData = data;
	});
	
	/*
	 * $scope.dbObjects = [{ objectType : 'Algorithm', childerns :
	 * [{table:'CI_BATCH_CTRL', inSw: 'Y', upSw: 'Y', dlSw :'N', frSw:
	 * 'Y'},{table:'CI_BATCH_CTRL_L', inSw: 'Y', upSw: 'Y', dlSw :'N', frSw:
	 * 'Y'}, {table:'CI_BATCH_CTRL_P', inSw: 'Y', upSw: 'Y', dlSw :'N', frSw:
	 * 'Y'}, {table:'CI_BATCH_CTRL_P_L', inSw: 'Y', upSw: 'Y', dlSw :'N', frSw:
	 * 'Y'}], condition : 'OWNER_FLG = \'CM\'', exclution : 'VERSION',
	 * isCollapsed : true }];
	 */
	$scope.dbObjects = [];
	
   $scope.addDbObj = function(index){
	   $scope.isCollapsed = false;
	    // var dbObject = {

	    	// objectType : $scope.objectType,
	    	
	    	// condition : $scope.condition,
	    	// exclution : $scope.exclution
	    // };
	    // $scope.dbObjects.push(dbObject);
	   var dbObject = {};
	    $scope.dbObjects.splice(index+1,0, dbObject);
   }; 
	

   $scope.changeDD = function(dbObj) {
	   console.log(dbObj);
	   var tempObj = [];
	   
	   angular.forEach(dbObj.objectType, function(o){
		   var b = {};
		   b.condition = o.condition;
		   b.exclution = o.exclution;
		   b.table = o.table;
		   b.inSw = o.inSw;
		   b.upSw = o.upSw;
		   b.dlSw = o.dlSw;
		   b.frSw = o.frSw;
		   b.objName = o.objName;
		   
		   tempObj.push(b);
	   });
	   
	   // delete dbObj.objectType;
	   dbObj.objectType = tempObj;
	   
   }
   
   
   $scope.removeDbObj = function(index){
	    $scope.dbObjects.splice(index, 1);
	  }; 
	  
	$scope.saving = false;
	$scope.proceed = function() {
		
		$scope.saving = true;
		angular.forEach($scope.dbObjects, function(dbObject){
			
			delete dbObject.isCollapsed;
			
		});
		$http.post('/db-objects',{
			
			data: $scope.dbObjects
		}).
		success(function(data, status, headers){
			$scope.saving = false;
			$location.path('/review');
		});
		
		// $location.path('/review');
	}
	
	$scope.goBack = function() {
		$window.history.back();
	}
	
   
});



tronApp.controller('CreatePatchController', function($scope, $rootScope, $location, $http, toastr){
	
	$scope.environments = null;
	
	$scope.prereqs = [];
	$scope.prer = {};
	
	$scope.bundles = [];
	
	$http.get("/environments").success(function(data, status){
		$scope.environments = data;
	});
	
    $scope.addPreq = function(index){
	    console.log("Adding Prequ");
	    var prereq = {};
	    prereq.packageName = "";
	    $scope.prereqs.splice(index+1,0, prereq);
    }; 
    
    $scope.removePreq = function(index){
	    $scope.prereqs.splice(index, 1);
	}; 

	$scope.handleFileAdd = function ($file, $event, $flow) {
		  console.log("flow...");
		  console.log($file.file.name);
		  // console.log(flowFile.d.files[0].file.name);
		  var prereq = {};
		  prereq.packageName = $file.file.name;
		 $scope.prereqs.push(prereq);
	};
	
	$scope.handleBundleAdd = function ($file, $event, $flow) {
		  console.log("flow...");
		  console.log($file.file.name);
		  // console.log(flowFile.d.files[0].file.name);
		  var bundle = {};
		  bundle.fileName = $file.file.name;
		 $scope.bundles.push(bundle);
	};	

	$scope.deleteFromServer = function($files, file) {
		// console.log($files);
		console.log(file);
		$http.post('/delete-prereq',{
			fileName: file.name
		}).success(function(data, status, header) {

		var found = false;
		for (i = 0; i < $scope.prereqs.length; i++) {
			if ($scope.prereqs[i].packageName == file.name) {
				found = true;
				break;
			}
		}

		if (found) {
			$scope.prereqs.splice(i, 1);
		}

		index = $files.indexOf(file);
		if (index > -1)
			$files.splice(index, 1);
			toastr.success('File Deleted from the Server!', 'Alert!');
		});
		

		
	}
	
	$scope.proceed = function() {
		
		$rootScope.patch = $scope.patch;
		
		$http.post('/patches', {
			id   : $scope.patch.id,
			name : $scope.patch.type+$scope.patch.module+"."+$scope.patch.version,
			desc : $scope.patch.desc,
			dest : $scope.patch.dest,
			env  : $scope.patch.env,
			createdBy : $scope.patch.createdBy,
			isAppIncl : $scope.patch.hasAppPatch,
			isDbIncl  : $scope.patch.hasDbPatch,
			isBundleIncl : $scope.patch.hasBundle,
			moduleNbr    : $scope.patch.module,
			version      : $scope.patch.version,
			type         : $scope.patch.type,
			status       : 'Created'
		}).success(function(data, status, header){
			$scope.patch = data;
			$rootScope.patch = data;
			
			$http.post('/patches/add-bundle',{
				data: $scope.bundles
			}).success(function(data, status, header){
				
			});
			
			$http.post('/patches/add-prereq', {
				data: $scope.prereqs
			}).success(function(data, status, header){
				console.log("Records Inserted");
			});
			
		});
		
	
		if($scope.patch.hasAppPatch)
			$location.path('/apppatch');
		else if($scope.patch.hasDbPatch)
			$location.path('/dbpatch');
	}
});

tronApp.controller('AppPatchController', function($scope, $rootScope, $location, $window, $http){
	
	
	$scope.selectedNode = null;
	$scope.selectedRightNode = null;
	
	$scope.treeConfig = {
            core : {
                multiple : false,
                animation: true,
                error : function(error) {
                    $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
                },
                check_callback : true,
                worker : true
            },
            version : 1
        };
	
	
	$scope.treeModelLeft = [{
		"id"     :"code",
		"parent" :"#",
		"text"   :"code"
	},{
		"id"     : "cobol",
		"parent" : "code",
		"text"   : "cobol"
	},{
		"id"     : "etc",
		"parent" : "code",
		"text"   : "etc"
	},{
		"id"     : "java",
		"parent" : "code",
		"text"   : "java"
	},{
		"id"     : "services",
		"parent" : "code",
		"text"   : "services"
	},{
		"id"     : "splapp",
		"parent" : "code",
		"text"   : "splapp"
	}];
	
	
	$scope.treeModelRight = [{
		"id"     :"code",
		"parent" :"#",
		"text"   :"code"
	},{
		"id"     : "cobol",
		"parent" : "code",
		"text"   : "cobol"
	},{
		"id"     : "etc",
		"parent" : "code",
		"text"   : "etc"
	},{
		"id"     : "java",
		"parent" : "code",
		"text"   : "java"
	},{
		"id"     : "services",
		"parent" : "code",
		"text"   : "services"
	},{
		"id"     : "splapp",
		"parent" : "code",
		"text"   : "splapp"
	}];
	
	
	
	$scope.treeModelLeft3 = [{
		  "id": "cm",
		  "parent": "#",
		  "text": "cm"
		}, {
		  "id": "api",
		  "parent": "cm",
		  "text": "api"
		}, {
		  "id": "domain",
		  "parent": "cm",
		  "text": "domain"
		}, {
		  "id": "batch",
		  "parent": "domain",
		  "text": "batch"
		},
		{
		"id": "CmLockedMeterShowingConsumption_BatchProcess.java",
		"parent": "batch",
		"text": "CmLockedMeterShowingConsumption_BatchProcess.java",
		"icon": "jstree-icon jstree-file"
		},
		{
			"id": "CmOpenCloseDepositTenderControl_BatchProcess.java",
			"parent": "batch",
			"text": "CmOpenCloseDepositTenderControl_BatchProcess.java",
			"icon": "jstree-icon jstree-file"
		},
		{
			"id": "CmPledgePayReconIn_BatchProcess.java",
			"parent": "batch",
			"text": "CmPledgePayReconIn_BatchProcess.java",
			"icon": "jstree-icon jstree-file"
		},
		{
			  "id": "payment",
			  "parent": "domain",
			  "text": "payment"
		},
		{
			  "id": "tenderControl",
			  "parent": "payment",
			  "text": "tenderControl"
		},	
		{
			"id": "CmCheckDuplicatePayment.java",
			"parent": "tenderControl",
			"text": "CmCheckDuplicatePayment.java",
			"icon": "jstree-icon jstree-file"
			},
			{
				"id": "PaymentTender_CHandler.java",
				"parent": "tenderControl",
				"text": "PaymentTender_CHandler.java",
				"icon": "jstree-icon jstree-file"
				},			
		];
	
	$scope.treeModelRight3 = [{
		  "id": "cm",
		  "parent": "#",
		  "text": "cm"
		}, {
		  "id": "api",
		  "parent": "cm",
		  "text": "api"
		}, {
		  "id": "domain",
		  "parent": "cm",
		  "text": "domain"
		}, {
		  "id": "batch",
		  "parent": "domain",
		  "text": "batch"
		},
		{
		"id": "CmLockedMeterShowingConsumption_BatchProcess.java",
		"parent": "batch",
		"text": "CmLockedMeterShowingConsumption_BatchProcess.java",
		"icon": "jstree-icon jstree-file"
		}];	
	
	$scope.addNewNode = function() {
		$scope.treeModelRight.push({ id : '44321', parent : $scope.newNode.parent, text : $scope.newNode.text });
    };
	
	$scope.applyModelChanges = function() {
		console.log('Setting Apply Changes as True');
		return true;
	};
	
	$scope.selectNodeCB = function(e, list){
		console.log(list.node);
		$scope.selectedNode = list.node;
		console.log('Pushed Value..');
		
		$http.post("/list-files", {path:list.node.id}).success(function(data, status){
			
			var i = $scope.treeModelLeft.length;
			while (i--){
				if($scope.treeModelLeft[i].parent == list.node.id) {
					$scope.treeModelLeft.splice(i, 1);
			    }
			}
			
		
			angular.forEach(data, function(fileObj){
				console.log(fileObj);
				$scope.treeModelLeft.push(fileObj);
			});
		});
		
		
	};
	
	$scope.moveRight = function() {
		
		$http.post("/parents-childerns", {path:$scope.selectedNode.id}).success(function(data, status){
			
			var i = data.length;
			
			while(i--) {
				fileObj = data[i];
				console.log(fileObj);
				var nodeFound = false;
				angular.forEach($scope.treeModelRight, function(rightNode){
					if(rightNode.id == fileObj.id) {
						nodeFound = true;
						console.log("Node Found...");
					}
				});
				if(!nodeFound) {
					console.log('Adding node to Right');
					$scope.treeModelRight.push(fileObj);
				}				
			}
			
			setTimeout(function(){
				console.log("Tree Instance");
				console.log($scope.treeInstanceTwo);
				$scope.treeInstanceTwo.jstree(true).open_all();				
			},500);	

			
		});
		

		// $scope.treeModelRight.push($scope.selectedNode);
		// $scope.reCreateTree();
	};
	
	$scope.selectRightNodeCB = function(e, list){
		$scope.selectedRightNode = list.node;
		console.log(list.node);
	}
	
	$scope.moveLeft = function() {
		
		/*
		 * console.log('Going to Remove');
		 * console.log($scope.selectedRightNode.id);
		 * console.log($scope.selectedRightNode.children_d);
		 */
		// Remove all Childrens First
		angular.forEach($scope.selectedRightNode.children_d, function (childNode) {
			console.log(childNode);
			angular.forEach($scope.treeModelRight, function(rightNode){
			if(rightNode.id == childNode || rightNode.id == $scope.selectedRightNode.id)
				{
					var index = $scope.treeModelRight.indexOf(rightNode);
					console.log('Going to Remove >>>'+index);
					if (index > -1) {
						console.log('Going to Remove '+index);
						$scope.treeModelRight.splice(index, 1);
					}	
				}
			})
		});
		
		// Remove Self If no Childern are present
		angular.forEach($scope.treeModelRight, function(rightNode){
			if(rightNode.id == $scope.selectedRightNode.id)
				{
					var index = $scope.treeModelRight.indexOf(rightNode);
					console.log('Going to Remove >>>'+index);
					if (index > -1) {
						console.log('Going to Remove '+index);
						$scope.treeModelRight.splice(index, 1);
					}	
				}
		});		

	}
	
	$scope.treeConfig = {
		    core : {
		        multiple : false,
		        animation: true,
		        error : function(error) {
		            $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
		        },
		        check_callback : true,
		        worker : true
		    },
		    version : 1
		};
		$scope.reCreateTree = function() {
		    this.treeConfig.version++;
		}
	
	
	$scope.proceed = function() {
		console.log('I am in');
		
		$http.post('/app-objects', {
			data: $scope.treeModelRight
		}).success(function(data, status, header){
			console.log("Records Inserted");
		});
		console.log($rootScope.patch);
		if($rootScope.patch.isDbIncl)
			$location.path('/dbpatch');
		else
			$location.path('/review');
			
	}
	
	$scope.goBack = function() {
		$window.history.back();
	}
	
});


tronApp.controller('ReviewController', function($scope,$http, $rootScope, $location){
	
	$scope.getPatch = function() {
		$http.get('/patches/details').success(function(data, status){
			$scope.patch = data;
		});
		
		$http.get('/app-objects').success(function(data, status){
			$scope.selectedFiles = data;
		});
	}
	
	$scope.createAndDownloadZip = function() {
		$http.post('/patches/download-package').success(function(data, status){
			
		});
	};
	
	$scope.getPatch();
	
	$scope.inputFileUrl = function(){
		return '../storage/'+$rootScope.patch.name+'/input.inp';
	};
	
	$scope.selectedFiles = [{
		'file': 'CmLockedMeterShowingConsumption_BatchProcess.java',
		'folder': 'cm/com/splwg/cm/domain/batch'
	},{
		'file': 'CmOpenCloseDepositTenderControl_BatchProcess.java',
		'folder': 'cm/com/splwg/cm/domain/batch'
	},{
		'file': 'CmCheckDuplicatePayment.java',
		'folder': 'cm/com/splwg/cm/domain/payment/tenderControl'
	}]
	
});

tronApp.controller('AppObjectTypeController', function($scope, $rootScope,
		$http, $location, $confirm) {

	$scope.assessmentsTodelete = {
		assessments : []
	};

	$http.get("/appObjType").success(function(appObjTypes, status) {
		$scope.appObjTypes = appObjTypes;
	});

});

tronApp.controller('EnvironementeController', function($scope, $rootScope,
		$http, $location, $confirm, $window,hexafy, toastr) {

	$scope.environmentToShow = {
		environments : []
	};
	
	$scope.loadData = function() {
		
		$http.get("/environments").success(function(environments, status) {
			$scope.environments = environments;
		});		
	};
	
	$scope.updateEnvironment = function updateEnvironment(environment){
		$scope.environment = environment;
		$('#myModal').modal('toggle');
	}
	
	$scope.addEnvironment = function addEnvironment(){
		$scope.environment = null;
		$('#myModal').modal('toggle');
	}
	
	// initail load
	$scope.loadData();


	$scope.saveEnvironment = function saveEnvironment() {
		$http.post('/environments', $scope.environment).success(
				function(data, status, headers) {
					// $window.location.reload();;
					// alert('Configuration Saved');
					$('#myModal').modal('toggle');
					toastr.success('Configuration Saved!', 'Alert!');
					$scope.loadData();

				}).error(function(data, status, headers) {
			// alert('Configuration Error');
					toastr.error(data.message, 'Alert!');
		});
	}

	$scope.deleteEnvironment = function deleteEnvironment(environment) {
		$http.post('/environment/delete', environment).success(function(data, status, headers) {
					console.log(">> Deleted..");
					toastr.success('Configuration Deleted!', 'Alert!');
					$scope.loadData();
				}).error(function(data, status, headers) {
					toastr.error(data.message, 'Error Deleting Entry!');
			
		});
	}

});


tronApp.controller('AppliedPackagesController', function($scope, $http, $interval, toastr){
	$scope.environments = null;
	$scope.environment = null;
	$scope.packageList = [];
	
	$http.get("/environments").success(function(data, status){
		$scope.environments = data;
	});
	
	$scope.setEnv = function(environment){
		$http.post('/environment/set', $scope.env).success(function(data, status, headers){
			$scope.environment = data;
			$scope.retrievePackageList();
			console.log($scope.treeInstancePackage.jstree(true));
			
			
			setTimeout(function(){
				$scope.treeInstancePackage.jstree(true).open_all();				
			},500);	
			
		});
	};
	
	$scope.sourcecode = "";
	$scope.lang = "";
	
	  $scope.$watch("search", function(newValue, oldValue) {
		    console.log($scope.search);
		    console.log($scope.treeInstancePackage.jstree(true));
		    $scope.treeInstancePackage.jstree(true).search($scope.search);	
	  });
	
	$scope.retrievePackageList = function(){
		$http.post("/environment/list-packages", {path:"root"}).success(function(data, status){
			
			var i = $scope.treeModelPackage.length;
			while (i--){
				if($scope.treeModelPackage[i].parent == "root") {
					$scope.treeModelPackage.splice(i, 1);
			    }
			}
			
		
			angular.forEach(data, function(fileObj){
				console.log(fileObj);
				$scope.treeModelPackage.push(fileObj);
			});
			
			
		});
	};
	
	$scope.treeConfig = {
            core : {
                multiple : false,
                animation: true,
                error : function(error) {
                    $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
                },
                check_callback : true,
                worker : true,
            },
			plugins : ["search"],
	        search: {
	            "case_insensitive": true,
	            "show_only_matches" : true
	        },
            version : 1
        };
	
	
	$scope.treeModelPackage = [{
		"id"     :"root",
		"parent" :"#",
		"text"   :"Applied Packages"
	}];
	
	
	$scope.selectPackageNodeCB = function(e, list){
		console.log(list.node);
		$scope.selectedNode = list.node;
		console.log('Pushed Value..');
		if(list.node.icon == "jstree-icon jstree-file")
			{
			$scope.sourcecode = " -- Loading --";
			$scope.lang = "sql";
				$http.post("/environment/retrieve-source", {path:list.node.id}).success(function(data, status){
					$scope.sourcecode = data.code;
					$scope.lang = data.lang;
				});
			}
		else
			$http.post("/environment/list-packages", {path:list.node.id}).success(function(data, status){
				
				var i = $scope.treeModelPackage.length;
				while (i--){
					if($scope.treeModelPackage[i].parent == list.node.id) {
						$scope.treeModelPackage.splice(i, 1);
				    }
				}
				
			
				angular.forEach(data, function(fileObj){
					console.log(fileObj);
					$scope.treeModelPackage.push(fileObj);
				});
			});
	
	}
});


tronApp.controller('CompareEnvironController', function($scope, $http){
	
	$scope.sourcePackages = null;
	$scope.targetPackages = null;
	
	$scope.sourceEnvList = null;
	$scope.targetEnvList = null;
	
	$http.get("/environments").success(function(data, status){
		$scope.sourceEnvList = data;
		$scope.targetEnvList = data;
	});
	
	$scope.compareEnv = function(){
		$http.post('/environment/compare-environment',{sourceEnv:$scope.sourceE, targetEnv:$scope.targetE}).success(function(data, status){
			$scope.sourcePackages = data;
		});
	}
	
	
});

tronApp.controller('GeneralSettings', function($scope, $http, toastr){
	
	$http.get('/general-settings').success(function(data, status){
		$scope.settings = data;
	});
	
	$scope.updateSetting = function(setting) {
		$http.post('/general-settings', setting).success(function(data, status){
			toastr.success('Setting saved successfully','Alert');
		});
	};
});

tronApp.controller('ApplyPatchController', function($scope, $http, $interval, toastr) {
	
	$scope.environments = null;
	$scope.environment = null;
	$scope.packageList = [];
	$scope.applying = false;
	$scope.isPackageExist = false;
	
	$scope.notifications = [];
	
	
	
	$http.get("/environments").success(function(data, status){
		$scope.environments = data;
	});
	
	$scope.setEnv = function(environment){
		$http.post('/environment/set', $scope.env).success(function(data, status, headers){
			$scope.environment = data;
		});
	};
	
	$scope.handleFileAdd = function ($file, $event, $flow) {
		  console.log("flow...");
		  console.log($file.file.name);
		  var release = {};
		  release.fileName = $file.file.name;
		  $scope.packageList.push(release);
		  
		  $http.post('/apply-package/check-package', release.fileName).success(function(data, status, headers) {
			  $scope.isPackageExist = data.FileExist;
		  });
	};
	
	$scope.checkStatus = function () {
		
		if($scope.applying) {
			$http.get('/apply-package/status').success(function(data, status, headers) {
				$scope.notifications = data
			});
		}
	}
	
	$scope.applyPatch =  function(){
		$scope.applying = true;
		
		$interval(function(){
			$scope.checkStatus();
		}, 3000);
		
		$http.post('/apply-package', $scope.packageList).success(function(data, status, headers) {
			
			$http.get('/apply-package/status').success(function(data, status, headers){
				$scope.notifications = data
			});
			$scope.applying = false;
		}).error(function(data){
			toastr.error(data.message,"Error! ");
		});
	};
	
});

tronApp.controller('CreateServicePackController', function($scope, $http, toastr){
	
	$scope.packages = [];
	
    $scope.addPackage = function(index){
	    var pack = {};
	    pack.packageName = "";
	    $scope.packages.splice(index+1,0, prereq);
    }; 
    
    $scope.removePackage = function(index){
	    $scope.packages.splice(index, 1);
	}; 

	$scope.handleFileAdd = function ($file, $event, $flow) {
		  var pack = {};
		  pack.packageName = $file.file.name;
		 $scope.packages.push(pack);
	};
	
	$scope.createServicePack = function() {
		
		$http.post('/set-sp', {sp: $scope.spName}).success(function(){
			$http.post('/create-sp', $scope.packages).success(function(data, status, headers){
				toastr.success('Service Pack created successfully','Alert');
			});
		});

	};
	
	$scope.deleteFromServer = function($files, file) {
		// console.log($files);
		console.log(file);
		$http.post('/delete-prereq',{
			fileName: file.name
		}).success(function(data, status, header) {

		var found = false;
		for (i = 0; i < $scope.packages.length; i++) {
			if ($scope.packages[i].packageName == file.name) {
				found = true;
				break;
			}
		}

		if (found) {
			$scope.packages.splice(i, 1);
		}

		index = $files.indexOf(file);
		if (index > -1)
			$files.splice(index, 1);
			toastr.success('File Deleted from the Server!', 'Alert!');
		});
		
	};
	
});

tronApp.controller('ApplyServicePackController', function($scope, $http, $interval, toastr){
	$scope.environments = null;
	$scope.environment = null;
	$scope.packageList = [];
	$scope.applying = false;
	$scope.isPackageExist = false;
	$scope.notifications = [];
	
	$http.get("/environments").success(function(data, status){
		$scope.environments = data;
	});
	
	$scope.setEnv = function(environment){
		$http.post('/environment/set', $scope.env).success(function(data, status, headers){
			$scope.environment = data;
		});
	};	

	$scope.handleFileAdd = function ($file, $event, $flow) {
		  var release = {};
		  release.fileName = $file.file.name;
		  $scope.packageList.push(release);
	};	
	
	$scope.checkStatus = function () {		
		if($scope.applying) {
			$http.get('/apply-package/status').success(function(data, status, headers) {
				$scope.notifications = data
			});
		}
	};	
	
	$scope.applySP =  function(){
		$scope.applying = true;
		
		$interval(function(){
			$scope.checkStatus();
		}, 3000);
		
		$http.post('/apply-package/service-pack', $scope.packageList).success(function(data, status, headers) {
			
			$http.get('/apply-package/status').success(function(data, status, headers){
				$scope.notifications = data
			});
			$scope.applying = false;
		}).error(function(data){
			toastr.error(data.message, 'Error!');
			$scope.applying = false;
		});
	};	
	
});

tronApp.controller('SchedulerController', function($scope, $http, $interval, toastr){
	
	$scope.environment = null;
	
	$http.get("/environments").success(function(data, status){
		$scope.environments = data;
	});
	
	$scope.setEnv = function(environment){
		$http.post('/environment/set', $scope.env).success(function(data, status, headers){
			$scope.environment = data;
		});
	};	
	
	$scope.saveChanges = function(){
		$http.post('/environments', $scope.environment).success(
				function(data, status, headers) {
					toastr.success('Scheduler Saved!', 'Alert!');

				}).error(function(data, status, headers) {
					toastr.error(data.message, 'Alert!');
		});
	}
	
});


tronApp.service('hexafy', function() {
    this.myFunc = function (x) {
    	$('#errorPopup').modal('toggle');
    }
});

tronApp.controller('ModalDemoCtrl', function ($scope, $uibModal, $log) {

	  $scope.items = ['item1', 'item2', 'item3'];

	  $scope.animationsEnabled = true;

	  $scope.open = function (size) {

	    var modalInstance = $uibModal.open({
	      animation: $scope.animationsEnabled,
	      templateUrl: 'myModalContent.html',
	      controller: 'ModalInstanceCtrl',
	      size: size,
	      resolve: {
	        items: function () {
	          return $scope.items;
	        }
	      }
	    });

	    modalInstance.result.then(function (selectedItem) {
	      $scope.selected = selectedItem;
	    }, function () {
	      $log.info('Modal dismissed at: ' + new Date());
	    });
	  };

	  $scope.toggleAnimation = function () {
	    $scope.animationsEnabled = !$scope.animationsEnabled;
	  };

	});

	// Please note that $uibModalInstance represents a modal window (instance)
	// dependency.
	// It is not the same as the $uibModal service used above.

tronApp.controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, items) {

	  $scope.items = items;
	  $scope.selected = {
	    item: $scope.items[0]
	  };

	  $scope.ok = function () {
	    $uibModalInstance.close($scope.selected.item);
	  };

	  $scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	  };
	});

/*
 * ---- Routing Configurations ----
 * 
 * All Routing Configurations will provided here
 * 
 */
tronApp.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : './pages/dashboard.html',
		controller : 'DashboardController'
	}).when('/appObjType', {
		templateUrl : './pages/databaseTemplate.html',
		controller : 'AppObjectTypeController'
	})
	.when('/createpatch', {
		templateUrl: './pages/createpatch.html',
		controller: 'CreatePatchController'
	})
	.when('/createsp', {
		templateUrl: './pages/createsp.html',
		controller: 'CreateServicePackController'
	})
	.when('/dbpatch', {
		templateUrl: './pages/dbpatch.html',
		controller: 'DBPatchController'
	})
	
	.when('/apppatch', {
		templateUrl: './pages/apppatch.html',
		controller: 'AppPatchController'
	})	
	.when('/applypatch', {
		templateUrl: './pages/applypatch.html',
		controller: 'ApplyPatchController'
	})
	.when('/applysp', {
		templateUrl: './pages/applysp.html',
		controller: 'ApplyServicePackController'
	})	
	.when('/review', {
		templateUrl: './pages/review.html',
		controller: 'ReviewController'
	})		
	.when('/manage-env', {
		templateUrl : './pages/environment.html',
		controller : 'EnvironementeController'
	}).when('/testTree', {
		templateUrl : './pages/treeTest.html',
		controller : 'TreeController'
	}).when('/applied-packages', {
		templateUrl : './pages/appliedpackages.html',
		controller : 'AppliedPackagesController'
	}).when('/compare-environ',{
		templateUrl : './pages/compareEnv.html',
		controller : 'CompareEnvironController'
	}).when('/general-settings',{
		templateUrl : './pages/generalsettings.html',
		controller : 'GeneralSettings'
	}).when('/scheduler',{
		templateUrl : './pages/scheduler.html',
		controller  : 'SchedulerController'
	}).when('/manage-role', {
		templateUrl : './pages/roles.html',
		controller : 'RolesController'
	}).when('/manage-users',{
		templateUrl : './pages/users.html',
		controller : 'UserController'
	})

});

tronApp.config(function(toastrConfig){
	angular.extend(toastrConfig, {
		newestOnTop: true,
		positionClass: 'toast-bottom-right'
	});
	
});

tronApp.filter('highlight', function($sce) {
	  return function(input, lang) {
		console.log(lang)
	    if (input && lang) return hljs.highlight(lang, input).value;
	    return input;
	  }
	}).filter('unsafe', function($sce) { return $sce.trustAsHtml; })
