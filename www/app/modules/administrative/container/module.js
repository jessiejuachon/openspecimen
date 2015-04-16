
angular.module('os.administrative.container',
  [
    'ui.router',
    'os.administrative.container.list',
    'os.administrative.container.addedit',
    'os.administrative.container.detail',
    'os.administrative.container.overview',
    'os.administrative.container.locations',
    'os.administrative.container.util',
    'os.administrative.container.map'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('containers', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) {
          // Storage Container Authorization Options
          $scope.containerResource = {
            createOpts: {resource: 'StorageContainer', operation: 'Create'},
            updateOpts: {resource: 'StorageContainer', operation: 'Update'},
            deleteOpts: {resource: 'StorageContainer', operation: 'Delete'}
          }
        },
        parent: 'signed-in'
      })
      .state('container-list', {
        url: '/containers',
        templateUrl: 'modules/administrative/container/list.html',
        controller: 'ContainerListCtrl',
        parent: 'containers'
      })
      .state('container-addedit', {
        url: '/container-addedit/:containerId?posOne&posTwo&parentContainerId&parentContainerName',
        templateUrl: 'modules/administrative/container/addedit.html',
        resolve: {
          container: function($stateParams, Container) {
            if ($stateParams.containerId) {
              return Container.getById($stateParams.containerId);
            }

            return new Container({allowedCollectionProtocols: [], allowedSpecimenClasses: [], allowedSpecimenTypes: []});
          } 
        },
        controller: 'ContainerAddEditCtrl',
        parent: 'containers'
      })
      .state('container-detail', {
        url: '/containers/:containerId',
        templateUrl: 'modules/administrative/container/detail.html',
        resolve: {
          container: function($stateParams, Container) {
            return Container.getById($stateParams.containerId);
          }
        },
        controller: 'ContainerDetailCtrl',
        parent: 'containers'
      })
      .state('container-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/container/overview.html',
        resolve: {
          childContainers: function(container) {
            return container.getChildContainers(true); 
          }
        },
        controller: 'ContainerOverviewCtrl',
        parent: 'container-detail'
      })
      .state('container-detail.locations', {
        url: '/locations',
        templateUrl: 'modules/administrative/container/locations.html',
        resolve: {
          occupancyMap: function(container) {
            return container.getOccupiedPositions();
          }
        },
        controller: 'ContainerLocationsCtrl',
        parent: 'container-detail'
      });
  });
