$(document).ready(function() {
  var currentDate = new Date();
  function generateCalendar(d) {
    function monthDays(month, year) {
      var result = [];
      var days = new Date(year, month, 0).getDate();
      //console.log(days);
      for (var i = 1; i <= days; i++) {
        result.push(i);
      }
      return result;
    }
    Date.prototype.monthDays = function() {
      var d = new Date(this.getFullYear(), this.getMonth() + 1, 0);
      return d.getDate();
    };


    var details = {
      // totalDays: monthDays(d.getMonth(), d.getFullYear()),
      totalDays: d.monthDays(),
      weekDays: ['Sun', 'Mon', 'Tue', 'Wed', 'Thur', 'Fri', 'Sat'],
      months: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
    };
    var response = {"datesMap":{}};
    $.ajax({
        url:'/getMonthDays',
        method:'post',
        data: {"month": d.getMonth() + 1,"year":d.getFullYear() },
        success: function (responses){
        //console.log(responses);
        response = responses;
        //console.log(response);
        }
           }).done(function(){
           var start = new Date(d.getFullYear(), d.getMonth()).getDay();

               var cal = [];
               var day = 1;
               for (var i = 0; i <= 6; i++) {
                 cal.push(['<tr>']);
                 for (var j = 0; j < 7; j++) {
                   if (i === 0) {
                     cal[i].push('<th style="width:50px;background-color:lightgrey;">' + details.weekDays[j] + '</th>');
                   } else if (day > details.totalDays) {
                     cal[i].push('<td>&nbsp;</td>');
                   } else {
                     if (i === 1 && j < start) {
                       cal[i].push('<td>&nbsp;</td>');
                     } else {
                     var month = d.getMonth()+1;
                     //console.log(month);
                     monthStr = month.toString();
                       if (monthStr.length== 1){
                       monthStr="0"+monthStr;
                       }dayStr = day.toString();
                       if (dayStr.length== 1){
                       dayStr="0"+dayStr;
                       }
                      var datte = d.getFullYear()+"-" +monthStr+"-" +(dayStr).toString()
                       var ToDate = new Date();
                      var userdatte = new Date(datte + "T00:00:00");
                      ToDate.setDate(ToDate.getDate() - 1);
                      console.log(57);
                      console.log(ToDate);
                      console.log(userdatte);
                      console.log(new Date(userdatte).getTime() < ToDate.getTime());
                     if (new Date(userdatte).getTime() < ToDate.getTime()){
                     cal[i].push('<td class="day" style="background-color:lightgrey;"  >' + day++ + '</td>');

                     }else{
                       cal[i].push('<td class="day" style="background-color:'+ response['defaultColor']+'"  onClick="getCustSlots(\''+ datte + '\')" >'+ day++ + '</td>');
                       }
                     }
           //cal[i].push('<td class="day" style="background-color:'+ response.datesMap[day++][1] +';" >' + day++ + '</td>');

                   }
                 }
                 cal[i].push('</tr>');
               }
               //console.log(cal);
               cal = cal.reduce(function(a, b) {
                 return a.concat(b);
               }, []).join('');
               $('.caltable').append(cal);
               $('#month').text(details.months[d.getMonth()]);
               $('#year').text(d.getFullYear());
               $('td.day').mouseover(function() {
                 $(this).addClass('hover');
               }).mouseout(function() {
                 $(this).removeClass('hover');
               });
           })


  }

  $('#left').click(function() {
    $('.caltable').text('');
    if (currentDate.getMonth() === 0) {
      currentDate = new Date(currentDate.getFullYear() - 1, 11);
      generateCalendar(currentDate);
    } else {
      currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1)
      generateCalendar(currentDate);
    }
  });
  $('#right').click(function() {
    $('.caltable').html('<tr></tr>');
    if (currentDate.getMonth() === 11) {
      currentDate = new Date(currentDate.getFullYear() + 1, 0);
      generateCalendar(currentDate);
    } else {
      currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1)
      generateCalendar(currentDate);
    }
  });
  generateCalendar(currentDate);
});

function getCustSlots(datte){
    window.location.href = "bookSlot?date="+datte;
}
