function(){
	if(this.callRecords!=null){
	this.callRecords.forEach(function(record)
	{
	emit(record.callTime.getFullYear()+"-"+record.callTime.getMonth()+"-"+ +record.callTime.getDay(),{count:1});
	});
	}
}