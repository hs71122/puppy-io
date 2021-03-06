package com.lovi.puppy.message;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import com.lovi.puppy.exceptions.ServiceCallerException;
import com.lovi.puppy.exceptions.message.ErrorMessage;
import com.lovi.puppy.message.factory.ResultFactory;
import com.lovi.puppy.message.handler.ResultHandler;

/**
 * Result is used to handle the result return from the service method which is called by ServiceCaller.
 * @author Tharanga Thennakoon
 * @see ServiceCaller
 * @see FailResult
 * @param <T> The data type of the return value of the service method.
 */
public interface Result<T> {

	/**
	 * Create new instance from Result class
	 * @return
	 */
	static <T> Result<T> create() {
		return factory.<T> create();
	}

	Handler<AsyncResult<Message<MessageBody>>> getHandler();

	/**
	 * Set a handler for processing the result.
	 * @param handler The Handler that will be called after the failure occur.
	 * @param failResult The FailResult for handle failure.
	 */
	default void process(ResultHandler<T> handler, FailResult failResult) {
		
		getVerxFuture().compose(new Handler<Message<MessageBody>>() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(Message<MessageBody> message) {
				try {
					MessageBody messageBody = message.body();
					handler.handle((T) messageBody.getValues()[0]);
					
				} catch (Exception e) {
					failResult.getVerxFuture().fail(new ServiceCallerException(ErrorMessage.SERVICE_CALL_RETURN_TYPE_MIS_MATCH.getMessage()));
				}
			}
		}, failResult.getVerxFuture());
	}
	
	/**
	 * Set a handler for processing the result.
	 * @param handler The Handler that will be called after the failure occur.
	 */
	default void process(ResultHandler<T> handler) {
		
		getVerxFuture().compose(new Handler<Message<MessageBody>>() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(Message<MessageBody> message) {
				try {
					MessageBody messageBody = message.body();
					handler.handle((T) messageBody.getValues()[0]);
					
				} catch (Exception e) {
					
				}
			}
		},Future.future());
	}

	Future<Message<MessageBody>> getVerxFuture();

	static ResultFactory factory = new ResultFactory();

}
