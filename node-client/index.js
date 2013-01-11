
// Thrift connect
var iService = new IService({ HOST : "127.0.0.1", PORT : 13001});
iService.connect();

entity  = new ttypes.IRequest({myName : "robot", text : "I'm sexy and I know it!"});
 
iService.query(entity);

console.log("Complete!");