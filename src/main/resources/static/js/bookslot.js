function showSlots(code){
    $("."+code).toggle();
        $(".request-"+code).toggle();

}
function approveSlot(id){


    $.ajax({
        url:'/approveSlot',
        method:'post',
        data: {'id':id,'remarks':$('#remarksAdm-'+id).val(),'autoReject':$('#autoReject').val()},
        success: function (response){
            if (response.status == 202){
            location.reload();
            }
            alert(response.msg);
        }
       })
}
function notifySlot(id){


    $.ajax({
        url:'/notifySlot',
        method:'post',
        data: {'id':id},
        success: function (response){
            alert(response.msg);

        }
       })
}

function rejectSlot(id){


    $.ajax({
        url:'/rejectSlot',
        method:'post',
        data: {'id':id,'remarks':$('#remarksAdm-'+id).val()},
        success: function (response){
            if (response.status == 202){
            location.reload();
            }
            alert(response.msg);
        }
       })
}
function placeRequestModel(slotCode, userId,userMobile, slotId){
    $("#MslotCode").html(slotCode);
    $("#MuserId").html(userId);
    $("#MuserMobile").html(userMobile);
    $("#MslotId").html(slotId);
}


function placeRequestOK(){
       const myModal = document.getElementById('modal');
       const myInput = document.getElementById('referredBy');
        $('#modal').modal('hide');

            console.log( $("#bookingDate").html());
            $.ajax({
                    url:'/requestSlot',
                    method:'post',
                    data: {'slotCode': $("#MslotCode").html(), 'userId': $("#MuserId").html(),'slotId': $("#MslotId").html(), 'date': $("#bookingDate").html(), 'remarks': $("#remarks").val(),'referredBy': $("#referredBy").val(),'gameMode': $("#gameMode").val(),'userMobile': $("#MuserMobile").html()},
                    success: function (response){
                        if (response.status == 202){
                        window.location.pathname = "/myRequests";
                        }
                        alert(response.msg);
                    }
                   })

}

$("document").ready(
function(){
    $("#dateacc, #courtacc").change(
        function(){
        $("#"+$('#dateacc').attr('orig')).toggle();
               $("#"+$('#dateacc').val()+'--'+ $('#courtacc').val()).toggle();

        $('#courtacc').attr('orig',$('#dateacc').val()+'--'+ $('#courtacc').val());
        $('#dateacc').attr('orig',$('#dateacc').val()+'--'+ $('#courtacc').val());

        })
    }

)
function showRequests(slotCode,courtCode){
 $(".requests").css("display", "none");

    if ($('#gameMode').val() == "All"){
                $(".request-"+slotCode+"-"+"Singles").toggle();
                $(".request-"+slotCode+"-"+"Doubles").toggle();

            }else{
                $(".request-"+slotCode+"-"+$('#gameMode').val()).toggle();
            }

 }


 $("document").ready(function (){
 $("#autoReject").click(function (){
    if ($("#autoReject").val() == "true"){
         $("#autoReject").val("false");
    }else{
        $("#autoReject").val("true");
    }
     console.log($("#autoReject").val());
 })

 $("#gameMode").change(function(){
 alert(123);
     $(".requests").css("display", "none");
            console.log($('#activeSlotCourt').val());
            if ($('#gameMode').val() == "All"){
                $($('#activeSlotCourt').val()+"Singles").toggle();
                $($('#activeSlotCourt').val()+"Doubles").toggle();

            }else{
                $($('#activeSlotCourt').val()+$('#gameMode').val()).toggle();
            }
     })



 })