var thrift = require('helenus-thrift')
  , IService = require('./generated-sources/IService.js')
  , ttypes = require('./generated-sources/entities_types.js');

function Service (config) {
  var options = {
    transport: thrift.transport.TBufferedTransport
  , protocol: thrift.protocol.TBinaryProtocol
  };

  this.connection = thrift.createConnection(config.HOST, config.PORT, options);

  this.connection.on('error', function(err) {
    this.connection.end();
    console.log(err);
  });

  this.connection.on('close', this.connect);
};

Service.prototype.connect = function () {
  this.client = thrift.createClient(IService, this.connection);
};

module.exports = Service;
module.exports.ttypes = ttypes;
