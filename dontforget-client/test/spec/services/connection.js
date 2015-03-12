'use strict';

describe('Service: Connection', function () {

  // load the service's module
  beforeEach(module('dontforgetApp'));

  // instantiate service
  var Connection;
  beforeEach(inject(function (_Connection_) {
    Connection = _Connection_;
  }));

  it('should do something', function () {
    expect(!!Connection).toBe(true);
  });

});
