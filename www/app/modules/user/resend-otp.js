angular.module('openspecimen')
  .controller('ResendOtpCtrl', function($scope, Tfauth, $translate)  {
  
    $scope.user = {};
    $scope.response = {};

    var onSendMail = function(result) {
      $scope.response.status = result.status;
      if (result.status == 'ok') {
        $scope.response.message = 'resend_otp.reset_email_sent';
      } else {
        $scope.response.message = 'resend_otp.invalid_email_address';
      }   
    }

    $scope.sendQrCode = function() {
      Tfauth.sendQrCode($scope.user).then(onSendMail);
    }
  });
