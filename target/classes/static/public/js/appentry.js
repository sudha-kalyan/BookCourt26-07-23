$('document').ready(function (){

 $('#getOtp').click(function() {
       $.ajax({
         url: '/public/sendOtp',
         method: 'post',
         data: {"mobileNo": $("#mobileNo").val()},
         success: function(response) {
      if($("#mobileNo").val()==""){
      alert(response.msg);
      if(Nan(response.otp))
      {
      document.getElementById('otp').value=' ';
      return;
      }


      }
       if($("#mobileNo").val().length !==10){
       alert(response.msg);
        if(Nan(response.otp))
             {
             document.getElementById('otp').value=' ';
             return;
             }
      }
//      if(response.msg!=$("#mobileNo").val()){
//      alert("pls Enter Registered MobileNo");
//       if(Nan(response.otp))
//            {
//            document.getElementById('otp').value=' ';
//            return;
//            }
//
//      }

      if($("#mobileNo").val()==response.msg){
           // Handle the successful response
           console.log(response);
          alert("Sent Otp Successfully" + " "  + response.otp);
          document.getElementById('otp').value=response.otp;

          }
          else{
          alert("Pls Enter Registered MobileNo");
          }
         },
         error: function(xhr, status, error) {
           // Handle the error
           console.log(error);
         }
       });

})
$('#getcOtp').click(function() {
       $.ajax({
         url: '/public/sendcOtp',
         method: 'post',
         data: {"mobileNo": $("#mobileNo").val()},
         success: function(response) {
      if($("#mobileNo").val()==""){
      alert(response.msg);
      if(Nan(response.otp))
            {
            document.getElementById('otp').value=' ';
            return;
            }
      }
       if($("#mobileNo").val().length !==10){
       alert(response.msg);
       if(Nan(response.otp))
             {
             document.getElementById('otp').value=' ';

             return;
             }
      }
      if(response==null){
      alert(response.msg);
      document.getElementById('otp').value=" ";
      }

      if($("#mobileNo").val()!=response.msg){
           // Handle the successful response
           console.log(response);
          alert(response.msg + " "  + response.otp);
         document.getElementById('otp').value=response.otp;
          }
          else{
          alert("U Already Registerd Pls Try With Ur Credentials")}
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
                 // alert(response.msg );

                },
                error: function(xhr, status, error) {
                  // Handle the error
                  console.log(error);
                }
              });});
//              $('#get').click(function() {
//                     $.ajax({
//                       url: '/public/sendOldPassword',
//                       method: 'post',
//                       data: {"mobileNo": $("#mobileNo").val()},
//                       success: function(response) {
//                    if($("#mobileNo").val()==""){
//                    alert(response.msg);
//                    }



              })