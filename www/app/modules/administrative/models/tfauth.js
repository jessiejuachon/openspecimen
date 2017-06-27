angular.module('os.administrative.models.tfauth', ['os.common.models'])
  .factory('Tfauth', function(osModel, $http, ApiUtil) {
    var Tfauth = new osModel('tfauth');

    Tfauth.prototype.getType = function() {
      return 'tfauth';
    }

    Tfauth.sendQrCode = function(user) {
        console.log(Tfauth.url() + 'reset-otp');
      return $http.post(Tfauth.url() + 'reset-otp', user).then(ApiUtil.processResp);
    }

    return Tfauth;
    });