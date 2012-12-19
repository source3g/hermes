function(){
	if(this.callRecords!=null){
	this.callRecords.forEach(function(record)
	{
		var month=(record.callTime.getMonth()+1).toString();
		if(month.length==1){
			month="0"+month;
		}
		var day=record.callTime.getDate().toString();
		if(day.length==1){
			day="0"+day;
		}
	if(record.newCustomer!=true){
		emit(month+"-"+day,{count:1});//record.callTime.getFullYear()+"-"+
	}
	});
	}
}