var thrift = require('helenus-thrift')
  , IService = require('./generated-sources/IService.js')
  , ttypes = require('./generated-sources/entities_types.js');

function Service (config) {

thrift.protocol.TBinaryProtocol.prototype.writeString = function(str) {
  str = new Buffer(str).toString('binary')
  this.writeI32(str.length);
  this.trans.write(str);
 }

thrift.protocol.TBinaryProtocol.prototype.readString = function() {
  var r = this.readBinary().toString('utf8');
  return r;
}
          
          
  this.options = {
    transport: thrift.transport.TBufferedTransport
  , protocol: thrift.protocol.TBinaryProtocol
  };

  this.config = config;
};

Service.prototype.connect = function () {
  var self = this;

  // Make connect
  this.connection = thrift.createConnection(this.config.HOST, this.config.PORT, this.options);

  // Event for error
  this.connection.on('error', function(err) {
    console.log(err);
    self.end();
  });

  // Event for close
  this.connection.on('close', function() {
    self.connect();
  });

  // Thrift client
  this.client = thrift.createClient(IService, this.connection);
};

Service.prototype.end = function() {
  this.connection.end();
};

module.exports = Service;
module.exports.ttypes = ttypes;
