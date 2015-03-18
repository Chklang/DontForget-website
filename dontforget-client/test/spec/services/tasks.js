'use strict';

describe('Service: tasks', function () {

  // load the service's module
  beforeEach(module('dontforgetApp'));

  // instantiate service
  var tasks;
  beforeEach(inject(function (_tasks_) {
    tasks = _tasks_;
  }));

  it('should do something', function () {
    expect(!!tasks).toBe(true);
  });

});
