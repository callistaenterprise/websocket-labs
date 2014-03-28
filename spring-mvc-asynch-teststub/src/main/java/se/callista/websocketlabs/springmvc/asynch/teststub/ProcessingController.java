package se.callista.websocketlabs.springmvc.asynch.teststub;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class ProcessingController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingController.class);

    private static Timer timer = new Timer();
    private static final AtomicLong lastRequestId = new AtomicLong(0);

    @RequestMapping("/process-blocking")
    public ProcessingStatus blockingProcessing(
        @RequestParam(value = "minMs", required = false, defaultValue = "0") int minMs,
        @RequestParam(value = "maxMs", required = false, defaultValue = "0") int maxMs) {

        long reqId = lastRequestId.getAndIncrement();
        
        int processingTimeMs = calculateProcessingTime(minMs, maxMs);

        LOG.info("Start blocking request #{}, processing time: {} ms.", reqId, processingTimeMs);

        try {
            Thread.sleep(processingTimeMs);
        } catch (InterruptedException e) {}

        LOG.info("Processing of blocking request #{} is done", reqId);        

        return new ProcessingStatus("Ok", processingTimeMs);
    }

    @RequestMapping("/process-non-blocking")
    public DeferredResult<ProcessingStatus> nonBlockingProcessing(
        @RequestParam(value = "minMs", required = false, defaultValue = "0") int minMs,
        @RequestParam(value = "maxMs", required = false, defaultValue = "0") int maxMs) {

        long reqId = lastRequestId.getAndIncrement();
        
        int processingTimeMs = calculateProcessingTime(minMs, maxMs);

        LOG.info("Start non-blocking request #{}, processing time: {} ms.", reqId, processingTimeMs);

        DeferredResult<ProcessingStatus> deferredResult = new DeferredResult<>();
        ProcessingCallbackTask task = new ProcessingCallbackTask(reqId, processingTimeMs, deferredResult);

        // Schedule the task for asynch completion in the future
        timer.schedule(task, processingTimeMs);

        LOG.info("Processing of non-blocking request #{} leave the request thread", reqId);

        // Return to let go of the precious thread we are holding on to...
        return deferredResult;
    }
    
    private int calculateProcessingTime(int minMs, int maxMs) {
        if (maxMs < minMs) maxMs = minMs;
        int processingTimeMs = minMs + (int) (Math.random() * (maxMs - minMs));
        return processingTimeMs;
    }
    
}