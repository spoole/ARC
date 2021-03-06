'use strict';

function recreateWebSQLDatabase() {
    resetDbs()
    .then(reCreateDbs)
    .then(fillWebSQL)
    .then(function(){
        console.log('import finished.')
    })
}

function resetDbs() {
    return arc.app.db.websql.resetDatabases()
      .then(arc.app.db.idb.deleteDatabase);
}
function reCreateDbs() {
    return arc.app.db.websql.open()
    .then(function() {
      console.log('WebSQL database has been initialized');
      return installWebSQLApp();
    })
    .catch(function(e) {
      console.error('Error initializing the database.', e);
    })
    .then(arc.app.db.idb.open)
    .then(function() {
      console.log('IndexedDB database has been initialized');
    })
    .catch(function(e) {
      console.error('Error initializing the database.', e);
    });
}



var fillWebSQL = function() {
    var dataSize = 1234;
    var projectsSize = 12;
    var historySize = 4321;
    var projects = getSqlProjectData(projectsSize);
    var requests = getSqlRequestData(dataSize);
    var projectsRequests = [];
    var history = getSqlHistoryData(historySize);
    for(let i=0;i<projects.length;i++) {
        let projectId = i+1;
        projects[i].id = projectId;
        var includedRequests = chance.integer({min: 1, max: Math.min(5, requests.length)});
        for(var j=0; j<includedRequests; j++) {
            var requestIndex = chance.integer({min: 0, max: requests.length-1});
            let req = requests[requestIndex];
            req.project = projectId;
            projectsRequests.push(req);
            requests.splice(requestIndex, 1);
        }
    }
    window.orygRequests = Object.assign({}, requests);
    window.projectsRequests = projectsRequests;
    arc.app.db.useIdb = false;
    arc.app.db.projects.importData(projects, projectsRequests)
    .then(arc.app.db.requests.importList(requests))
    .then(function() {
        let promises = [];
        for(let i=0;i<historySize;i++) {
            promises.push(arc.app.db.requests.insert(history[i], 'history'));
        }
        return Promise.all(promises);
    });
};
var getSqlProjectData = function(dataSize) {
    var list = [];
    for(var i=0; i<dataSize; i++) {
        list.push(generateSqlProjectData());
    }
    return list;
};
var getSqlRequestData = function(dataSize) {
    var list = [];
    for(var i=0; i<dataSize; i++) {
        list.push(generateSqlRequestData());
    }
    return list;
};
var getSqlHistoryData = function(dataSize) {
    var list = [];
    for(var i=0; i<dataSize; i++) {
        list.push(generateSqlHistoryData());
    }
    return list;
};
var generateSqlRequestData = function() {
    var isPayload = chance.bool();
    var payloadMethods = ['POST', 'PUT', 'DELETE', 'OPTIONS'];
    var otherMethods = ['GET','HEAD'];
    var headersSize = chance.integer({min: 0, max: 10});
    var headers = '';
    for (var i=0; i<headersSize; i++) {
        headers += 'X-' + chance.word() + ': ' + chance.word() + '\n';
    }
    var payload = isPayload ? chance.paragraph() : '';
  var schema = {
      'name': chance.sentence({words: 2}),
      'project': 0,
      'url': chance.url(),
      'method': chance.pick(isPayload ? payloadMethods : otherMethods),
      'headers': headers,
      'payload': payload,
      'time': chance.hammertime()
  };
  return schema;
};
var generateSqlProjectData = function() {
    var schema = {
      'name': chance.sentence({words: 2}),
      'time': chance.hammertime()
    };
    return schema;
};

var generateSqlHistoryData = function() {
    var isPayload = chance.bool();
    var payloadMethods = ['POST', 'PUT', 'DELETE', 'OPTIONS'];
    var otherMethods = ['GET','HEAD'];
    var headersSize = chance.integer({min: 0, max: 10});
    var headers = '';
    for (var i=0; i<headersSize; i++) {
        headers += 'X-' + chance.word() + ': ' + chance.word() + '\n';
    }
    var payload = isPayload ? chance.paragraph() : '';
    var schema = {
      'url': chance.url(),
      'method': chance.pick(isPayload ? payloadMethods : otherMethods),
      'headers': headers,
      'payload': payload,
      'time': chance.hammertime()
    };
    return schema;
};
/**
 * Install old system database.
 * This is only temporary here until upgrade to packaged apps.
 */
var installWebSQLApp = function() {
  return downloadDefinitions()
    .then(installDefinitions)
    .then(function() {
      console.log('App database has been filled with default values.');
      chrome.storage.local.set({
        'firstrun': true
      });
    })
    .catch(function(r) {
      console.error('There was an error when filling up the database with definitions.', r);
    });
};
/**
 * Get ARC's definitions list.
 * This is only temporary here until upgrade to packaged apps.
 */
var downloadDefinitions = function() {
  return fetch(chrome.runtime.getURL('assets/definitions.json'))
    .then(function(response) {
      return response.json();
    });
};
/**
 * Add definitions to the database.
 */
var installDefinitions = function(defs) {
  if (!defs || !defs.codes || !defs.requests || !defs.responses) {
    return Promise.reject({
      'message': 'No definitions found'
    });
  }
  return arc.app.db.websql.insertStatusCodes(defs.codes)
    .then(function() {
      defs.requests.forEach(function(item) {
        item.type = 'request';
      });
      defs.responses.forEach(function(item) {
        item.type = 'response';
      });
      let save = defs.requests.concat(defs.responses);
      return arc.app.db.websql.insertHeadersDefinitions(save);
    });
};