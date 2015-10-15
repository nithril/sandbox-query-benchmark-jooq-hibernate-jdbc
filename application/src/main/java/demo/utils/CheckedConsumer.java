package demo.utils;

import java.util.function.Consumer;

/**
 * Created by nlabrot on 05/10/15.
 */
@FunctionalInterface
public interface CheckedConsumer<T> extends Consumer<T> {

	void checkedAccept(T t) throws Exception;

	@Override
	default void accept(T t){
		try{
			checkedAccept(t);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}
