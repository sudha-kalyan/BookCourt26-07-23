$('document').ready(function (){

 $('#getOtp').click(function() {
       $.ajax({
         url: '/public/sendOtp',
         method: 'post',
         data: {"mobileNo": $("#mobileNo").val()},
         success: function(response) {
       alert(response);
       alert(data);
         if((response===null)&&(response==="")){
         console.log(response);
        // alert(response.msg);
        alert(response);
         }
         else{
           // Handle the successful response
           console.log(response);
//           alert(response.msg + " "  + response.otp);
         },
         error: function(xhr, status, error) {
           // Handle the error
           console.log(error);
         }
       });

})
$('#submitOtp').click(function() {
              $.ajax({
                url: '/public/checkOtp',
                method: 'post',
                data: {"mobileNo": $("#mobileNo").val(), "otp": $("#otp").val()},
                success: function(response) {
                  // Handle the successful response
                  console.log(response);
                  location.reload();
                  alert(response.msg );

                },
                error: function(xhr, status, error) {
                  // Handle the error
                  console.log(error);
                }
              });});
              })