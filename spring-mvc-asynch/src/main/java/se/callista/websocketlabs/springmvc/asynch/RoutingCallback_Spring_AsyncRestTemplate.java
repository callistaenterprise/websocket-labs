package se.callista.websocketlabs.springmvc.asynch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.context.request.async.DeferredResult;

public class RoutingCallback_Spring_AsyncRestTemplate implements ListenableFutureCallback<ResponseEntity<String>> {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingCallback_Spring_AsyncRestTemplate.class);

    private long reqId;
    private DeferredResult<String> deferredResult;

    public RoutingCallback_Spring_AsyncRestTemplate(long reqId, DeferredResult<String> deferredResult) {
        this.reqId = reqId;
        this.deferredResult = deferredResult;
    }

    @Override
    public void onSuccess(ResponseEntity<String> result) {

        // TODO: Handle status codes other than 200...

        if (deferredResult.isSetOrExpired()) {
            LOG.warn("Processing of non-blocking routing #{} already expiered", reqId);        
        } else {
            boolean ok = deferredResult.setResult(result.getBody());
            LOG.debug("Processing of non-blocking routing #{} done, http-status = {}, setResult() returned {}", reqId, result.getStatusCode(), ok);        
        }
        
    }

    @Override
    public void onFailure(Throwable t) {

        // TODO: Handle asynchronous processing errors...

        if (deferredResult.isSetOrExpired()) {
            LOG.warn("Processing of non-blocking routing #{} caused an exception: {}", reqId, t);        
        }
    }
}
