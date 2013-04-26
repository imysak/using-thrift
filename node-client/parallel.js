var async = require('async')
  , IService = require('using-thrift')
  , ttypes = IService.ttypes;

var options = {
  HOST : "127.0.0.1"
, PORT : 13001
};

// Thrift connect
var iService = new IService(options);
iService.connect();

var tasks = [];
for (var i = 0; i < 10; i++) {
  tasks.push( Task(i) );
};

async.parallel(tasks, function(err, result) {
  console.log(err, result);

  iService.end();
});

function Task (i) {
  var request = {
      myName : "robot " + i
    , text : "I'm sexy and I know it!"
  };

  return function(callback) {
    var entity = new ttypes.IRequest(request);

    iService.client.query(entity, function(err, response) {
      callback(err, response);
    });
  };
}
