var IService = require('using-thrift')
  , ttypes = IService.ttypes;

var options = {
  HOST : "127.0.0.1"
, PORT : 13001
};

// Thrift connect
var iService = new IService(options);
iService.connect();

var request = {
  myName : "robot"
, text : "I'm sexy and I know it!"
};

entity  = new ttypes.IRequest(request);
 
iService.client.query(entity, function() {
  console.log("Complete!");
});
