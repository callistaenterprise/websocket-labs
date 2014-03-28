package se.callista.websocketlabs.springmvc.asynch.teststub;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

public class ProcessingCallbackTask extends TimerTask {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessingCallbackTask.class);

    private long reqId;
	private DeferredResult<ProcessingStatus> deferredResult;
	private int processingTimeMs;
	
	public ProcessingCallbackTask(long reqId, int processingTimeMs, DeferredResult<ProcessingStatus> deferredResult) {
	    this.reqId = reqId;
	    this.processingTimeMs = processingTimeMs;
		this.deferredResult = deferredResult;
	}
	
	@Override
	public void run() {
	    if (deferredResult.isSetOrExpired()) {
            LOG.warn("Processing of non-blocking request #{} already expiered", reqId);        
	    } else {
	        boolean ok = deferredResult.setResult(new ProcessingStatus("Ok", processingTimeMs));
            LOG.info("Processing of non-blocking request #{} done, setResult() returned {}", reqId, ok);        
	    }
	}
}
